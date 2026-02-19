# 헥사고날 아키텍처 시나리오 예제 5선

이 문서는 실무에서 자주 마주치는 설계 난이도별로 5가지 케이스를 제공합니다.
각 케이스는 다음을 포함합니다.

- 목표
- 도메인/BC 구성
- 절대 규칙 적용 포인트
- 유연 규칙 적용 포인트
- 포트/어댑터 샘플 구조
- 호출 흐름

---

## 케이스 1) 절대 규칙을 엄격히 지킨 케이스

### 상황
**결제 승인 API** (단일 BC: `payment`)

### 목표
- 프레임워크 비의존 도메인
- 포트 기반 입출력 완전 분리
- 컨트롤러는 UseCase만 호출

### BC 구성
- `payment`

### 절대 규칙 적용
- `Payment` 도메인 모델은 순수 자바(스프링 어노테이션 없음)
- `ApprovePaymentService`는 `LoadPaymentPort`, `SavePaymentPort`, `PublishDomainEventPort`만 의존
- DB/JMS/Kafka 구현은 모두 adapter.out

### 유연 규칙 적용
- 없음(의도적으로 최소화)

### 패키지 예시
```text
payment
├─ application
│  ├─ domain/model/Payment.java
│  ├─ port/in/ApprovePaymentUseCase.java
│  ├─ port/out/LoadPaymentPort.java
│  ├─ port/out/SavePaymentPort.java
│  └─ service/ApprovePaymentService.java
└─ adapter
   ├─ in/web/PaymentController.java
   └─ out/persistence/PaymentJdbcAdapter.java
```

### 흐름
1. `PaymentController` → `ApprovePaymentUseCase`
2. `ApprovePaymentService`에서 도메인 검증/상태 전이
3. `SavePaymentPort` 저장
4. `PublishDomainEventPort` 이벤트 기록

### 실제 코드 매핑 참고
- Inbound Adapter 패턴: `src/main/java/com/example/hexagonal/adapter/in/web/TransferController.java`
- Application Service + Port 사용: `src/main/java/com/example/hexagonal/application/service/TransferMoneyService.java`

### 안티패턴(피해야 할 것)
- `PaymentController`가 `JdbcClient`를 직접 호출해 결제를 저장

---

## 케이스 2) 절대 규칙 + 유연 규칙이 적당히 섞인 케이스

### 상황
**상품 상세 조회 API** (단일 BC: `catalog`)

### 목표
- 핵심 규칙은 유지
- 조회 성능/개발 생산성을 위해 일부 유연 규칙 허용

### BC 구성
- `catalog`

### 절대 규칙 적용
- UseCase 인터페이스 유지: `GetProductDetailUseCase`
- Application은 `LoadProductDetailPort`, `CachePort`만 참조
- Redis/JDBC는 adapter.out에서 처리

### 유연 규칙 적용
- Query 객체 생략(파라미터 1개 `productId`)
- 응답 DTO 생략 가능(도메인 모델 또는 View record 직접 반환)
- 캐시 miss fallback 경로 허용

### 패키지 예시
```text
catalog
├─ application
│  ├─ port/in/GetProductDetailUseCase.java
│  ├─ port/out/LoadProductDetailPort.java
│  ├─ port/out/CachePort.java
│  └─ service/GetProductDetailService.java
└─ adapter
   ├─ in/web/ProductQueryController.java
   ├─ out/persistence/ProductReadJdbcAdapter.java
   └─ out/cache/RedisCacheAdapter.java
```

### 흐름
1. 캐시 조회
2. miss 시 DB 조회
3. 캐시 적재 후 반환

### 실제 코드 매핑 참고
- 조회 서비스의 캐시 우선 처리: `src/main/java/com/example/hexagonal/application/service/GetProductSummaryService.java`
- Redis 어댑터 구현: `src/main/java/com/example/hexagonal/adapter/out/cache/RedisCacheAdapter.java`

### 안티패턴(피해야 할 것)
- Application Service가 `StringRedisTemplate`을 직접 의존

---

## 케이스 3) 도메인 3개 이상 + BC 경계 호출

### 상황
**주문 생성 API**
- BC 1: `order`
- BC 2: `inventory`
- BC 3: `coupon`

### 목표
- BC 간 직접 도메인 참조 금지
- 상대 BC의 `port.in`만 호출

### 절대 규칙 적용
- `order`는 `inventory`의 `ReserveStockUseCase`, `coupon`의 `UseCouponUseCase`에만 의존
- `order`가 `inventory.domain`/`coupon.adapter` 참조 금지
- 트랜잭션 경계는 각 BC 내부에서 관리

### 유연 규칙 적용
- 주문 성공 후 알림 발송은 비동기 이벤트로 지연 처리
- 일부 조회는 read model DTO로 단순화

### BC 경계 호출 구조
```text
order.application.service.PlaceOrderService
  ├─ calls inventory.port.in.ReserveStockUseCase
  └─ calls coupon.port.in.UseCouponUseCase
```

