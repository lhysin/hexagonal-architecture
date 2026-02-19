# 헥사고날 아키텍쳐 정의 문서 (모든 API 공통 표준)

## 0) 적용 범위
이 문서는 **내부/외부 구분 없이 모든 API**(내부 연동 API, 외부 공개 API, 배치성 API, 이벤트 기반 API)에 공통 적용합니다.

---

## 1) 빠른 요약 (3줄)
- 의존성은 항상 **바깥 → 안쪽**으로만 향합니다.
- 애플리케이션은 **Port(인터페이스)**로 외부 의존성을 추상화합니다.
- 외부 기술(Web/DB/Redis/Object Storage/Messaging)은 **Adapter**에서만 다룹니다.

---

## 2) 핵심 정의

### 2.1 헥사고날 아키텍쳐란?
헥사고날 아키텍쳐(Hexagonal Architecture, Ports and Adapters)는 애플리케이션 핵심을 중심에 두고, 외부 세계와의 연결을 **포트(추상 인터페이스)**로 정의한 뒤, 외부 구현을 **어댑터**로 교체 가능하게 둡니다.

- 중심: 도메인 모델 + 유스케이스(애플리케이션 서비스)
- 바깥: 웹/DB/메시지 브로커/Object Storage/Redis/외부 API
- 연결: Inbound Port(입력), Outbound Port(출력)

### 2.2 클린 아키텍쳐와의 관계
클린 아키텍쳐의 의존성 규칙(안쪽으로만 향함)을 헥사고날이 구현 디테일(포트/어댑터 패턴)로 더 명확히 만든다고 볼 수 있습니다.

---

## 3) 의존성 규칙

### 3.1 절대 규칙 (반드시 지켜야 함)
1. **도메인(`domain`)은 프레임워크에 의존하지 않는다.**
   - `@Entity`, `@Table`, `@Component`, `RestTemplate` 등 금지.
2. **애플리케이션(`application`)은 어댑터 구현체를 모른다.**
   - 오직 `application.port.out` 인터페이스에만 의존.
3. **어댑터(`adapter`)는 도메인/애플리케이션을 참조할 수 있으나, 반대는 금지.**
4. **Inbound Adapter는 Inbound Port만 호출한다.**
   - 컨트롤러가 JDBC/Redis/S3를 직접 호출하면 규칙 위반.
5. **Outbound Adapter만 외부 리소스를 직접 다룬다.**
   - SQL, RedisTemplate, S3Client, KafkaProducer 등.
6. **트랜잭션 경계는 애플리케이션 서비스에서 관리한다.**
   - 분산 트랜잭션 대신 Outbox/Saga 같은 패턴 사용.

### 3.2 유연 규칙 (상황에 따라 허용)
1. 단순 조회는 Query Service를 application에 둘 수 있음.
2. 작은 팀/초기 제품은 유스케이스당 파일 수를 줄여도 됨.
3. 도메인 이벤트는 즉시 발행 대신 Outbox 지연 발행 허용.
4. 캐시는 Port 뒤에 숨기되, 성능 요구 시 캐시 우회 경로 허용.
5. Object Storage URL 정책은 application에서 표현 가능(단 SDK 호출은 adapter 유지).

### 3.3 절대 규칙 vs 유연 규칙 한눈에
| 구분 | 예시 | 판단 기준 |
|---|---|---|
| 절대 규칙 | 도메인에 스프링 의존 금지 | 아키텍처 무결성 |
| 절대 규칙 | Adapter 구현체를 Application이 직접 참조 금지 | 테스트/교체 용이성 |
| 유연 규칙 | 단순 조회에서 Query 객체 생략 | 보일러플레이트 최소화 |
| 유연 규칙 | Outbox 지연 발행 | 운영 안정성/재시도 |

---

## 4) 패키지 구조 가이드

```text
com.example.hexagonal
├─ domain
│  ├─ model
│  └─ service
├─ application
│  ├─ port
│  │  ├─ in
│  │  └─ out
│  └─ service
├─ adapter
│  ├─ in
│  │  └─ web
│  └─ out
│     ├─ persistence
│     ├─ messaging
│     ├─ cache
│     └─ storage
└─ config
```

