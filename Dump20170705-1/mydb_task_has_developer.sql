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
-- Table structure for table `task_has_developer`
--

DROP TABLE IF EXISTS `task_has_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_has_developer` (
  `task_ID` int(10) NOT NULL,
  `developer_ID` int(10) NOT NULL,
  `state` int(11) DEFAULT '0',
  `date` date DEFAULT NULL,
  `vote` int(11) DEFAULT '0',
  `sender` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_ID`,`developer_ID`),
  KEY `fk_task_has_developer_developer1_idx` (`developer_ID`),
  KEY `fk_task_has_developer_task1_idx` (`task_ID`),
  KEY `fk_sender_idx` (`sender`),
  CONSTRAINT `fk_sender` FOREIGN KEY (`sender`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_developer_developer1` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_developer_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_has_developer`
--

LOCK TABLES `task_has_developer` WRITE;
/*!40000 ALTER TABLE `task_has_developer` DISABLE KEYS */;
INSERT INTO `task_has_developer` VALUES (99,177,1,'2017-07-05',-1,176),(99,178,0,'2017-07-05',-1,176),(99,179,-1,'2017-07-05',-1,179),(102,176,0,'2017-07-05',-1,176),(102,179,0,'2017-07-05',-1,179),(103,176,1,'2017-07-05',-1,177),(103,178,1,'2017-07-05',-1,177),(103,179,1,'2017-07-05',-1,177),(104,176,0,'2017-07-05',-1,176),(104,179,0,'2017-07-05',-1,178),(105,176,-1,'2017-07-05',-1,179),(107,178,1,'2017-07-05',-1,179),(108,176,0,'2017-07-05',-1,180),(108,177,0,'2017-07-05',-1,180),(108,179,0,'2017-07-05',-1,180);
/*!40000 ALTER TABLE `task_has_developer` ENABLE KEYS */;
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
