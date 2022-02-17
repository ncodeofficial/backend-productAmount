DROP TABLE product IF EXISTS ;

CREATE TABLE product(
    id INTEGER NOT NULL,
    name VARCHAR(255),
    price INTEGER,
    PRIMARY KEY (id)
);

DROP TABLE coupon IF EXISTS ;

CREATE TABLE coupon(
    id INTEGER NOT NULL,
    name VARCHAR(255),
    discount_type VARCHAR(15),
    discount_value INTEGER,
    PRIMARY KEY (id)
);


