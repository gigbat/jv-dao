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
    `car_id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `manufacturer_id` BIGINT      NOT NULL,
    `car_model`       VARCHAR(45) NOT NULL,
    `car_deleted`     TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (`car_id`),
    INDEX `manufacturers_cars_id_idx` (`manufacturer_id` ASC) VISIBLE,
    CONSTRAINT `manufacturers_cars_id`
        FOREIGN KEY (`manufacturer_id`)
            REFERENCES `internet_shop`.`manufacturers` (`manufacturer_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
-- Create table drivers
CREATE TABLE `internet_shop`.`drivers`
(
    `driver_id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `driver_name`           VARCHAR(45) NOT NULL,
    `driver_license_number` VARCHAR(45) NOT NULL,
    `driver_deleted`        TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (`driver_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
-- Create table cars_drivers
CREATE TABLE `internet_shop`.`cars_drivers`
(
    `driver_id` BIGINT NOT NULL,
    `car_id`    BIGINT NOT NULL,
    INDEX `driver_id_idx` (`driver_id` ASC) VISIBLE,
    INDEX `car_id_idx` (`car_id` ASC) VISIBLE,
    CONSTRAINT `driver_id`
        FOREIGN KEY (`driver_id`)
            REFERENCES `internet_shop`.`drivers` (`driver_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `car_id`
        FOREIGN KEY (`car_id`)
            REFERENCES `internet_shop`.`cars` (`car_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
-- Clear all db
USE internet_shop;
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cars;
TRUNCATE TABLE cars_drivers;
TRUNCATE TABLE drivers;
TRUNCATE TABLE manufacturers;
SET FOREIGN_KEY_CHECKS = 1;