# MoneyCat — Claude 작업 노트

검은 고양이 마스코트의 온디바이스 AI 가계부 앱. 취업 포트폴리오 목적.

## 모듈 구조

| 모듈 | 패키지 | 역할 |
|------|--------|------|
| `:app` | `com.day.moneycat` | Presentation (Compose UI, Navigation) |
| `:domain` | `com.moneycat.domain` | 순수 Kotlin — UseCase, 도메인 모델, Repository 인터페이스 |
| `:data` | `com.moneycat.data` | Room+SQLCipher, Retrofit, Repository 구현체, DI |
| `:core-security` | `com.moneycat.security` | BiometricAuth, PIN, Session, Crypto, RootDetector |
| `:core-ui` | `com.moneycat.ui` | MoneyCat 테마(민트 팔레트), CatMascot Composable |
| `:core-common` | `com.moneycat.common` | Result sealed class |

## 핵심 규칙

- `domain` 모듈에 Android API import 금지 (순수 Kotlin)
- 의존성 방향: `app` → `domain` ← `data`, `core-security` → `core-common`
- `MainActivity`는 반드시 `AppCompatActivity` 유지 (BiometricPrompt가 FragmentActivity 필요)
- 앱 테마는 `com.moneycat.ui.theme.MoneyCatTheme` (core-ui) 사용

## 빌드 설정

- AGP 8.9.1 / Kotlin 2.2.0 / KSP 2.2.0-2.0.2
- compileSdk=36, minSdk=29, targetSdk=35
- Hilt 2.51.1 / Room 2.6.1 / SQLCipher 4.5.4
- Firebase: `google-services.json` 없어서 `app/build.gradle.kts`에서 주석 처리 중

---

## 완료된 작업

### Week 1 (2026-04-27) — 인프라 구축

**Room DB**
- 9개 Entity: UserProfile, Asset, Transaction, Card, CardBenefit, Budget, ExchangeRate, AiInsight, NotificationRule
- 9개 DAO (Flow 기반 쿼리)
- TypeConverters: BigDecimal↔String, LocalDate/DateTime↔String, Enum↔String
- SQLCipher 암호화 (EncryptedSharedPreferences로 passphrase 관리)

**인증 시스템** (`core-security`)
- BiometricPrompt → 5회 실패 시 PIN 폴백
- PIN: PBKDF2WithHmacSHA256 (65536 iterations, 256-bit)
- 세션 타임아웃 10분, PIN 10회 실패 시 30분 잠금
- FLAG_SECURE 인증 화면 적용

**UI**
- MoneyCat 민트 팔레트 테마 (라이트/다크)
- CatMascot Composable (Canvas 드로잉, 5가지 표정)
- AuthScreen + AuthViewModel 완성 (생체인증 + PIN 설정/입력/잠금 화면)

**기타**
- GitHub Actions CI (lint/test/build)
- MoneyCatApplication (ProcessLifecycle 세션 관리)

---

### Week 2 초입 (2026-04-28) — Domain/Data 연결 + 앱 진입 흐름

**Domain 레이어**
- 도메인 모델: `Transaction`, `Asset`, `Budget`, `UserProfile`, `CategorySummary`
- Repository 인터페이스: `TransactionRepository`, `AssetRepository`, `BudgetRepository`, `UserProfileRepository`

**Data 레이어**
- `EntityMapper.kt` — Entity ↔ Domain 양방향 매핑 (extension functions)
- Repository 구현체: `TransactionRepositoryImpl`, `AssetRepositoryImpl`, `BudgetRepositoryImpl`, `UserProfileRepositoryImpl`
- `RepositoryModule` — `@Binds` abstract class로 인터페이스↔구현체 바인딩 완성

**App 레이어**
- `MainActivity`: `AppCompatActivity` 전환, `@AndroidEntryPoint`, `BiometricAuthManager` inject
- `NavHost` 설정: `auth` → `home` (popUpTo로 백스택 정리)
- `HomeScreen` 플레이스홀더 추가
- `app/build.gradle.kts` — `androidx.appcompat` 의존성 추가

---

### Week 2 본격 (2026-05-16) — Navigation + 거래/통계/설정 화면

**Navigation 구조 개편**
- `MainActivity`: auth → main(Bottom Nav) → add_transaction
- `MainScreen`: `NavigationBar` 4탭 (홈/거래/통계/설정) + FAB (거래 추가)
- 탭 전환 시 `saveState/restoreState` 적용

**거래 내역 화면** (`transaction/TransactionListScreen`)
- 월 네비게이션 (← yyyy년 mm월 →)
- 전체/수입/지출 FilterChip 필터
- 날짜별 그룹핑 + 스와이프 삭제 (`SwipeToDismissBox`)
- 월별 수입/지출/합계 요약 Row
- `TransactionListViewModel`: `flatMapLatest` + `DeleteTransactionUseCase`

