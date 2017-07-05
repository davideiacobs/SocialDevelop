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
  CONSTRAINT `fk_tak_type` FOREIGN KEY (`type_ID`) REFERENCES `type` (`ID`),
  CONSTRAINT `fk_task_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (66,'task1',12,'2017-09-01','2017-12-01','blablabla',1,47,18),(67,'task2',21,'2017-03-01','2017-12-01','fjowpejfpojwe',1,47,20),(68,'task3',4,'2017-01-01','2018-01-01','fjpwjfowjef',1,47,19),(74,'hgehe',10,'2018-01-01','2017-07-04','wjefijweifj',1,46,18),(75,'ergewrgererg',12,'2019-01-01','2017-07-04','eipfjwipehfgiwergiw',1,46,18),(76,'hedhjtrt',12,'2019-01-01','2017-07-04','fiwjeifegwje',1,46,19),(77,'Ergewrgererg',11,'2017-07-04','2017-07-04','Eipfjwipehfgiwergiw',1,46,18),(78,'Hedhjtrt',12,'2017-07-04','2017-07-04','Fiwjeifegwje',1,46,19),(79,'1',13,'2017-07-04','2017-07-04','Wfodqwheifhwiehfoiwehf',1,50,18),(80,'2',11,'2017-07-04','2017-07-04','Jmfgwoejkfrewger',1,50,19),(81,'3',30,'2017-07-04','2017-07-04','Ojewpfojwpoejf',1,50,20),(82,'4',15,'2017-07-04','2017-07-04','Jmfgwoejkfrewgerhrfthrthrth',0,50,19),(83,'6',100,'2017-07-04','2017-07-05','Wfodqwheifhwiehfoiwehf',0,50,18),(84,'2',12,'2017-07-04','2017-07-05','Jmfgwoejkfrewger',1,50,19),(85,'3',30,'2017-07-04','2017-07-05','Ojewpfojwpoejf',1,50,20),(86,'4',15,'2017-07-04','2017-07-05','Jmfgwoejkfrewgerhrfthrthrth',0,50,19),(87,'6',100,'2017-07-04','2017-07-05','Wfodqwheifhwiehfoiwehf',0,50,18),(94,'Task2',120,'2017-07-05','2017-07-05','Woifwiehf',0,52,18),(95,'TaskNuovo',12,'2017-07-05','2017-07-05','Eifgwf',1,52,18),(96,'TaskVecchioVecchio',12,'2017-07-05','2017-07-05','Eihfoiw',0,52,18),(97,'TaskNumero1',10,'2017-01-01','2018-01-01','efpwjnf',1,53,18),(98,'TaskNumero2',10,'2017-01-01','2017-11-12','gfmwèrojgnoperg',1,53,19);
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

-- Dump completed on 2017-07-05 11:47:40
