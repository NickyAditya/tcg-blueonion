-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for redonion_tcg
DROP DATABASE IF EXISTS `redonion_tcg`;
CREATE DATABASE IF NOT EXISTS `redonion_tcg` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `redonion_tcg`;

-- Dumping structure for table redonion_tcg.cards
DROP TABLE IF EXISTS `cards`;
CREATE TABLE IF NOT EXISTS `cards` (
  `id_kartu` bigint NOT NULL AUTO_INCREMENT,
  `gambar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `harga` decimal(38,2) DEFAULT NULL,
  `deskripsi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `rarity` ENUM('Common', 'Rare', 'Ultra Rare', 'Shiny Ultra Rare', 'Extremely Rare') DEFAULT NULL,
  `kondisi` ENUM('Mint', 'Good', 'Damaged') DEFAULT NULL,
  `stok` int DEFAULT NULL COMMENT 'Jumlah stok',
  `gambar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_kategori` int NOT NULL,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_kartu`),
  KEY `id_kategori` (`id_kategori`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `FK_cards_kategori` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_katgori`),
  CONSTRAINT `FK_cards_users` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table redonion_tcg.cards: ~5 rows (approximately)
INSERT INTO `cards` (`id_kartu`, `gambar_url`, `nama`, `harga`, `deskripsi`, `rarity`, `kondisi`, `stok`, `gambar`, `id_kategori`, `id_user`) VALUES
	(2, NULL, 'Rotom kipas', 1.00, 'Rotom Kipas adalah salah satu bentuk dari Pokémon Rotom. Ketika Rotom memasuki kipas angin, ia berubah menjadi Rotom Kipas, dengan tipe Listrik/Terbang dan memiliki kemampuan Air Slash', 'Common', 'Good', 1, 'rotomkipas.png', 1, 1),
	(3, NULL, 'espathra ex', 5000.00, 'Espathra ex adalah kartu Pokémon ex yang terdapat dalam permainan kartu TCG Pokémon.', 'Shiny Ultra Rare', 'Mint', 1, 'espathra ex.jpeg', 1, 1),
	(5, NULL, 'charizard (holo)', 4608632.00, 'Kartu ini sangat lagnka hanya ada 1 di indonesia.', 'Extremely Rare', 'Mint', 1, 'charizad.jpg', 1, 1),
	(6, NULL, 'alpha black lotus', 49305000000.00, 'versi dari kartu "Black Lotus" dalam permainan Magic: The Gathering yang diluncurkan sebagai bagian dari set "Limited Edition Alpha" pada tahun 1993', 'Rare', 'Mint', 1, 'blacklotus.jpg', 3, 1),
	(7, NULL, 'blue eyes white dragon', 32890400.00, 'Kartu Blue-Eyes White Dragon adalah salah satu kartu paling ikonik dalam permainan Yu-Gi-Oh!, pertama kali diperkenalkan pada tahun 1999. Nilainya sangat bervariasi tergantung pada edisi, kondisi, dan kelangkaannya', 'rare', 'gem mint', 1, 'Blue-Eyes_White_Dragon.webp', 2, 1);

-- Dumping structure for table redonion_tcg.kategori
DROP TABLE IF EXISTS `kategori`;
CREATE TABLE IF NOT EXISTS `kategori` (
  `id_katgori` int NOT NULL AUTO_INCREMENT COMMENT '	ID kategori',
  `nama` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ex: Pokémon, Yu-Gi-Oh, MTG',
  PRIMARY KEY (`id_katgori`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table redonion_tcg.kategori: ~3 rows (approximately)
INSERT INTO `kategori` (`id_katgori`, `nama`) VALUES
	(1, 'pokemon'),
	(2, 'yugioh'),
	(3, 'mtg');

-- Dumping structure for table redonion_tcg.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id_user` int NOT NULL AUTO_INCREMENT COMMENT '	ID user',
  `nama` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('ADMIN','USER') COLLATE utf8mb4_general_ci NOT NULL,  `avatar` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Path to user avatar image',
  `about` text COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'User bio/description',
  `favorite_tags` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Comma-separated list of favorite tags',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time',
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table redonion_tcg.users: ~1 rows (approximately)
INSERT INTO `users` (`id_user`, `nama`, `email`, `password`, `role`) VALUES
	(1, 'admin', 'admin@gmail.com', '$2a$12$2XiXMIjfH3U8r9Hu9mbrgu6UaPxdx/mXhFE0e4/DUCwuw3ithrL0y', 'ADMIN'),
	(2, 'user2', 'user2@gmail.com', '$2a$12$2XiXMIjfH3U8r9Hu9mbrgu6UaPxdx/mXhFE0e4/DUCwuw3ithrL0y', 'USER');

-- Dumping structure for table redonion_tcg.email_change_requests
DROP TABLE IF EXISTS `email_change_requests`;
CREATE TABLE IF NOT EXISTS `email_change_requests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `current_email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `requested_email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `request_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','APPROVED','DENIED') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING',
  `processed_date` timestamp NULL DEFAULT NULL,
  `processed_by` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_email_requests_user` (`user_id`),
  KEY `FK_email_requests_admin` (`processed_by`),
  CONSTRAINT `FK_email_requests_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_user`),
  CONSTRAINT `FK_email_requests_admin` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping structure for table redonion_tcg.user_inventory
DROP TABLE IF EXISTS `user_inventory`;
CREATE TABLE IF NOT EXISTS `user_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_kartu` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `acquired_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_inventory_user` (`id_user`),
  KEY `FK_inventory_card` (`id_kartu`),
  CONSTRAINT `FK_inventory_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  CONSTRAINT `FK_inventory_card` FOREIGN KEY (`id_kartu`) REFERENCES `cards` (`id_kartu`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table redonion_tcg.user_inventory: ~5 rows (approximately)
INSERT INTO `user_inventory` (`id_user`, `id_kartu`, `quantity`)
SELECT 2, id_kartu, 1
FROM `cards`
WHERE stok > 0;

-- Dumping structure for table redonion_tcg.store_card_sell
DROP TABLE IF EXISTS `store_card_sell`;
CREATE TABLE IF NOT EXISTS `store_card_sell` (
  `id_sell` bigint NOT NULL AUTO_INCREMENT,
  `id_kartu` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_sell`),
  KEY `FK_sell_card` (`id_kartu`),
  CONSTRAINT `FK_sell_card` FOREIGN KEY (`id_kartu`) REFERENCES `cards` (`id_kartu`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table redonion_tcg.store_card_sell: ~5 rows (approximately)
INSERT INTO `store_card_sell` (`id_kartu`, `quantity`) VALUES
	(2, 1),  -- Rotom kipas
	(3, 1),  -- espathra ex
	(5, 1),  -- charizard (holo)
	(6, 1),  -- alpha black lotus
	(7, 1);  -- blue eyes white dragon

-- Dumping structure for table redonion_tcg.transactions
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE IF NOT EXISTS `transactions` (
  `id_transaction` bigint NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_kartu` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `total_price` decimal(38,2) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_method` enum('transfer','cash','ewallet') COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('PENDING','COMPLETED','CANCELLED') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING',
  PRIMARY KEY (`id_transaction`),
  KEY `FK_transaction_user` (`id_user`),
  KEY `FK_transaction_card` (`id_kartu`),
  CONSTRAINT `FK_transaction_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  CONSTRAINT `FK_transaction_card` FOREIGN KEY (`id_kartu`) REFERENCES `cards` (`id_kartu`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