### 흐름
1. 주문 생성 요청
2. 재고 예약(Inventory BC)
3. 쿠폰 사용(Coupon BC)
4. 주문 확정 후 outbox 이벤트 저장

### 실제 코드 매핑 참고
- 주문 오케스트레이션 예시: `src/main/java/com/example/hexagonal/application/service/PlaceOrderService.java`
- 주문 저장/재고 차감 어댑터: `src/main/java/com/example/hexagonal/adapter/out/persistence/order/OrderJdbcAdapter.java`

### 안티패턴(피해야 할 것)
- `order` BC가 `inventory.adapter` 패키지를 직접 참조

---

## 케이스 4) 절대 규칙 + 유연 규칙이 많이 섞인 케이스

### 상황
**마이페이지 집계 API** (`account` BC 내부 + 다수 read adapter)

### 목표
- 쓰기 모델은 엄격 규칙 유지
- 읽기 모델은 빠른 화면 응답을 위해 유연하게 최적화

### 절대 규칙 적용
- 쓰기 유스케이스는 포트 중심(`UpdateProfileUseCase`, `ChangePasswordUseCase`)
- 외부 연동은 OutPort로만 사용

### 유연 규칙 적용(많음)
- 조회 전용 서비스에서 Query 객체 일부 생략
- API별 View DTO 적극 사용
- 캐시 우선 + 검색엔진 fallback
- 동일 BC 내 read adapter 다중화(JDBC + Redis + Search)

### 패키지 예시
```text
account
├─ application
│  ├─ port/in/*UseCase
│  ├─ port/out/*Port
│  ├─ service/write/*Service
│  └─ service/read/*QueryService
└─ adapter/out
   ├─ persistence/
   ├─ cache/
   └─ search/
```

### 흐름
- 프로필 수정: 엄격한 도메인 규칙/트랜잭션
- 마이페이지 조회: read model 조합 + 캐시 전략

### 운영 실패/보상 전략
- 검색 인덱스 장애 시: DB 조회로 fallback, 검색 어댑터는 비활성화 플래그 적용
- 캐시 장애 시: cache bypass + 짧은 TTL 재적용

### 안티패턴(피해야 할 것)
- 조회 최적화를 이유로 도메인 규칙이 필요한 쓰기 로직까지 QueryService에 혼합

---

## 케이스 5) 도메인 5개 이상 + BC 경계 호출 + 다양한 절대/유연 규칙

### 상황
**이커머스 주문 종합 프로세스**
- `order`, `inventory`, `coupon`, `payment`, `shipping`, `notification` (6개 BC)

### 목표
- BC 경계 준수(Port In 호출)
- 장애 내성(Outbox/Saga)
- 운영 유연성(캐시/비동기/조회모델)

### 절대 규칙 적용
- BC 간 참조는 `port.in`만 허용
- 각 BC 도메인은 프레임워크 비의존
- OutPort로 외부 기술 캡슐화
- 핵심 상태 변경은 application service 트랜잭션 경계

### 유연 규칙 적용
- 알림/추천은 eventual consistency 허용
- read API는 Query DTO/Response DTO 적극 사용
- 호출 부담 큰 외부 API는 cache-aside + circuit breaker
- 주문 생성 후 후속 단계는 saga orchestrator로 분산 처리

### BC 경계 호출 예시
```text
order.PlaceOrderService
  ├─ inventory.ReserveStockUseCase
  ├─ coupon.ApplyCouponUseCase
  ├─ payment.AuthorizePaymentUseCase
  ├─ shipping.CreateShipmentUseCase
  └─ notification.SendOrderPlacedUseCase (비동기)
```

### 권장 운영 패턴
- **동기**: 재고/결제 승인(핵심 성공조건)
- **비동기**: 알림/추천/통계
- **복구**: 결제 성공 후 배송 실패 시 보상 트랜잭션

### 실패/복구 표준(권장)
| 항목 | 권장 정책 |
|---|---|
| 타임아웃 | BC 간 동기 호출은 300~800ms + 지수 백오프 재시도 |
| 멱등성 | `requestId/orderId` 기반 멱등 키 강제 |
| 보상 트랜잭션 | 결제 승인 후 배송 실패 시 결제 취소 이벤트 발행 |
| 중복 이벤트 | 소비자에서 idempotent consumer 적용 |

### 안티패턴(피해야 할 것)
- 핵심 경계 호출을 임의로 이벤트 비동기로 바꿔 주문 성공 조건을 깨뜨리는 설계

---

## 한눈에 비교표

| 케이스 | BC 수 | 절대 규칙 강도 | 유연 규칙 강도 | 권장 패턴 |
|---|---:|---|---|---|
| 1 | 1 | 매우 높음 | 매우 낮음 | 순수 포트/어댑터 |
| 2 | 1 | 높음 | 중간 | 캐시+조회 간소화 |
| 3 | 3 | 높음 | 중간 | BC `port.in` 경계 호출 |
| 4 | 1~2 | 높음 | 높음 | CQRS/조회 최적화 |
| 5 | 6+ | 매우 높음 | 매우 높음 | Saga + Outbox + 다중 어댑터 |
