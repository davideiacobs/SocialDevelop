CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.18-0ubuntu0.17.04.1

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
  `type_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_task_project1_idx` (`project_ID`),
  KEY `fk_tak_type_idx` (`type_ID`),
  CONSTRAINT `fk_tak_type` FOREIGN KEY (`type_ID`) REFERENCES `type` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_task_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(2,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(3,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(4,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(5,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(6,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(7,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(8,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(9,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(10,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(11,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(12,'sviluppo',8,'2018-08-28','2018-10-28','come andiamo',1,1,NULL),(39,'efwefefwe',21,'2020-02-02','2017-06-28','ejmfipwjefijweopjfopwejfopwejfopjwe',1,28,18),(40,'efwefwef',12,'2017-01-01','2017-06-28','hefqwiohefihwiefhoeifhw12',1,29,18),(41,'TaskNumeroUno',12,'2018-10-10','2017-06-28','wefjiwejfiwjepfijwepfjwepjf',1,30,18),(42,'TaskNumeroDue',21,'2019-10-10','2017-06-28','efhniwjhefiweifjwiefjpkowe',1,30,18);
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

-- Dump completed on 2017-06-28 14:48:57
