-- 创建数据库
CREATE DATABASE IF NOT EXISTS `springboot-wx`;
USE `springboot-wx`;

-- 创建用户并授予权限
CREATE USER IF NOT EXISTS 'test-wx'@'%' IDENTIFIED BY 'test-wx';
GRANT ALL PRIVILEGES ON `springboot-wx`.* TO 'test-wx'@'%';
FLUSH PRIVILEGES;