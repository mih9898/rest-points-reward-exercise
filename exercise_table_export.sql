-- Adminer 4.8.1 MySQL 8.0.22 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `points_reward`;
CREATE DATABASE `points_reward` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `points_reward`;

DROP TABLE IF EXISTS `payers`;
CREATE TABLE `payers` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(70) NOT NULL,
                          `balance` int NOT NULL DEFAULT '0',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `payers` (`id`, `name`, `balance`) VALUES
(11,	'DANNON',	1100),
(12,	'UNILEVER',	200),
(13,	'MILLER COORS',	10000);

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `points` int NOT NULL,
                                `date` timestamp NOT NULL,
                                `is_counted` tinyint NOT NULL DEFAULT '0',
                                `payer_id` int NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `payer_id` (`payer_id`),
                                CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`payer_id`) REFERENCES `payers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `transactions` (`id`, `points`, `date`, `is_counted`, `payer_id`) VALUES
(23,	1000,	'2020-11-03 02:00:00',	0,	11),
(24,	200,	'2020-10-31 21:00:00',	0,	12),
(25,	-200,	'2020-11-01 01:00:00',	0,	11),
(26,	10000,	'2020-11-02 02:00:00',	0,	13),
(27,	300,	'2020-10-31 20:00:00',	0,	11);

-- 2021-08-19 06:05:52