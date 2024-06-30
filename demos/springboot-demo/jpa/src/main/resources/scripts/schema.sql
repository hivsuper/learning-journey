-- 创建数据库
CREATE DATABASE IF NOT EXISTS `springboot-jpa`;
USE `springboot-jpa`;

-- 创建用户并授予权限
CREATE USER IF NOT EXISTS 'test-jpa'@'%' IDENTIFIED BY 'test-jpa';
GRANT ALL PRIVILEGES ON `springboot-jpa`.* TO 'test-jpa'@'%';
FLUSH PRIVILEGES;

-- 创建新表
DROP TABLE IF EXISTS customer;
CREATE TABLE `customer` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

DROP TABLE IF EXISTS customer_password;
CREATE TABLE `customer_password` (
	`customer_id` INT(11) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	`modified_date` DATE NOT NULL,
	PRIMARY KEY (`customer_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

DROP TABLE IF EXISTS customer_ops_log;
CREATE TABLE `customer_ops_log` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`customer_id` INT(11) NOT NULL,
	`event` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;