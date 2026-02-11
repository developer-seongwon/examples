# Problem Detail Specification (RFC 7807)

## 1. Overview

본 문서는 프로젝트의 **HTTP 에러 응답 형식**을 정의한다.
RFC 7807 (Problem Details for HTTP APIs) 표준을 기반으로 하되,
`code`와 `timestamp` 확장 필드를 추가하여 사용한다.

---

## 2. Content-Type

```
application/problem+json
```

---

## 3. Response Format

```json
{
  "type": "https://api.example.com/problems/member-not-found",
  "title": "Member not found",
  "status": 404,
  "detail": "회원을 찾을 수 없습니다. id=99",
  "instance": "/api/members/99",
  "code": "EXP-404-01",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

---

## 4. Field Definition

### 4.1 Standard Fields (RFC 7807)

| Field    | Type   | Required | Description                              |
|----------|--------|----------|------------------------------------------|
| type     | string | O        | 에러 가이드 페이지 URI                    |
| title    | string | O        | 에러 코드에 정의된 짧은 제목             |
| status   | int    | O        | HTTP 상태 코드                            |
| detail   | string | O        | 사람이 읽을 수 있는 에러 상세 메시지      |
| instance | string | O        | 에러가 발생한 요청 URI                    |

### 4.2 Extension Fields

| Field     | Type   | Required | Description                              |
|-----------|--------|----------|------------------------------------------|
| code      | string | O        | 서버별 고유 에러 코드                     |
| timestamp | string | O        | 에러 발생 시각 (ISO 8601)                 |

---

## 5. Error Code (`code`)

### 5.1 Format

```
{SERVER_NAME(3)}-{HTTP_STATUS(3)}-{SEQ(2)}
```

| Segment     | Length | Description                        | Example |
|-------------|--------|------------------------------------|---------|
| SERVER_NAME | 3      | `application.yml`의 `app.name`     | EXP     |
| HTTP_STATUS | 3      | HTTP 상태 코드                      | 404     |
| SEQ         | 2      | 동일 상태 코드 내 순번              | 01      |

### 5.2 Code Registry

| Code       | ErrorCode         | Status | Title                 |
|------------|-------------------|--------|-----------------------|
| EXP-400-01 | INVALID_PARAMETER | 400    | Invalid parameter     |
| EXP-400-02 | INVALID_EMAIL     | 400    | Invalid email         |
| EXP-400-03 | INVALID_AGE       | 400    | Invalid age           |
| EXP-404-01 | MEMBER_NOT_FOUND  | 404    | Member not found      |
| EXP-409-01 | DUPLICATE_EMAIL   | 409    | Duplicate email       |
| EXP-500-01 | INTERNAL_ERROR    | 500    | Internal server error |

> `SERVER_NAME`은 런타임에 `app.name` 프로퍼티에서 주입받아 조합한다.

---

## 6. Type URI (`type`)

### 6.1 용도

`type` 필드는 **에러 해결 가이드 페이지**의 URL로 사용한다.
클라이언트나 운영자가 해당 URL로 접근하면 에러의 원인과 해결 방법을 확인할 수 있다.

### 6.2 생성 규칙

```
{problems-base-url}/{error-code-name(kebab-case)}
```

| Example                                                  | ErrorCode        |
|----------------------------------------------------------|------------------|
| `https://api.example.com/problems/invalid-parameter`     | INVALID_PARAMETER|
| `https://api.example.com/problems/member-not-found`      | MEMBER_NOT_FOUND |
| `https://api.example.com/problems/duplicate-email`       | DUPLICATE_EMAIL  |
| `https://api.example.com/problems/internal-error`        | INTERNAL_ERROR   |

> `problems-base-url`은 `application.yml`의 `app.problems-base-url` 프로퍼티에서 주입받는다.

---

## 7. Error Handling Layers

본 프로젝트는 **두 레이어**에서 에러를 처리한다.
두 레이어 모두 동일한 ProblemDetail 형식으로 응답한다.

