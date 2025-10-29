-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: graduate_schema
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiuhn9mru62vgqy1h0t1ggc3s7` (`order_id`),
  KEY `FKl7je3auqyq1raj52qmwrgih8x` (`product_id`),
  CONSTRAINT `FKiuhn9mru62vgqy1h0t1ggc3s7` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKl7je3auqyq1raj52qmwrgih8x` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (1,1,1,16),(2,1,1,15),(3,1,1,14),(4,1,1,13),(5,10,2,15),(6,1,3,1),(7,1,3,3),(8,1,3,8),(9,2,3,7),(10,1,4,13),(11,3,5,13),(12,6,6,14),(13,1,7,16),(14,4,8,16),(15,2,8,15),(16,2,8,1),(17,2,8,3),(18,2,9,15),(19,2,10,1),(20,4,11,7),(21,6,12,16),(22,2,13,14),(23,2,14,13),(24,2,15,13),(25,1,16,7),(26,2,16,8),(27,2,17,16),(28,2,18,16);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Những sản phẩm làm từ cà phê','Cà phê'),(2,'Những sản phẩm làm từ trà','Trà');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_vouchers`
--

DROP TABLE IF EXISTS `customer_vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_vouchers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  `used` bit(1) NOT NULL,
  `used_at` datetime(6) DEFAULT NULL,
  `voucher_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2vrl0hpkjsm5ttw4oa5irlcj` (`voucher_id`),
  CONSTRAINT `FK2vrl0hpkjsm5ttw4oa5irlcj` FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_vouchers`
--

