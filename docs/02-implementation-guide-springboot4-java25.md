# 실제 코드 구현 문서 (Spring Boot 4.0.1 + Java 25 + Gradle + Web + Data JDBC)

## 1) 먼저 읽기 (권장 순서)
1. `docs/01-hexagonal-architecture-definition.md`에서 규칙 확인
2. 본 문서의 예제 흐름 확인
3. `src/main/java/com/example/hexagonal` 코드와 1:1 매핑

---

## 2) 프로젝트 설정

### 2.1 Gradle 핵심
- Spring Boot 4.0.1
- Java Toolchain 25
- 의존성: web, data-jdbc, validation, data-redis

> 구현 파일: `build.gradle.kts`, `settings.gradle.kts`

---

## 3) 도메인 예제 A: 계좌 이체 (핵심 도메인)

### 3.1 도메인 모델
- `Account`: 출금/입금 규칙
- `TransferPolicy`: 일 이체 한도 정책

### 3.2 포트 구성
- In: `TransferMoneyUseCase`
- Out: `LoadAccountPort`, `UpdateAccountStatePort`, `PublishDomainEventPort`

### 3.3 처리 순서
1. 출금/입금 계좌 조회
2. 정책 검증 (`TransferPolicy`)
3. 도메인 연산 수행
4. 상태 저장
5. 도메인 이벤트 기록(Outbox)

---

## 4) 도메인 예제 B: 주문 생성 + 재고 예약 (커머스 트랜잭션)

### 4.1 시나리오
- 고객 주문 생성
- 상품 재고 예약/차감
- 주문 이벤트 발행

### 4.2 포트 구성
- In: `PlaceOrderUseCase`
- Out: `SaveOrderPort`, `ReserveStockPort`, `PublishDomainEventPort`

### 4.3 구현 포인트
- `PlaceOrderService`가 도메인 생성/상태 전이/저장/이벤트까지 오케스트레이션
- `OrderJdbcAdapter`가 주문 저장 + 재고 차감을 캡슐화

---

## 5) 도메인 예제 C: 상품 이미지 업로드 (Object Storage)

### 5.1 규칙
- 유스케이스는 `ObjectStoragePort`에만 의존
- 실제 SDK 호출은 `S3ObjectStorageAdapter`에 위치

### 5.2 장점
- 스토리지 교체(AWS S3 ↔ MinIO ↔ GCS) 용이
- 테스트에서 Fake Adapter 대체 쉬움

---

## 6) 도메인 예제 D: Redis 기반 처리

### 6.1 쿠폰 발급
- `IssueCouponService`는 `CachePort`만 사용
- TTL(24시간)은 application 정책에서 결정

### 6.2 상품 요약 조회 캐시(read-through)
- hit: Redis 즉시 반환
- miss: `LoadProductSummaryPort`로 DB 조회 후 캐시 적재

### 6.3 운영 팁
- cache miss를 기본 시나리오로 설계
- 키 네이밍: `도메인:용도:식별자`

---

## 7) 도메인 예제 E: EDA + Outbox

### 7.1 왜 Outbox?
DB 저장 + 브로커 발행을 강결합하면 장애 전파가 커집니다.

### 7.2 권장 패턴
1. 비즈니스 트랜잭션 내 `outbox` 적재
2. 별도 프로세스가 `published=false` 이벤트 polling
3. 발행 성공 시 `published=true` 업데이트

### 7.3 기대 효과
- 최소 1회(at-least-once) 발행
- 재시도 가능
- 핵심 도메인 트랜잭션 보호

---

## 8) 유연 규칙 검증 케이스

| 케이스 | 내용 | 목적 |
|---|---|---|
| A | `TransferMoneyService`를 `TransferMoneyUseCase`로 바로 노출 | 초기 생산성 |
| B | 복잡 조회 시 Query DTO(`ProductSummaryView`) 사용 | 조회 최적화 |
| C | Cache 실패 시 DB fallback | 장애 내성 |
| D | Outbox vs 직접 발행 전략 선택 | 운영 유연성 |

---

## 9) 샘플 코드 인덱스 (찾기 쉽게 정리)

### 9.1 도메인
- `domain/model/Account.java`
- `domain/model/order/Order.java`
- `domain/service/TransferPolicy.java`

### 9.2 포트
- `application/port/in/TransferMoneyUseCase.java`
- `application/port/in/PlaceOrderUseCase.java`
- `application/port/in/GetProductSummaryUseCase.java`
- `application/port/out/*Port.java`

### 9.3 서비스
- `application/service/TransferMoneyService.java`
- `application/service/PlaceOrderService.java`
- `application/service/GetProductSummaryService.java`
- `application/service/IssueCouponService.java`
- `application/service/UploadProductImageService.java`

### 9.4 인/아웃바운드 어댑터
- `adapter/in/web/TransferController.java`
- `adapter/in/web/OrderController.java`
- `adapter/in/web/ProductQueryController.java`
- `adapter/out/persistence/account/AccountJdbcAdapter.java`
- `adapter/out/persistence/order/OrderJdbcAdapter.java`
- `adapter/out/persistence/product/ProductSummaryJdbcAdapter.java`
- `adapter/out/messaging/OutboxEventPublisherAdapter.java`
- `adapter/out/storage/S3ObjectStorageAdapter.java`
- `adapter/out/cache/RedisCacheAdapter.java`

---

## 10) 운영 관점 전체 검토 요약
- 구조 일관성: 포트/어댑터 경계가 명확함
- 확장성: Redis/Object Storage/EDA를 포트 기반으로 캡슐화
- 유지보수성: 기능별 서비스와 포트가 분리되어 영향 범위 제한
- 권장 보완: ArchUnit 의존성 테스트, Testcontainers 통합 테스트, outbox relay 운영 자동화

---

## 11) 시작 순서 (실무 온보딩)
1. 도메인 정책/용어 정리 (유비쿼터스 언어)
2. 유스케이스별 In/Out 포트 정의
3. application service 구현
4. web adapter 구현
5. JDBC adapter 구현
6. Redis/Object Storage/Messaging adapter 구현
7. 통합 테스트(포트 목/테스트컨테이너)
8. 관측성(로그/트레이싱/메트릭) 추가

---

## 12) 확장 제안
- 멀티 모듈 분리: `domain`, `application`, `adapter-*`, `bootstrap`
- 아키텍처 테스트: ArchUnit으로 의존성 규칙 자동 검증
- EDA 고도화: CDC 기반 outbox relay
- Object Storage 보안: Presigned URL 발급 유스케이스 분리
- Redis 고도화: 분산락/레이트리밋 포트 추가


---

## 13) 상황별 설계 예제 (5개 시나리오)
- `docs/03-scenario-examples-playbook.md` 문서에서 다음 5개를 제공합니다.
  1. 절대 규칙을 엄격히 지킨 케이스
  2. 절대 규칙 + 유연 규칙이 적당히 섞인 케이스
  3. 도메인 3개 이상 + BC 경계 호출 케이스
  4. 절대 규칙 + 유연 규칙이 많이 섞인 케이스
  5. 도메인 5개 이상 + BC 경계 호출 + 다양한 규칙 케이스
