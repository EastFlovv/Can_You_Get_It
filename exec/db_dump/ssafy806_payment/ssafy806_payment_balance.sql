-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: k8a806.p.ssafy.io    Database: ssafy806_payment
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `balance`
--

DROP TABLE IF EXISTS `balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balance` (
  `balance_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `concert_id` bigint NOT NULL,
  `price` int NOT NULL,
  `seat` varchar(10) NOT NULL,
  `user_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`balance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balance`
--

LOCK TABLES `balance` WRITE;
/*!40000 ALTER TABLE `balance` DISABLE KEYS */;
INSERT INTO `balance` VALUES (1,9,100,'B15',1,NULL,NULL),(2,9,100,'A10',1,NULL,NULL),(3,9,100,'F1',1,NULL,NULL),(4,9,100,'C17',1,NULL,NULL),(5,9,100,'E13',1,NULL,NULL),(6,9,100,'J17',1,NULL,NULL),(7,9,100,'D13',1,NULL,NULL),(8,9,100,'J10',1,NULL,NULL),(9,9,100,'H17',1,NULL,NULL),(10,9,100,'B2',1,NULL,NULL),(11,9,100,'F7',1,NULL,NULL),(12,9,100,'I2',1,NULL,NULL),(13,9,100,'B10',1,NULL,NULL),(14,9,100,'J6',1,NULL,NULL),(15,9,100,'A9',1,NULL,NULL),(16,9,100,'C6',1,NULL,NULL),(17,9,100,'F13',1,NULL,NULL),(18,9,100,'G20',1,NULL,NULL),(19,9,100,'B16',1,NULL,NULL),(20,9,100,'I1',1,NULL,NULL),(21,9,100,'I18',1,NULL,NULL),(22,9,100,'H2',1,NULL,NULL),(23,9,100,'E5',1,NULL,NULL),(24,9,100,'H20',1,NULL,NULL),(25,9,100,'E19',1,NULL,NULL),(26,9,100,'E1',1,NULL,NULL),(27,9,100,'F17',1,NULL,NULL),(28,9,100,'J18',1,NULL,NULL),(29,9,100,'E20',1,NULL,NULL),(30,9,100,'C2',1,NULL,NULL),(31,9,100,'D6',1,NULL,NULL),(32,9,100,'G19',1,NULL,NULL),(33,9,100,'D12',1,NULL,NULL),(34,9,100,'F19',1,NULL,NULL),(35,9,100,'H14',1,NULL,NULL),(36,9,100,'B1',1,NULL,NULL),(37,9,100,'J7',1,NULL,NULL),(38,9,100,'C19',1,NULL,NULL),(39,9,100,'D14',1,NULL,NULL),(40,9,100,'A10',1,NULL,NULL),(41,9,100,'I16',1,NULL,NULL),(42,9,100,'D20',1,NULL,NULL),(43,9,100,'G16',1,NULL,NULL),(44,9,100,'F12',1,NULL,NULL),(45,9,100,'B8',1,NULL,NULL),(46,9,100,'I17',1,NULL,NULL),(47,9,100,'E18',1,NULL,NULL),(48,9,100,'B9',1,NULL,NULL),(49,9,100,'J2',1,NULL,NULL),(50,9,100,'C4',1,NULL,NULL),(51,9,100,'B18',1,NULL,NULL),(52,9,100,'G8',1,NULL,NULL),(53,9,100,'A8',1,NULL,NULL),(54,9,100,'I7',1,NULL,NULL),(55,9,100,'H9',1,NULL,NULL),(56,9,100,'I15',1,NULL,NULL),(57,9,100,'E3',1,NULL,NULL),(58,9,100,'C1',1,NULL,NULL),(59,9,100,'E3',1,NULL,NULL),(60,9,100,'I16',1,NULL,NULL),(61,9,100,'I11',1,NULL,NULL),(62,9,100,'G6',1,NULL,NULL),(63,9,100,'E2',1,NULL,NULL),(64,9,100,'F18',1,NULL,NULL),(65,9,100,'A14',1,NULL,NULL),(66,9,100,'C11',1,NULL,NULL),(67,9,100,'F16',1,NULL,NULL),(68,9,100,'D19',1,NULL,NULL),(69,9,100,'F20',1,NULL,NULL),(70,9,100,'G4',1,NULL,NULL),(71,9,100,'D17',1,NULL,NULL),(72,9,100,'B6',1,NULL,NULL),(73,9,100,'I10',1,NULL,NULL),(74,9,100,'F15',1,NULL,NULL),(75,9,100,'J12',1,NULL,NULL),(76,9,100,'J11',1,NULL,NULL),(77,9,100,'I14',1,NULL,NULL),(78,9,100,'G12',1,NULL,NULL),(79,9,100,'G17',1,NULL,NULL),(80,9,100,'G15',1,NULL,NULL),(81,9,100,'G9',1,NULL,NULL),(82,9,100,'G2',1,NULL,NULL),(83,9,100,'B11',1,NULL,NULL),(84,9,100,'A1',1,NULL,NULL),(85,9,100,'F6',1,NULL,NULL),(86,9,100,'E10',1,NULL,NULL),(87,9,100,'B5',1,NULL,NULL),(88,9,100,'A20',1,NULL,NULL),(89,9,100,'G14',1,NULL,NULL),(90,9,100,'H18',1,NULL,NULL),(91,9,100,'D9',1,NULL,NULL),(92,9,100,'D8',1,NULL,NULL),(93,9,100,'J12',1,NULL,NULL),(94,9,100,'C5',1,NULL,NULL),(95,9,100,'F14',1,NULL,NULL),(96,9,100,'I17',1,NULL,NULL),(97,9,100,'F13',1,NULL,NULL),(98,9,100,'G9',1,NULL,NULL),(99,9,100,'I16',1,NULL,NULL),(100,9,100,'I4',1,NULL,NULL),(101,9,100,'B17',1,NULL,NULL),(102,9,100,'C13',1,NULL,NULL),(103,9,100,'D7',1,NULL,NULL),(104,9,100,'A12',1,NULL,NULL);
/*!40000 ALTER TABLE `balance` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:34:11
