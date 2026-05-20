# Contributing to MoneyCat

## 브랜치 전략

| 브랜치 | 용도 |
|--------|------|
| `main` | 배포 버전 (tag 기반 릴리즈) |
| `develop` | 개발 통합 브랜치 |
| `feature/*` | 새 기능 개발 |
| `bugfix/*` | 버그 수정 |
| `hotfix/*` | 운영 긴급 수정 |

```
feature/auth-biometric → develop → main
```

## 커밋 메시지 규칙 (Conventional Commits)

```
<type>(<scope>): <subject>

[body]
[footer]
```

| Type | 설명 |
|------|------|
| `feat` | 새로운 기능 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `refactor` | 리팩토링 |
| `test` | 테스트 추가/수정 |
| `chore` | 빌드 설정, 패키지 업데이트 |
| `perf` | 성능 개선 |

**예시:**
```
feat(auth): 생체인증 PIN 폴백 구현

BiometricPrompt 5회 실패 시 PIN 입력으로 전환
PinAuthManager에 PBKDF2 해싱 적용

Closes #12
```

## PR 가이드

1. `develop` 브랜치에서 `feature/<기능명>` 브랜치 생성
2. 구현 후 `./gradlew assembleDebug` 빌드 확인
3. 유닛 테스트 추가 (`./gradlew testDebugUnitTest`)
4. PR 템플릿 체크리스트 완료 후 PR 생성

## 아키텍처 규칙

```
app → domain, data, core-security, core-ui, core-common
data → domain, core-common
core-security → core-common
core-ui → core-common, domain
core-common → (독립)
domain → (독립, 순수 Kotlin)
```

- **절대로 `domain` 모듈에서 Android API를 import하지 마세요**
- **`kapt` 대신 `KSP`를 사용하세요**
- 금융 데이터 처리 코드는 반드시 `BigDecimal`을 사용하세요 (Float/Double 금지)