**통계 화면** (`statistics/StatisticsScreen`)
- 월별 수입/지출/잔액 요약 Card
- Vico `CartesianChartHost` + `ColumnCartesianLayer` (카테고리별 지출 바 차트)
- 카테고리별 `LinearProgressIndicator` + 비율 표시
- `StatisticsViewModel`: `getMonthlyCategoryTotals` + `getByDateRange` combine

**설정 화면** (`settings/SettingsScreen`)
- 고양이 마스코트 + 앱 버전
- 섹션별 설정 항목 (보안/데이터/알림/앱 정보)

**Domain**
- `DeleteTransactionUseCase` 추가

**기타**
- `material-icons-extended` 추가 (filled/outlined 아이콘 쌍)
- `themes.xml`: `Theme.AppCompat.DayNight.NoActionBar` (AppCompatActivity 호환)
- Kotlin `2.0.21` + KSP `2.0.21-1.0.28` 최종 안정 조합

---

### Week 3 (2026-05-16) — 예산/자산/OCR/알림

**예산 관리** (`budget/BudgetScreen`)
- `BudgetCard`: 카테고리별 예산 vs 지출 진행 바 (녹색/주황/빨강 색상)
- `BudgetEditDialog`: ExposedDropdownMenuBox 카테고리 선택, Slider 알림 임계값
- `BudgetViewModel`: `BudgetWithSpending` (ratio, isOverBudget, isNearLimit), `flatMapLatest`
- UseCases: `UpsertBudgetUseCase`, `DeleteBudgetUseCase`

**자산 관리** (`asset/AssetScreen`)
- 자산 유형별 그룹 (계좌/현금/투자 등) + 총 자산 합계
- `AssetItem` 편집/삭제 아이콘 버튼
- `AssetEditDialog`: ExposedDropdownMenuBox 유형 선택
- UseCases: `InsertAssetUseCase`, `UpdateAssetUseCase`, `DeleteAssetUseCase`

**OCR 영수증 스캔** (`ocr/OcrScanScreen`)
- CameraX `ProcessCameraProvider` + `PreviewView` (AndroidView)
- MLKit `TextRecognition` + `KoreanTextRecognizerOptions`
- Canvas 반투명 가이드 사각형 (BlendMode.Clear)
- 금액 추출 정규식: 합계/결제금액/총금액/₩/원
- 금액 감지 결과 다이얼로그 → `savedStateHandle["ocr_amount"]`으로 AddTransactionScreen에 전달

**CSV 내보내기** (`settings/CsvExporter`)
- MediaStore `EXTERNAL_CONTENT_URI` + `IS_PENDING` 플래그 (WRITE_EXTERNAL_STORAGE 불필요)
- `PrintWriter` + RFC 4180 CSV 인코딩 (opencsv 미사용, 순수 Java I/O)
- `SettingsViewModel`: `ExportResult` sealed interface (Loading/Success/Error)

**WorkManager 예산 알림** (`worker/BudgetAlertWorker`)
- `@HiltWorker` + `@AssistedInject`, `CoroutineWorker`
- 12시간 주기 알림, `NotificationCompat.Builder`
- `MoneyCatApplication`: `Configuration.Provider`, HiltWorkerFactory inject
- Manifest: WorkManager 기본 초기화 제거 → HiltWorkerFactory 사용

**Navigation 완성**
- `MainActivity`: `add_transaction` ↔ `scan_receipt` 라우트 (savedStateHandle OCR 금액 전달)
- `AddTransactionScreen`: `PhotoCamera` 아이콘 버튼 (TopAppBar actions), `ocrAmount` LaunchedEffect
- `HomeScreen.AssetCard`: `onClick` 파라미터 추가 → 자산 화면 이동
- `StatisticsScreen`: `onNavigateToBudget` 파라미터 추가

---

---

### Week 4 (2026-05-16) — AI 소비 인사이트

**AI 인사이트 엔진** (`domain/usecase`)
- `GenerateMonthlyInsightUseCase`: 규칙 기반 온디바이스 소비 분석
  - SPENDING_ALERT: 예산 90% 초과 시
  - SAVING_TIP: 최다 지출 카테고리 10% 절약 제안
  - ANOMALY: 평균의 2.5배 이상 단건 지출 감지
  - WEEKLY_SUMMARY: 월간 수입/지출/건수 요약
- `GetInsightsUseCase`: Flow 기반 인사이트 목록 조회
- `AiInsight` 도메인 모델, `AiInsightRepository` 인터페이스

