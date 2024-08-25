DROP TABLE IF EXISTS customer;
CREATE TABLE `customer` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	PRIMARY KEY (`id`)
);;

DROP TABLE IF EXISTS customer_password;
CREATE TABLE `customer_password` (
	`customer_id` INT(11) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	`modified_date` DATE NULL,
	PRIMARY KEY (`customer_id`)
);;

DROP TABLE IF EXISTS customer_ops_log;
CREATE TABLE `customer_ops_log` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`customer_id` INT(11) NOT NULL,
	`event` VARCHAR(100) NOT NULL,
	`created_date` DATE NOT NULL,
	PRIMARY KEY (`id`)
);;