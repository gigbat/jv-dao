-- Create DB
CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;
-- Create table manufacturers
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
-- Create table cars
CREATE TABLE `internet_shop`.`cars`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `manufacturer_id` BIGINT      NOT NULL,
    `model`       VARCHAR(45) NOT NULL,
    `deleted`     TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `manufacturers_cars_id_idx` (`manufacturer_id` ASC) VISIBLE,
    CONSTRAINT `manufacturers_cars_id`
        FOREIGN KEY (`manufacturer_id`)
            REFERENCES `internet_shop`.`manufacturers` (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
-- Create table drivers
CREATE TABLE `internet_shop`.`drivers`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(45) NOT NULL,
    `license_number` VARCHAR(45) NOT NULL,
    `deleted`        TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
-- Create table cars_drivers
CREATE TABLE `internet_shop`.`cars_drivers`
(
    `driver_id` BIGINT NOT NULL,
    `car_id`    BIGINT NOT NULL,
    INDEX `id_idx` (`driver_id` ASC) VISIBLE,
    INDEX `id_idx` (`car_id` ASC) VISIBLE,
    CONSTRAINT `id`
        FOREIGN KEY (`driver_id`)
            REFERENCES `internet_shop`.`drivers` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `id`
        FOREIGN KEY (`driver_id`)
            REFERENCES `internet_shop`.`cars` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;