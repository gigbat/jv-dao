CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE internet_shop.manufacturers
(
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,
    country VARCHAR(45) NOT NULL,
    deleted TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;