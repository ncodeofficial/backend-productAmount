INSERT INTO promotion VALUES(1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-02-01', '2022-04-01' );
INSERT INTO promotion VALUES(2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-02-01', '2022-04-01');
INSERT INTO promotion VALUES(3, 'CODE', '유효기간아닌쿠폰1', 'PERCENT', 15, '2022-04-01', '2022-05-01');
INSERT INTO promotion VALUES(4, 'CODE', '유효기간아닌쿠폰2', 'PERCENT', 15, '2022-02-01', '2022-03-01');

INSERT INTO product VALUES(1, '디코드상품', 215000);
INSERT INTO product VALUES(2, '만원상품', 10000);
INSERT INTO product VALUES(3, '쿠폰사용불가상품', 10000);


INSERT INTO promotion_products VALUES(1, 1, 1);
INSERT INTO promotion_products VALUES(2, 2, 1);
INSERT INTO promotion_products VALUES(3, 3, 1);
INSERT INTO promotion_products VALUES(4, 4, 1);
INSERT INTO promotion_products VALUES(5, 1, 2);
INSERT INTO promotion_products VALUES(6, 2, 2);
INSERT INTO promotion_products VALUES(7, 3, 2);
INSERT INTO promotion_products VALUES(8, 4, 2);