**Data 레이어**
- `AiInsightRepositoryImpl` (AiInsightDao 연결)
- `EntityMapper`: AiInsight ↔ AiInsightEntity 양방향 매핑
- `RepositoryModule`: AiInsightRepository 바인딩 추가

**AI 인사이트 화면** (`insight/AiInsightScreen`)
- InsightCard: 유형별 아이콘/색상 (주황/초록/파랑/빨강/보라)
- 읽지 않은 항목 강조 (컬러 테두리 + 파란 점 배지)
- '분석하기' 버튼 (로딩 인디케이터 포함)
- 빈 상태: 고양이 THINKING 표정

**Navigation**
- MainScreen 5번째 탭: "AI" (AutoAwesome 아이콘)
- 탭 순서: 홈 | 거래 | AI | 통계 | 설정

---

### Week 5 (2026-05-17) — 온보딩 + 단위 테스트

**온보딩 화면** (`onboarding/OnboardingScreen`)
- 3단계 슬라이드: 이름 → 월 소득 → 재무 목표
- AnimatedContent 페이지 전환 (슬라이드 인/아웃)
- 프리셋 FilterChip 4종 (비상금/여행/내 집 마련/노후) + 직접 입력
- 단계별 canProceed 검증 (이름 필수, 소득 양수 필수)
- OnboardingViewModel: UserProfile.onboardingCompleted = true로 upsert

**앱 진입 흐름 개선**
- `StartupViewModel`: UserProfileRepository.get() → onboardingCompleted 감시
- `MainActivity`: 인증 후 온보딩 완료 여부에 따라 "onboarding" 또는 "main" 분기
- 최초 실행 → 인증 → 온보딩(3단계) → 메인
- 재실행 → 인증 → 메인

**단위 테스트** (`domain/src/test`, 18개 테스트, 100% 통과)
- `ResolveCatExpressionUseCaseTest` (8개): ALERT/SLEEPING/HAPPY/DEFAULT 경계조건 검증
- `GetMonthlySummaryUseCaseTest` (4개): 수입/지출 집계, 예산 합산, 빈 데이터 처리
- `GenerateMonthlyInsightUseCaseTest` (6개): 인사이트 생성 조건, 타입 검증, 카테고리 최다 지출

---

### Week 6 (2026-05-17) — 홈 개선 + 프로필 편집 + 알림 자동 캡처

**HomeScreen 개선**
- `CatHeader`: `userName` 파라미터 추가 → "안녕하세요, {name}님!" 인사말 표시
- `InsightPreviewCard`: 미읽은 AI 인사이트 미리보기 카드 (클릭 시 AI 탭 이동)
- `HomeViewModel`: 5-flow combine (기존 3 + UserProfile + AiInsight 미읽은 목록 추가)
  - `uiState.userName`, `uiState.latestInsight` 필드 추가

**프로필 편집 화면** (`settings/ProfileEditScreen`)
- 이름 / 월 수입 / 재무 목표 편집 (프리셋 FilterChip 4종 + 직접 입력)
- `ProfileEditViewModel`: 기존 UserProfile load → 수정 → upsert (onboardingCompleted 유지)
- 저장 후 자동 뒤로 이동 (`isSaved` 플래그)

**알림 자동 캡처** (`notification/`)
- `MoneyNotificationListenerService` (`@AndroidEntryPoint`):
  - `NotificationRuleDao` + `AddTransactionUseCase` inject
  - `onListenerConnected()`: DB가 비어있으면 기본 규칙 시드
  - `onNotificationPosted()`: 패키지 → 규칙 조회 → 정규식 파싱 → EXPENSE 자동 등록
- `DefaultNotificationRules`: 8개 한국 카드사 규칙 (신한/삼성/KB/현대/롯데/하나/NH/우리)
- AndroidManifest: `BIND_NOTIFICATION_LISTENER_SERVICE` 서비스 선언

**SettingsScreen 업데이트**
- "자동 입력" 섹션: 알림 권한 상태 배지 (활성화 = primary 색 / 설정 필요 = error 색)
- "프로필 수정" 메뉴 항목 추가 (앱 정보 섹션)
- `SettingItem` composable: `badge`, `badgeColor` 파라미터 추가

**Navigation 완성 (MainScreen)**
- HomeScreen: `onNavigateToInsights` 연결
- SettingsScreen: `onNavigateToProfileEdit` 연결
- `profile_edit` 라우트 추가

## 빌드 상태

- 최종 빌드 성공: 2026-05-17 (assembleDebug, 164 tasks)
- 단위 테스트: 18/18 통과 (4.962s) — `./gradlew :domain:test`
- 주의: `opencsv` 미사용 — `CsvExporter`는 표준 Java I/O (`PrintWriter`)로 구현