### 7.1 ExceptionFilter (Servlet Filter)

```
요청 → TransactionFilter → LoggingFilter → ExceptionFilter → DispatcherServlet
                                                ↑
                                        필터 체인 내 예외를
                                        여기서 catch 후
                                        ProblemDetail 응답
```

* Spring MVC **밖**에서 발생한 예외 처리
* `response`에 직접 JSON 문자열을 작성
* `ObjectMapper` 없이 문자열 직접 조합
* `BusinessException`과 일반 `Exception`을 분리 처리

### 7.2 GlobalExceptionHandler (@RestControllerAdvice)

```
DispatcherServlet → Controller → Service
                        ↑
                  Spring MVC 안에서
                  발생한 예외를 처리
```

* Spring MVC **안**에서 발생한 예외 처리
* `@ExceptionHandler`로 예외 타입별 분기
* `ResponseEntity`로 ProblemDetail JSON 반환
* `BusinessException`, `IllegalArgumentException`, 일반 `Exception` 분리 처리

---

## 8. ErrorCode Enum

`ErrorCode`는 도메인 모듈에 정의되며, 모든 에러의 메타데이터를 관리한다.

```kotlin
enum class ErrorCode(
    val status: Int,    // HTTP 상태 코드
    val seq: String,    // 동일 상태 코드 내 순번
    val title: String,  // 짧은 제목
    val detail: String  // 기본 상세 메시지
)
```

`toCode(prefix)` 메서드로 전체 코드를 생성한다:

```kotlin
ErrorCode.MEMBER_NOT_FOUND.toCode("EXP")  // → "EXP-404-01"
```

---

## 9. Examples

### 404 Member Not Found

```http
HTTP/1.1 404 Not Found
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/member-not-found",
  "title": "Member not found",
  "status": 404,
  "detail": "회원을 찾을 수 없습니다. id=99",
  "instance": "/api/members/99",
  "code": "EXP-404-01",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

### 409 Duplicate Email

```http
HTTP/1.1 409 Conflict
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/duplicate-email",
  "title": "Duplicate email",
  "status": 409,
  "detail": "이미 존재하는 이메일입니다. email=test@test.com",
  "instance": "/api/members",
  "code": "EXP-409-01",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

### 400 Invalid Parameter

```http
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/invalid-parameter",
  "title": "Invalid parameter",
  "status": 400,
  "detail": "이메일은 비어있을 수 없습니다.",
  "instance": "/api/members",
  "code": "EXP-400-01",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

### 400 Invalid Email

```http
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/invalid-email",
  "title": "Invalid email",
  "status": 400,
  "detail": "이메일 형식이 올바르지 않습니다.",
  "instance": "/api/members",
  "code": "EXP-400-02",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

### 500 Internal Server Error (Filter Level)

```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/internal-error",
  "title": "Internal server error",
  "status": 500,
  "detail": "Unexpected error",
  "instance": "/api/members",
  "code": "EXP-500-01",
  "timestamp": "2026-02-10T14:23:15.873Z"
}
```

---

## 10. Design Decisions

1. **RFC 7807 기반 확장** — 표준 형식을 따르되 `code`, `timestamp` 확장 필드 추가
2. **두 레이어 분리** — Filter 예외와 MVC 예외를 각각 처리하되 동일 형식 유지
3. **ObjectMapper 미사용** (Filter) — 필터에서 직접 문자열 조합하여 의존성 최소화
4. **`type`은 가이드 페이지 URI** — 에러 해결 방법을 안내하는 문서 페이지 링크
5. **`code`로 세분화된 에러 식별** — 동일 HTTP 상태라도 `code`로 정확한 에러 구분 가능
6. **`ErrorCode` enum 중앙 관리** — 도메인 모듈에서 모든 에러 코드를 일원화 관리
7. **detail에 한글 메시지** — 도메인 예외 메시지를 그대로 전달하여 디버깅 용이
