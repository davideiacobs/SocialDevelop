CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `numCollaborators` int(11) DEFAULT '1',
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `description` text,
  `open` tinyint(1) DEFAULT '1',
  `project_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_task_project1_idx` (`project_ID`),
  CONSTRAINT `fk_task_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(2,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(3,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(4,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(5,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(6,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(7,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(8,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(9,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(10,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(11,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(12,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1),(13,'sviluppo',8,'2018-08-28',NULL,'come andiamo',1,1);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-26 15:57:31
