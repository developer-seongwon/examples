# Custom Time-Ordered ID Specification

## 1. Overview

본 문서는 멀티 서버 환경에서 **시간 정렬 가능하고 충돌이 거의 없는 커스텀 ID (UUID 대체)** 생성 규칙을 정의한다.

이 ID는 다음 목표를 가진다:

* 시간 순 정렬 가능
* 멀티 서버 충돌 방지
* 사람이 읽기 쉬움
* 운영 / 디버깅 친화
* UUID 대비 짧은 길이
* 서버 / 버전 추적 가능
* Core ID 불변성 유지

---

## 2. Core ID Format

```
yyyyMMdd-HHmm-ssSSS-SERVER(6)-SEQ(4)+RAND(4)
```

### Example

```
20260210-1423-15873-SEO101-0001A7K3
20260210-1423-15873-SEO101-0002F92Q
20260210-1423-15874-SEO101-0003K1Z8
```

---

## 3. Field Definition

| Field     | Length | Description       | Example  |
|-----------|--------|-------------------|----------|
| yyyyMMdd  | 8      | 날짜              | 20260210 |
| HHmm      | 4      | 시분              | 1423     |
| ssSSS     | 5      | 초 + 밀리초        | 15873    |
| SERVER(6) | 6      | 서버 식별자        | SEO101   |
| SEQ(4)    | 4      | Atomic 증가 시퀀스 | 0001     |
| RAND(4)   | 4      | Base36 랜덤       | A7K3     |

---

## 4. SERVER(6) Structure

```
[ServerName(3)][ServerNo(1)][Version(2)]
```

### Example

| Item        | Value      |
|-------------|------------|
| Server Name | SEO        |
| Server No   | 1          |
| Version     | 01         |
| Result      | **SEO101** |

### Version Source

* Gradle `project.version`
* Spring Boot `build-info`
* 자동 변환 규칙:

```
0.0.1-SNAPSHOT → 01
1.2.3 → 23
2.0.0 → 00
```

---

## 5. Extended Metadata (Outside Core ID)

Core ID는 **절대 변경되지 않는 불변 값**이다.
추가 실행 정보는 외부 메타로 관리한다.

### Format

```
[CoreID][API][SEQ]
```

### Example

```
[20260210-1423-15873-SEO101-0001A7K3][PAYMENT][03]
```

### Purpose

* 실행 컨텍스트 추적
* 병렬 처리 식별
* 로그 / 트레이싱
* 샤딩 / 워커 구분

> ⚠️ DB Primary Key에는 **Core ID만 사용**

---

## 6. Ordering Characteristics

* Core ID는 **시간 기준 정렬 가능**
* 동일 ms 내에서는 `SEQ`로 순서 보장
* 멀티 서버 환경에서도 충돌 없음

---

## 7. Collision Safety

ID 구성:

```
Timestamp(ms) + ServerID + AtomicSequence + Random
```

충돌 조건:

* 동일 ms
* 동일 서버
* 시퀀스 overflow
* 랜덤 동일

→ 실무적으로 **충돌 확률 ≈ 0**

---

## 8. Performance Characteristics

| Metric         | Result                      |
|----------------|-----------------------------|
| Max Throughput | ~10K–50K IDs/sec per server |
| Multi-Server   | Safe                        |
| Thread Safe    | Yes (Atomic)                |
| Length         | ~32 chars                   |
| Readable       | Yes                         |
| Debuggable     | Excellent                   |

---

## 9. Design Principles

1. Core ID는 불변
2. 시간 정렬 가능
3. 서버 식별 포함
4. 확장 메타 분리
5. 사람이 읽기 가능
6. UUID보다 운영 친화적
7. 멀티 서버 안전

---

## 10. Optional Extensions

필요 시 확장 가능:

* API ID 포함
* Worker / Node ID
* Clock Rollback Protection
* Snowflake-style Bit Packing
* UUID v7 호환
* Random 제거 (Deterministic)
* 전체 글로벌 시간 정렬
* ID 길이 압축
* DB Index 최적화

---

## 11. Recommended Usage

### Primary Key

* Use **Core ID only**

### Logging / Tracing

* Use `[CoreID][API][SEQ]`

### Distributed Systems

* Safe for multi-node / multi-thread

---

## 12. Example Full Usage

**Core ID**

```
20260210-1423-15873-SEO101-0001A7K3
```

**With Metadata**

```
[20260210-1423-15873-SEO101-0001A7K3][ORDER][02]
```
