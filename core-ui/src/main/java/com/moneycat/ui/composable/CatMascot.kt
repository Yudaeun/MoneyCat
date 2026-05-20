package com.moneycat.ui.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moneycat.domain.model.CatExpression

@Composable
fun CatMascot(
    expression: CatExpression,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    // 표정 전환 시 부드러운 애니메이션
    val pupilScale by animateFloatAsState(
        targetValue = when (expression) {
            CatExpression.ALERT -> 1.4f
            CatExpression.SLEEPING, CatExpression.HAPPY -> 0f
            else -> 1f
        },
        animationSpec = tween(300),
        label = "pupilScale"
    )
    val mouthCurve by animateFloatAsState(
        targetValue = when (expression) {
            CatExpression.HAPPY -> 1f
            CatExpression.ALERT -> -1f
            else -> 0f
        },
        animationSpec = tween(300),
        label = "mouthCurve"
    )

    Canvas(modifier = modifier.size(size)) {
        val scale = this.size.width / 100f

        // 몸통 (검은 고양이)
        drawCircle(
            color = Color(0xFF2A2A2A),
            radius = 38f * scale,
            center = Offset(50f * scale, 55f * scale)
        )

        // 귀 (왼쪽)
        val leftEar = Path().apply {
            moveTo(20f * scale, 30f * scale)
            lineTo(30f * scale, 10f * scale)
            lineTo(40f * scale, 28f * scale)
            close()
        }
        drawPath(leftEar, color = Color(0xFF2A2A2A))

        // 귀 (오른쪽)
        val rightEar = Path().apply {
            moveTo(60f * scale, 28f * scale)
            lineTo(70f * scale, 10f * scale)
            lineTo(80f * scale, 30f * scale)
            close()
        }
        drawPath(rightEar, color = Color(0xFF2A2A2A))

        // 얼굴
        drawCircle(
            color = Color(0xFF333333),
            radius = 30f * scale,
            center = Offset(50f * scale, 45f * scale)
        )

        drawEyes(expression, pupilScale, scale)
        drawNose(scale)
        drawMouth(expression, mouthCurve, scale)
        drawWhiskers(scale)

        if (expression == CatExpression.SLEEPING) {
            drawZzz(scale)
        }
        if (expression == CatExpression.HAPPY) {
            drawBlush(scale)
        }
    }
}

private fun DrawScope.drawEyes(expression: CatExpression, pupilScale: Float, scale: Float) {
    val eyeY = 40f * scale
    val leftEyeX = 38f * scale
    val rightEyeX = 62f * scale

    when (expression) {
        CatExpression.SLEEPING, CatExpression.HAPPY -> {
            // 눈 감기: 호 형태
            drawArc(
                color = Color(0xFF9DD8E0),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(leftEyeX - 6f * scale, eyeY - 4f * scale),
                size = Size(12f * scale, 8f * scale),
                style = Stroke(width = 2f * scale, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF9DD8E0),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(rightEyeX - 6f * scale, eyeY - 4f * scale),
                size = Size(12f * scale, 8f * scale),
                style = Stroke(width = 2f * scale, cap = StrokeCap.Round)
            )
        }
        else -> {
            // 눈 바깥 (연하늘)
            drawCircle(Color(0xFF9DD8E0), 7f * scale, Offset(leftEyeX, eyeY))
            drawCircle(Color(0xFF9DD8E0), 7f * scale, Offset(rightEyeX, eyeY))
            // 눈 안쪽 (C4E2E2)
            drawCircle(Color(0xFFC4E2E2), 5f * scale, Offset(leftEyeX, eyeY))
            drawCircle(Color(0xFFC4E2E2), 5f * scale, Offset(rightEyeX, eyeY))
            // 동공
            val pupilRadius = 3f * scale * pupilScale
            drawCircle(Color(0xFF1A1A1A), pupilRadius, Offset(leftEyeX, eyeY))
            drawCircle(Color(0xFF1A1A1A), pupilRadius, Offset(rightEyeX, eyeY))
            // 하이라이트
            drawCircle(
                Color(0xCCFFFFFF),
                1.5f * scale,
                Offset(leftEyeX + 2f * scale, eyeY - 2f * scale)
            )
            drawCircle(
                Color(0xCCFFFFFF),
                1.5f * scale,
                Offset(rightEyeX + 2f * scale, eyeY - 2f * scale)
            )
        }
    }
}

private fun DrawScope.drawNose(scale: Float) {
    val nosePath = Path().apply {
        moveTo(50f * scale, 49f * scale)
        lineTo(47f * scale, 53f * scale)
        lineTo(53f * scale, 53f * scale)
        close()
    }
    drawPath(nosePath, color = Color(0xFFE8A0AA))
}

private fun DrawScope.drawMouth(expression: CatExpression, mouthCurve: Float, scale: Float) {
    val mouthPath = Path().apply {
        val midY = 56f * scale
        val curveY = midY + (4f * mouthCurve * scale)
        moveTo(44f * scale, midY)
        quadraticTo(50f * scale, curveY, 56f * scale, midY)
    }
    drawPath(
        mouthPath,
        color = Color(0xFF555555),
        style = Stroke(width = 1.5f * scale, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawWhiskers(scale: Float) {
    val whiskerColor = Color(0xFF555555)
    val strokeWidth = 1f * scale
    // 왼쪽 수염
    drawLine(whiskerColor, Offset(18f * scale, 50f * scale), Offset(40f * scale, 52f * scale), strokeWidth)
    drawLine(whiskerColor, Offset(18f * scale, 55f * scale), Offset(40f * scale, 55f * scale), strokeWidth)
    // 오른쪽 수염
    drawLine(whiskerColor, Offset(60f * scale, 52f * scale), Offset(82f * scale, 50f * scale), strokeWidth)
    drawLine(whiskerColor, Offset(60f * scale, 55f * scale), Offset(82f * scale, 55f * scale), strokeWidth)
}

private fun DrawScope.drawZzz(scale: Float) {
    // 간단한 Z 표현 (텍스트 대신 도형으로)
    val zPath = Path().apply {
        moveTo(72f * scale, 20f * scale)
        lineTo(80f * scale, 20f * scale)
        lineTo(72f * scale, 28f * scale)
        lineTo(80f * scale, 28f * scale)
    }
    drawPath(
        zPath,
        color = Color(0xFFB5C3C3),
        style = Stroke(width = 2f * scale, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawBlush(scale: Float) {
    drawCircle(Color(0x44E8A0AA), 8f * scale, Offset(30f * scale, 52f * scale))
    drawCircle(Color(0x44E8A0AA), 8f * scale, Offset(70f * scale, 52f * scale))
}

@Preview(showBackground = true, name = "Default")
@Composable
private fun PreviewDefault() = CatMascot(CatExpression.DEFAULT)

@Preview(showBackground = true, name = "Happy")
@Composable
private fun PreviewHappy() = CatMascot(CatExpression.HAPPY)

@Preview(showBackground = true, name = "Alert")
@Composable
private fun PreviewAlert() = CatMascot(CatExpression.ALERT)

@Preview(showBackground = true, name = "Thinking")
@Composable
private fun PreviewThinking() = CatMascot(CatExpression.THINKING)

@Preview(showBackground = true, name = "Sleeping")
@Composable
private fun PreviewSleeping() = CatMascot(CatExpression.SLEEPING)
