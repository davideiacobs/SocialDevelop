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
-- Table structure for table `skill_has_developer`
--

DROP TABLE IF EXISTS `skill_has_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill_has_developer` (
  `skill_ID` int(10) NOT NULL,
  `developer_ID` int(10) NOT NULL,
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`skill_ID`,`developer_ID`),
  KEY `fk_skill_has_developer_developer1_idx` (`developer_ID`),
  KEY `fk_skill_has_developer_skill1_idx` (`skill_ID`),
  CONSTRAINT `fk_skill_has_developer_developer1` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_skill_has_developer_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_has_developer`
--

LOCK TABLES `skill_has_developer` WRITE;
/*!40000 ALTER TABLE `skill_has_developer` DISABLE KEYS */;
INSERT INTO `skill_has_developer` VALUES (1,176,6),(1,177,7),(1,179,9),(2,176,10),(2,179,10),(3,176,7),(3,177,7),(3,179,9),(4,176,10),(4,179,6),(5,177,1),(7,176,5),(7,178,10),(8,176,7),(8,178,10),(8,179,7),(10,178,10),(11,176,10),(11,179,9),(12,176,10),(12,179,9),(13,176,10),(13,178,6),(13,179,9),(14,176,10),(14,177,8),(14,179,9);
/*!40000 ALTER TABLE `skill_has_developer` ENABLE KEYS */;
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
