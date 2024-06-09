-- 创建数据库
CREATE DATABASE IF NOT EXISTS `springboot-test`;
USE `springboot-test`;

-- 创建用户并授予权限
CREATE USER IF NOT EXISTS 'test'@'%' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON `springboot-test`.* TO 'test'@'%';
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