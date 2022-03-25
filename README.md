# backend-productAmount
백엔드 주니어 기술과제(상품가격 산정)

## Overview
- 할인(쿠폰 or 할인코드)이 적용된 상품 가격을 조회하는 API 작성
- Workflow
 
1. API Request 수신 (ProductId와 PromotionID 목록을 수신)
2. RequestBody(Param) 유효성검사
3. ProductId 로 product 조회
   1. 없는 Product 인 경우 핸들링 (EmptyResultDataAccessException 핸들링)
   2. 있는 Product 인 경우 continue

5. 할인 적용
   1. 쿠폰들을 하나씩 확인하며...
   2. 쿠폰아이디로 쿠폰 정보 조회
   3. product 적용하기
   4. 적용가능한 기간인지 확인하기
   4. ***쿠폰 적용 우선순위는....?
6. 최소 상품가격 10,000 이상인지
7. 1000원 단위 절삭하기
8. 리턴

---------

## TODO

- [x] ProductRepository 추상화
- [x] PromotionRepository 인터페이스 생성
- [ ] Service 클래스 테스트 작성하기
    - [x] 없는 Product 인 경우 핸들링
    - [x] ProductRequest Validation Test 하기
    - [x] product_promotion 에 등록되어있지 않은 경우 필터
        -  DB 쿼리로 해결
    - [x] 최소 상품가격 10,000 이상인지
- [ ] Controller 레이어 테스트
  - [ ] Exception -> HttpStatus 매핑 스펙 정하기
- [ ] Service 의 쿼리 Result 없을경우의 핸들링 (checked Exception vs ControllerAdivce 비교)
2. API RequestBody(Param) 유효성검사 (validation)
   - [ ] ProductInfoRequest 클래스 builder(생성자와) 객체 생성시 Validation
3. ProductId 로 product 조회
   - [x] 없는 Product 인 경우 핸들링
   - [x] 있는 Product 인 경우 continue
4. 쿠폰적용여부체크
    5. PromotionProducts 테이블에 Product가 등록되어잇는지 체크
       1. product-promotion ID 미연결시 오류
    6. 할인 적용 (Service)
       - [ ] enum으로 로직 구현...?
- [ ] 할인 적용 (Service)
   - [x] 쿠폰들을 하나씩 확인하며...
   - [x] 쿠폰아이디로 쿠폰 정보 조회
   - [x] product 적용하기
   - [x] 적용가능한 기간인지 확인하기
   - [x] 최소 상품가격 10,000 이상인지
   - [x] 1000원 단위 절삭하기
- [ ] 에러 핸들링
- [x] Request 도메인 Validation 
- [x] Response 도메인 Validation

Unresolved
- [ ] ***쿠폰 적용 우선순위는....?
 