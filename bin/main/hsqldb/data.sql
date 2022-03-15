INSERT INTO promotion VALUES(1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-02-01', '2022-03-01' );
INSERT INTO promotion VALUES(2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-02-01', '2022-03-01');

------------------------------- 테스트용 데이터 추가 ------------------------------------------------
INSERT INTO promotion VALUES(3, 'CODE', '10% 할인코드', 'PERCENT', 10, '2022-03-01', '2022-04-01');
INSERT INTO promotion VALUES(4, 'CODE', '40% 할인코드', 'PERCENT', 40, '2022-03-01', '2022-04-01');
INSERT INTO promotion VALUES(5, 'CODE', '99% 할인코드', 'PERCENT', 99, '2022-03-01', '2022-04-01');
INSERT INTO promotion VALUES(6, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-03-01', '2022-05-01' );
INSERT INTO promotion VALUES(7, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-03-01', '2022-05-01');
-----------------------------------------------------------------------------------------------

INSERT INTO product VALUES(1, '디코드상품', 215000);


INSERT INTO promotion_products VALUES(1, 1, 1);
INSERT INTO promotion_products VALUES(2, 2, 1);

------------- 테스트용 데이터 추가 -------------------
INSERT INTO promotion_products VALUES(3, 6, 1);
INSERT INTO promotion_products VALUES(4, 7, 1);
-----------------------------------------------