LOCK TABLES `customer_vouchers` WRITE;
/*!40000 ALTER TABLE `customer_vouchers` DISABLE KEYS */;
INSERT INTO `customer_vouchers` VALUES (1,'2025-04-20 20:58:45.775665',12,_binary '\0','2025-04-20 20:58:36.661130',2),(2,'2025-04-20 20:58:52.252300',12,_binary '\0',NULL,3);
/*!40000 ALTER TABLE `customer_vouchers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_email` varchar(255) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_phone` varchar(255) NOT NULL,
  `discount_amount` decimal(38,2) DEFAULT NULL,
  `invoice_number` varchar(255) NOT NULL,
  `is_paid` bit(1) NOT NULL,
  `issue_date` datetime(6) NOT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `payment_method` varchar(255) NOT NULL,
  `payment_reference` varchar(50) DEFAULT NULL,
  `shipping_address` varchar(255) NOT NULL,
  `shipping_fee` decimal(38,2) DEFAULT NULL,
  `subtotal` decimal(38,2) NOT NULL,
  `tax_amount` decimal(38,2) DEFAULT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `delivery_staff_id` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `processing_staff_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  UNIQUE KEY `UKe718q5klx5pempy28p2nx88a6` (`order_id`),
  KEY `FK1s7p0e11j0nf8kgs4kwic4w0c` (`delivery_staff_id`),
  KEY `FKjg4mvstkua880vam5y3qe9ydm` (`processing_staff_id`),
  CONSTRAINT `FK1s7p0e11j0nf8kgs4kwic4w0c` FOREIGN KEY (`delivery_staff_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK4ko3y00tkkk2ya3p6wnefjj2f` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKjg4mvstkua880vam5y3qe9ydm` FOREIGN KEY (`processing_staff_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,'lybon219@gmail.com','ly bi','0397008778',0.00,'INV-20250420-00002',_binary '','2025-04-20 20:31:25.362946',NULL,'2025-04-20 15:39:36.212826','MOMO','3299449761','138 Nguyen Thi Buu',0.00,400000.00,0.00,400000.00,3,2,2),(2,'nhanpt179@gmail.com','mohamepsena','123489898',0.00,'INV-20250420-00013',_binary '','2025-04-20 20:31:49.647166',NULL,'2025-04-20 16:43:43.703570','MOMO','3299450194','123 đường abc',25000.00,90000.00,0.00,115000.00,3,13,2);
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `customer_email` varchar(255) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `delivery_at` datetime(6) DEFAULT NULL,
  `discount_amount` decimal(38,2) NOT NULL,
  `final_amount` decimal(38,2) NOT NULL,
  `order_date` datetime(6) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `processed_at` datetime(6) DEFAULT NULL,
  `shipping_fee` decimal(38,2) NOT NULL,
  `status` enum('CANCELLED','COMPLETED','DELIVERING','PENDING','PLACED') NOT NULL,
  `total_price` decimal(38,2) NOT NULL,
  `delivery_staff_id` bigint DEFAULT NULL,
  `processing_staff_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKajt66gbaasyg08mcga0cumdki` (`delivery_staff_id`),
  KEY `FK1xnlekpevehhns8yuvr42f0gc` (`processing_staff_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK1xnlekpevehhns8yuvr42f0gc` FOREIGN KEY (`processing_staff_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKajt66gbaasyg08mcga0cumdki` FOREIGN KEY (`delivery_staff_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'123 nguyen duy tra',NULL,'lybon219@gmail.com','ly bi',NULL,0.00,200000.00,'2025-04-20 15:37:12.350231','123489898',NULL,15000.00,'CANCELLED',185000.00,NULL,NULL,4),(2,'138 Nguyen Thi Buu','2025-04-20 20:31:25.336671','lybon219@gmail.com','ly bi','2025-04-20 18:20:51.287795',0.00,400000.00,'2025-04-20 15:38:43.243523','0397008778','2025-04-20 18:20:41.161772',0.00,'COMPLETED',400000.00,3,2,4),(3,'398 Ma ha ka',NULL,'lybon219@gmail.com','ly bi',NULL,0.00,150000.00,'2025-04-20 15:40:28.558300','123489898','2025-04-20 18:19:17.389492',15000.00,'CANCELLED',135000.00,3,2,4),(4,'258 Lee liem',NULL,'lybon219@gmail.com','ly bi',NULL,0.00,75000.00,'2025-04-20 15:41:15.230540','123489898','2025-04-20 16:12:32.147199',25000.00,'CANCELLED',50000.00,3,2,4),(5,'358 Lê liêm',NULL,'lybon219@gmail.com','ly bi',NULL,75000.00,90000.00,'2025-04-20 15:53:47.035927','159357880',NULL,15000.00,'CANCELLED',150000.00,NULL,NULL,4),(6,'357 Kien hoa',NULL,'lybon219@gmail.com','ly bi',NULL,0.00,285000.00,'2025-04-20 16:09:20.418732','0369258741',NULL,15000.00,'CANCELLED',270000.00,NULL,NULL,4),(7,'123 Nguyen van thuc',NULL,'lybon219@gmail.com','ly bi',NULL,0.00,75000.00,'2025-04-20 16:14:54.947255','123489898',NULL,25000.00,'PENDING',50000.00,NULL,NULL,4),(8,'138 Vo van kiet',NULL,'pnhan1792003@gmail.com','Nhân Phạm',NULL,0.00,400000.00,'2025-04-20 16:35:33.376183','0397008665',NULL,0.00,'PENDING',400000.00,NULL,NULL,6),(9,'123 Nguyen van thuc',NULL,'pnhan1792003@gmail.com','Nhân Phạm',NULL,0.00,105000.00,'2025-04-20 16:36:44.068519','03970087025',NULL,25000.00,'PENDING',80000.00,NULL,NULL,6),(10,'1238 nGUYEN DINH TUY',NULL,'pnhan1792003@gmail.com','Nhân Phạm',NULL,30000.00,55000.00,'2025-04-20 16:39:40.737202','0397008774',NULL,25000.00,'PENDING',60000.00,NULL,NULL,6),(11,'345 Kien La Mac',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,0.00,115000.00,'2025-04-20 16:41:14.868450','456987123',NULL,15000.00,'CANCELLED',100000.00,NULL,NULL,12),(12,'123 Anh Gia Kiet',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,0.00,300000.00,'2025-04-20 16:41:59.751363','123489898','2025-04-20 16:46:18.113997',0.00,'CANCELLED',300000.00,3,2,12),(13,'123 đường abc','2025-04-20 20:31:49.647166','nhanpt179@gmail.com','mohamepsena','2025-04-20 16:48:01.056693',0.00,115000.00,'2025-04-20 16:42:55.142976','123489898','2025-04-20 16:47:23.594269',25000.00,'COMPLETED',90000.00,3,2,12),(14,'123 nguyen duy tra',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,0.00,115000.00,'2025-04-20 16:51:14.593157','123489898',NULL,15000.00,'CANCELLED',100000.00,NULL,NULL,12),(15,'138 Nguyen Thi Buu',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,0.00,115000.00,'2025-04-20 16:51:31.953170','123489898','2025-04-20 16:52:28.124320',15000.00,'CANCELLED',100000.00,11,2,12),(16,'454 XUan dieu',NULL,'nhanpt179@gmail.com','mohamepsena','2025-04-20 16:57:47.888263',0.00,100000.00,'2025-04-20 16:55:02.671699','123489898','2025-04-20 16:57:25.619216',25000.00,'DELIVERING',75000.00,11,2,12),(17,'3354 kA LA MA',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,50000.00,65000.00,'2025-04-20 18:34:28.598472','123489898',NULL,15000.00,'CANCELLED',100000.00,NULL,NULL,12),(18,'123 Nguyen van thuc',NULL,'nhanpt179@gmail.com','mohamepsena',NULL,7500.00,107500.00,'2025-04-20 20:58:36.530704','123489898',NULL,15000.00,'PENDING',100000.00,NULL,NULL,12);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('CASH','MOMO') NOT NULL,
  `status` enum('FAILED','PAID','PENDING','REFUNDED') NOT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8vo36cen604as7etdfwmyjsxt` (`order_id`),
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,200000.00,'2025-04-20 15:37:12.481662','CASH','PENDING',NULL,1),(2,400000.00,'2025-04-20 15:39:36.212826','MOMO','PAID','3299449761',2),(3,150000.00,'2025-04-20 15:40:28.579122','CASH','PENDING',NULL,3),(4,75000.00,'2025-04-20 15:41:15.246815','CASH','PENDING',NULL,4),(5,90000.00,'2025-04-20 15:53:47.048054','CASH','PENDING',NULL,5),(6,285000.00,'2025-04-20 16:09:20.426748','CASH','PENDING',NULL,6),(7,75000.00,'2025-04-20 16:14:54.955392','CASH','PENDING',NULL,7),(8,400000.00,'2025-04-20 16:35:33.389199','CASH','PENDING',NULL,8),(9,105000.00,'2025-04-20 16:36:44.072102','CASH','PENDING',NULL,9),(10,55000.00,'2025-04-20 16:39:40.737202','CASH','PENDING',NULL,10),(11,115000.00,'2025-04-20 16:41:14.885309','CASH','PENDING',NULL,11),(12,300000.00,'2025-04-20 16:41:59.751363','CASH','PENDING',NULL,12),(13,115000.00,'2025-04-20 16:43:43.703570','MOMO','PAID','3299450194',13),(14,115000.00,'2025-04-20 16:51:14.593157','CASH','PENDING',NULL,14),(15,115000.00,'2025-04-20 16:52:08.279332','MOMO','PAID','3299450293',15),(16,100000.00,'2025-04-20 16:55:02.671699','CASH','PENDING',NULL,16),(17,65000.00,'2025-04-20 18:34:28.665849','CASH','PENDING',NULL,17),(18,107500.00,'2025-04-20 20:58:36.541069','CASH','PENDING',NULL,18);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_transactions`
--

DROP TABLE IF EXISTS `point_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  `points_change` int NOT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(255) DEFAULT NULL,
  `transaction_type` enum('ADJUST','ERN','REDEEM') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_transactions`
--

LOCK TABLES `point_transactions` WRITE;
/*!40000 ALTER TABLE `point_transactions` DISABLE KEYS */;
INSERT INTO `point_transactions` VALUES (1,'2025-04-20 18:35:00.797925',12,50,17,'ORDER','ADJUST'),(2,'2025-04-20 20:31:25.449144',4,400,2,'ORDER','ERN'),(3,'2025-04-20 20:31:49.680629',12,115,13,'ORDER','ERN'),(4,'2025-04-20 20:57:57.410402',12,-50,2,'VOUCHER','REDEEM'),(5,'2025-04-20 20:58:45.778661',12,-50,2,'VOUCHER','REDEEM'),(6,'2025-04-20 20:58:52.256425',12,-40,3,'VOUCHER','REDEEM');
/*!40000 ALTER TABLE `point_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `points`
--

DROP TABLE IF EXISTS `points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `balance` int NOT NULL,
  `customer_id` bigint NOT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `points`
--

LOCK TABLES `points` WRITE;
/*!40000 ALTER TABLE `points` DISABLE KEYS */;
INSERT INTO `points` VALUES (1,25,12,'2025-04-20 20:58:52.258391'),(2,400,4,'2025-04-20 20:31:25.446840'),(3,0,7,'2025-04-20 20:57:19.659905');
/*!40000 ALTER TABLE `points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `img_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `stock` bigint NOT NULL DEFAULT '0',
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Cà phê mới pha, tươi mới mỗi sáng',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744944752/products/gsxxjm35ofzjf1a0smci.jpg','Cà Phê Sáng Mới',30000,246,1),(2,'Cà phê đậm đà, hương vị kéo dài',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744944767/products/y8slaudlqwrrd0xqzxh4.jpg','Hương Vị Vĩnh Cửu',30000,0,1),(3,'Cà phê nhẹ nhàng cho buổi chiều thư giãn',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744945895/products/pro1i6ktvvcy1znszngd.jpg','Cà Phê Hoàng Hôn',30000,246,1),(4,'Cà phê espresso mạnh mẽ, thích hợp cho những ai yêu thích đậm đà',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744944799/products/u3hkmrfkpdkavne9poyo.jpg','Tươi Ngon Espresso',30000,0,1),(5,'Cà phê cao cấp dành cho những tín đồ cà phê thực thụ',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744945969/products/pcepkthjswdfbtci5glb.jpg','Cà Phê Đặc Biệt',25000,248,1),(6,'Sự kết hợp hoàn hảo giữa vị đậm và vị nhẹ, thích hợp cho mọi khẩu vị',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946255/products/fm9mgogliwnkgwl1aqmi.jpg','Vị Đậm Vị Nhẹ',25000,5,1),(7,'Cà phê nhẹ nhàng, thanh thoát như làn gió mùa xuân.\n',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946320/products/fms4yyz9uigb4jlppvk9.jpg','Cà Phê Mùa Xuân',25000,242,1),(8,'Cà phê đặc trưng với hương vị riêng biệt, đến từ những vùng trồng nổi tiếng',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946349/products/mymhkzs9dm2lovdg31mh.jpg','Cà Phê Bản Sắc',25000,245,1),(9,'Cà phê đậm đà, mạnh mẽ cho những ai yêu thích sự mạnh mẽ trong từng ngụm',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946401/products/ckowwyjsxl2lqxlnhocd.jpg','Cà Phê Mạnh Mẽ',25000,4,1),(10,'Cà phê hoàn hảo với sự hòa quyện giữa các hương vị độc đáo',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946432/products/c9jmfg7xps7dgz7u2gwe.jpg','Cà Phê Hòa Quyện',30000,0,1),(11,'Trà nhẹ nhàng, ngọt ngào như tình yêu',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946467/products/r5p18hsbeoofs6dph0ar.jpg','Trà Tình Yêu',30000,99,2),(12,'Trà mát, thanh khiết, mang lại cảm giác như mùa xuân',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946502/products/ymufgyfvldqmsadjutxm.jpg','Trà Thanh Xuân',30000,99,2),(13,'Cà phê sáng tạo, mang lại cảm giác mới lạ trong từng tách',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946548/products/fhqfn9a2kjclytukeigy.jpg','Sáng Tạo Cà Phê',50000,97,1),(14,'Trà nguyên chất, thanh khiết từ thiên nhiên',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946599/products/xbetthk4a5jdgyymedac.jpg','Trà Thiên Nhiên',45000,47,2),(15,'Cà phê mang đậm chất tự nhiên, mộc mạc, không cầu kỳ',1,'https://res.cloudinary.com/dx9yg1sze/image/upload/v1744946671/products/ohyibww71ikyzfkgbcb0.jpg','Cà Phê Mộc Mạc',40000,87,1),(16,'',1,'http://res.cloudinary.com/dx9yg1sze/image/upload/v1745118807/n5vmysltaxlrxynuktv5.jpg','aphogato',50000,44,1),(17,'',0,'http://res.cloudinary.com/dx9yg1sze/image/upload/v1745119368/lbcg65kjynjm3onm7uhf.jpg','LATEEE',40000,40,1),(18,'',0,'http://res.cloudinary.com/dx9yg1sze/image/upload/v1745119244/gq1q6bl5ebeo3opjhep0.jpg','APHOGATO2',30000,40,1),(20,'',0,'http://res.cloudinary.com/dx9yg1sze/image/upload/v1745156232/xyy8pxszbyncwi0xrfoq.jpg','san pham test23',50000,0,2);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9yqmlf28ges8c30nj4v4hva7t` (`product_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FK9yqmlf28ges8c30nj4v4hva7t` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Ngon','2025-04-20 16:08:54.386257',4,14,4),(2,'Quá ngon\n','2025-04-20 20:54:56.236831',5,16,12),(3,'Cũng được, mọi người nên thử','2025-04-20 20:55:34.306761',4,16,7);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `img` varbinary(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  CONSTRAINT `users_chk_1` CHECK ((`role` between 0 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@gmail.com',NULL,'admin','$2a$12$H4y2p/fZSAXyxE/Uqp0Lqey6iJked2TotESdPHADfzO5DK/AIzVxu',0),(2,'staff1@gmail.com',NULL,'Staff1','$2a$12$Fagkuo70CqSY3ETipuEXRevZenGAG3bJnyolrh6BAPQTkOl.Q9qre',2),(3,'delivery1@gmail.com',NULL,'Delivery','$2a$12$f.on.sdq/n2VDcXp.6qA8.4z2l3KcigwG8KVfiuT3D/o4oIi1Bz/6',3),(4,'lybon219@gmail.com',NULL,'ly bi',NULL,1),(5,'ynhup1999@gmail.com',NULL,'Yến Như Phạm Thị',NULL,1),(6,'pnhan1792003@gmail.com',NULL,'Nhân Phạm',NULL,1),(7,'nhanpt@gmail.com',NULL,'Phạm Thành Nhân','$2a$12$oFhpkmg8VgrmGbv5fyoEPOu5ySHfK9sXvTlco1UUZVGqcSnzqxxji',1),(8,'nhipyt@gmail.com',NULL,'Ala mohamep','$2a$12$/eXNWOmMTr4aLfQEDxdfreYwBDrJ4nx.TAGMw47jBMhrv5c1huq.a',1),(10,'staff2@gmail.com',NULL,'STAFF2','$2a$12$gS5ZzXN.JDeDO3Bs.z/lWeXG1ACs5Jv2z9Z.qYZMjAvV.ye.Yc58q',2),(11,'delivery2@gmail.com',NULL,'DELIVERY2','$2a$12$Do3ifrGDSs.QZ/fXpz.5/Ok06lqYkcnjDxjlyI.0U9a3kCC6As/tC',3),(12,'nhanpt179@gmail.com',NULL,'mohamepsena','$2a$12$T7.Qgl2.U6VyXbxdJf6wF.hi4ErA6m4swBGhgRjnppcuircnoCyfG',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vouchers`
--

DROP TABLE IF EXISTS `vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vouchers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_usage` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount_type` enum('FIXED_AMOUNT','PERCENTAGE','SHIPPING_PERCENTAGE') NOT NULL,
  `discount_value` decimal(38,2) NOT NULL,
  `end_date` datetime(6) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `is_public` bit(1) NOT NULL,
  `max_usage` int DEFAULT NULL,
  `minimum_order_value` decimal(38,2) DEFAULT NULL,
  `points_required` int DEFAULT NULL,
  `start_date` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vouchers`
--

LOCK TABLES `vouchers` WRITE;
/*!40000 ALTER TABLE `vouchers` DISABLE KEYS */;
INSERT INTO `vouchers` VALUES (1,'WELCOME50','2025-04-20 10:31:33.612768',3,'Giảm 50% phí vận chuyển','PERCENTAGE',50.00,'2025-05-20 00:00:00.000000',_binary '',_binary '\0',NULL,0.00,0,'2025-04-20 10:31:08.000000','2025-04-20 15:42:17.798265'),(2,'FREESHIP50','2025-04-20 15:43:01.062304',2,'GIẢM 50% PHÍ GIAO HÀNG','SHIPPING_PERCENTAGE',50.00,'2025-05-20 00:00:00.000000',_binary '',_binary '',NULL,10000.00,50,'2025-04-20 15:42:22.000000','2025-04-20 20:58:45.781662'),(3,'WELCOM20','2025-04-20 15:43:42.234866',1,'GIẢM 20K GIÁ TRỊ ĐƠN','FIXED_AMOUNT',20000.00,'2025-05-20 00:00:00.000000',_binary '',_binary '',NULL,10000.00,40,'2025-04-20 15:43:07.000000','2025-04-20 20:58:52.259460'),(4,'VOUCHERRAC','2025-04-20 15:44:16.679892',0,'GIAM GIA 100%','PERCENTAGE',100.00,'2025-05-20 00:00:00.000000',_binary '',_binary '\0',NULL,0.00,0,'2025-04-20 15:43:43.000000','2025-04-20 15:45:31.093077'),(5,'VOUCHERCUCRAC','2025-04-20 15:44:48.323829',0,'GIAM 100K','FIXED_AMOUNT',100000.00,'2025-05-20 00:00:00.000000',_binary '',_binary '\0',NULL,500000.00,0,'2025-04-20 15:44:20.000000','2025-04-20 15:51:53.505206'),(6,'VOUCHERTESST','2025-04-20 15:45:53.969758',0,'123312','SHIPPING_PERCENTAGE',50.00,'2025-05-20 00:00:00.000000',_binary '',_binary '',NULL,500000.00,123,'2025-04-20 15:45:32.000000','2025-04-20 15:47:33.335255'),(7,'VOICHERCUCUASND','2025-04-20 15:47:03.968740',0,'','SHIPPING_PERCENTAGE',100.00,'2025-05-20 00:00:00.000000',_binary '\0',_binary '',NULL,0.00,100,'2025-04-20 15:46:34.000000','2025-04-20 15:47:03.968740'),(8,'GIAM100K','2025-04-20 16:16:40.141449',0,'','FIXED_AMOUNT',100000.00,'2025-05-20 00:00:00.000000',_binary '',_binary '\0',NULL,0.00,0,'2025-04-20 16:16:23.000000','2025-04-20 16:16:47.922735'),(9,'TESTDATE','2025-04-20 20:46:57.469724',0,'','PERCENTAGE',50.00,'2025-04-19 00:00:00.000000',_binary '\0',_binary '\0',NULL,0.00,0,'2025-04-01 00:00:00.000000','2025-04-20 20:46:57.469724');
/*!40000 ALTER TABLE `vouchers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-10 13:51:56
