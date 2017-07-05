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
-- Table structure for table `task_has_skill`
--

DROP TABLE IF EXISTS `task_has_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_has_skill` (
  `task_ID` int(10) NOT NULL,
  `skill_ID` int(10) NOT NULL,
  `level_min` int(11) DEFAULT '0',
  PRIMARY KEY (`task_ID`,`skill_ID`),
  KEY `fk_task_has_skill_skill1_idx` (`skill_ID`),
  KEY `fk_task_has_skill_task1_idx` (`task_ID`),
  CONSTRAINT `fk_task_has_skill_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`),
  CONSTRAINT `fk_task_has_skill_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_has_skill`
--

LOCK TABLES `task_has_skill` WRITE;
/*!40000 ALTER TABLE `task_has_skill` DISABLE KEYS */;
INSERT INTO `task_has_skill` VALUES (99,7,6),(99,8,6),(99,10,7),(100,11,10),(100,12,8),(100,13,10),(100,14,6),(101,1,7),(101,2,7),(101,3,6),(102,4,6),(103,8,2),(104,1,8),(104,2,7),(104,4,8),(105,1,7),(105,2,7),(105,3,7),(106,11,6),(107,7,6),(107,8,6),(107,10,6),(108,1,1),(109,8,7);
/*!40000 ALTER TABLE `task_has_skill` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-05 20:20:29
