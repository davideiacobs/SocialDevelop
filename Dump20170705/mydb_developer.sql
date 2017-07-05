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
-- Table structure for table `developer`
--

DROP TABLE IF EXISTS `developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `developer` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(60) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `biography` text,
  `curriculum_pdf_ID` int(10) DEFAULT NULL,
  `curriculumString` varchar(500) DEFAULT NULL,
  `photo_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UC_username` (`username`),
  KEY `FK_curriculumPdf` (`curriculum_pdf_ID`),
  KEY `FK_photo_idx` (`photo_ID`),
  CONSTRAINT `FK_curriculumPdf` FOREIGN KEY (`curriculum_pdf_ID`) REFERENCES `files` (`ID`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `FK_photo` FOREIGN KEY (`photo_ID`) REFERENCES `files` (`ID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `developer`
--

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` VALUES (143,'Davide','Iacobelli','davideiacobs','davideiacobs@gmail.com','1995-04-27','123','Studente di Informatica presso Università degli Studi dell\'Aquila',70,'',69),(144,'Manuel','Di Pietro','ameche12','manuel123ryan@gmail.com','1995-01-07','123','Studente di Informatica',NULL,'',93),(145,'Andrea','Perna','andreaperna','andreaperna24@gmail.com','1996-08-15','123','baòbaòsdkvsvdkskvèksdpvèkspvkèpskthrftjhr',NULL,'<p style=\"text-align: center;\">blablabal <strong>blablabla </strong><em>blablabal </em></p>\r\n<h1><em>blabalbal</em></h1>\r\n<p>fwjkoerfgjowerjfgoej</p>\r\n<p style=\"text-align: center;\">&nbsp;</p>\r\n<p style=\"text-align: center;\">&nbsp;</p>',85),(147,'Luca','Balestrieri','bales','lucabalestrieri.it@gmail.com','1995-07-08','123','blablablablablabla',87,'',86),(148,'nuovo','utente','nuovoUtente','nuovo@utente.it','1995-10-12','123','aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahhh',NULL,'<p>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahhhh</p>',90);
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;
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