### 4.1 명명 규칙
- Inbound Port: `~UseCase` (예: `TransferMoneyUseCase`)
- Outbound Port: `~Port` (예: `LoadAccountPort`, `ObjectStoragePort`)
- Application Service: `~Service` (예: `TransferMoneyService`)
- Inbound Adapter: `~Controller`, `~Consumer`
- Outbound Adapter: `~JdbcAdapter`, `~RedisAdapter`, `~S3Adapter`, `~PublisherAdapter`
- Domain Event: 과거형 + `Event` (`MoneyTransferredEvent`)

### 4.2 계층별 책임
- `domain.model`: 엔티티/VO/애그리거트 규칙
- `domain.service`: 도메인 정책(순수 규칙)
- `application.service`: 유스케이스 orchestration, 트랜잭션, 포트 호출
- `adapter.in`: 프로토콜 변환(HTTP, 메시지)
- `adapter.out`: 기술 상세 구현

---

## 5) 참조 흐름 (Web + Data JDBC + Redis + Object Storage + EDA)
1. 사용자가 `/api/transfers` 호출
2. `TransferController`가 `TransferMoneyUseCase` 호출
3. `TransferMoneyService`가 계좌 조회/검증/갱신
4. `OutboxEventPublisherAdapter`가 outbox 테이블에 이벤트 저장
5. 별도 배치/폴러/CDC가 브로커로 발행 (EDA)
6. 상품 이미지 업로드 시 `ObjectStoragePort` 통해 저장
7. 쿠폰 발급 시 `CachePort` 통해 Redis TTL 저장

---

## 6) 안티패턴
- Controller에서 SQL 직접 실행
- Domain 객체에 `RedisTemplate` 필드 주입
- Application이 `S3Client`, `KafkaTemplate` 직접 호출
- Outbound Port 없이 Repository/SDK 직접 참조
- 이벤트 발행 실패를 이유로 핵심 트랜잭션 전체 롤백(무분별)

---

## 7) 체크리스트
- [ ] 도메인에 스프링 어노테이션이 없는가?
- [ ] 모든 외부 연동이 `application.port.out` 뒤에 있는가?
- [ ] 컨트롤러가 유스케이스 포트만 참조하는가?
- [ ] 트랜잭션 경계가 application service에 있는가?
- [ ] EDA는 Outbox 등 재시도 가능한 구조인가?
- [ ] Redis/Object Storage 교체 시 application/domain 변경이 최소인가?

---

## 8) 도메인별 포트 설계 템플릿

### 8.1 금융(송금)
- In Port: `TransferMoneyUseCase`
- Out Port: `LoadAccountPort`, `UpdateAccountStatePort`, `PublishDomainEventPort`
- 핵심 규칙: 한도/잔액 검증, 감사 이벤트 적재

### 8.2 커머스(주문/재고)
- In Port: `PlaceOrderUseCase`
- Out Port: `SaveOrderPort`, `ReserveStockPort`, `PublishDomainEventPort`
- 핵심 규칙: 재고 선점 실패 시 주문 실패, 성공 시 이벤트 발행

### 8.3 콘텐츠(이미지/파일)
- In Port: `UploadProductImageUseCase`(또는 서비스)
- Out Port: `ObjectStoragePort`
- 핵심 규칙: 버킷/경로 정책은 application, SDK 호출은 adapter

### 8.4 마케팅(쿠폰/프로모션)
- In Port: `IssueCouponUseCase`
- Out Port: `CachePort`
- 핵심 규칙: TTL 정책은 유스케이스가 결정, Redis 구현은 adapter

### 8.5 카탈로그(조회 최적화)
- In Port: `GetProductSummaryUseCase`
- Out Port: `LoadProductSummaryPort`, `CachePort`
- 핵심 규칙: read-model DTO 허용(유연 규칙), miss 시 DB fallback
