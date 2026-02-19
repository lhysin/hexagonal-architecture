# Hexagonal Architecture Guide Samples

## 문서 맵
- 아키텍쳐 정의: `docs/01-hexagonal-architecture-definition.md`
- 구현 가이드: `docs/02-implementation-guide-springboot4-java25.md`
- 시나리오 예제집(5개): `docs/03-scenario-examples-playbook.md`
- 샘플 코드: `src/main/java/com/example/hexagonal`

## 이 저장소에서 바로 보는 포인트
1. **의존성 규칙**: Adapter → Application → Domain
2. **다양한 사례**: 금융/커머스/콘텐츠/마케팅/카탈로그
3. **기술 조합**: Web + Data JDBC + Redis + Object Storage + EDA(Outbox)

## 포함된 예제 도메인
- 금융: 계좌 이체 + 일 한도 정책 + Outbox 이벤트
- 커머스: 주문 생성 + 재고 예약 + 주문 이벤트
- 콘텐츠: 상품 이미지 업로드(Object Storage)
- 마케팅: 쿠폰 발급(Redis TTL)
- 카탈로그: 상품 요약 조회 캐시(read-through)

