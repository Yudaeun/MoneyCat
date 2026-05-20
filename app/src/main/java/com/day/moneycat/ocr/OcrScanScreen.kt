package com.day.moneycat.ocr

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrScanScreen(
    onBack: () -> Unit,
    onAmountDetected: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { ContextCompat.getMainExecutor(context) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var detectedAmount by remember { mutableStateOf("") }
    var editableAmount by remember { mutableStateOf("") }

    val recognizer = remember {
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }
    DisposableEffect(Unit) { onDispose { recognizer.close() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("영수증 스캔", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
        containerColor = Color.Black,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // 카메라 프리뷰
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }.also { previewView ->
                        val providerFuture = ProcessCameraProvider.getInstance(ctx)
                        providerFuture.addListener({
                            val provider = providerFuture.get()
                            val preview = Preview.Builder().build()
                                .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                            val capture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                .build()
                            imageCapture = capture
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                capture,
                            )
                        }, executor)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            // 스캔 가이드 오버레이
            Canvas(modifier = Modifier.fillMaxSize()) {
                val guideW = size.width * 0.85f
                val guideH = size.height * 0.45f
                val left = (size.width - guideW) / 2f
                val top = (size.height - guideH) / 2f

                drawRect(color = Color.Black.copy(alpha = 0.55f), size = size)
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(guideW, guideH),
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    blendMode = BlendMode.Clear,
                )
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.8f),
                    topLeft = Offset(left, top),
                    size = Size(guideW, guideH),
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
                )
            }

            // 안내 텍스트
            Text(
                text = "영수증을 네모 안에 맞추고 촬영 버튼을 누르세요",
                color = Color.White,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-(LocalContext.current.resources.displayMetrics.heightPixels * 0.26f).dp)),
            )

            // 촬영 버튼
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FloatingActionButton(
                    onClick = {
                        if (!isCapturing) {
                            isCapturing = true
                            imageCapture?.takePicture(
                                executor,
                                object : ImageCapture.OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        val mediaImage = image.image
                                        if (mediaImage == null) {
                                            image.close()
                                            isCapturing = false
                                            return
                                        }
                                        val inputImage = InputImage.fromMediaImage(
                                            mediaImage,
                                            image.imageInfo.rotationDegrees,
                                        )
                                        recognizer.process(inputImage)
                                            .addOnSuccessListener { result ->
                                                detectedAmount = extractAmount(result.text)
                                                editableAmount = detectedAmount
                                                showResultDialog = true
                                                isCapturing = false
                                            }
                                            .addOnFailureListener {
                                                isCapturing = false
                                            }
                                            .addOnCompleteListener { image.close() }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        isCapturing = false
                                    }
                                },
                            )
                        }
                    },
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = Color.White,
                            strokeWidth = 3.dp,
                        )
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = "촬영", modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }

    // 결과 다이얼로그
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = { Text("금액 확인") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (detectedAmount.isEmpty()) {
                        Text(
                            "금액을 인식하지 못했습니다.\n직접 입력해 주세요.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                    } else {
                        Text(
                            "인식된 금액",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                    }
                    OutlinedTextField(
                        value = editableAmount,
                        onValueChange = { editableAmount = it },
                        label = { Text("금액") },
                        suffix = { Text("원") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAmountDetected(editableAmount.replace(",", ""))
                        showResultDialog = false
                    },
                    enabled = editableAmount.isNotBlank(),
                ) { Text("거래 입력으로 이동") }
            },
            dismissButton = {
                TextButton(onClick = { showResultDialog = false }) { Text("다시 촬영") }
            },
        )
    }
}

private fun extractAmount(text: String): String {
    val patterns = listOf(
        Regex("""합\s*계\s*[:\s]*([0-9,]+)"""),
        Regex("""결\s*제\s*금\s*액\s*[:\s]*([0-9,]+)"""),
        Regex("""총\s*금\s*액\s*[:\s]*([0-9,]+)"""),
        Regex("""₩\s*([0-9,]+)"""),
        Regex("""([0-9,]+)\s*원"""),
    )
    for (pattern in patterns) {
        val match = pattern.find(text) ?: continue
        val raw = match.groupValues[1].replace(",", "")
        if (raw.length in 3..8) return raw
    }
    return ""
}
