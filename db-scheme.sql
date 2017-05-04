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
  `curriculumFile` varchar(255) DEFAULT NULL,
  `curriculumString` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `private` tinyint(1) DEFAULT '0',
  `text` text,
  `type` varchar(45) DEFAULT NULL,
  `project_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_message_project1_idx` (`project_ID`),
  CONSTRAINT `fk_message_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` text,
  `coordinator_ID` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_project_developer_idx` (`coordinator_ID`),
  CONSTRAINT `fk_project_developer` FOREIGN KEY (`coordinator_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parent_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_skill_skill1_idx` (`parent_ID`),
  CONSTRAINT `fk_skill_skill1` FOREIGN KEY (`parent_ID`) REFERENCES `skill` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `fk_skill_has_developer_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `timeInterval` time DEFAULT NULL,
  `description` text,
  `open` tinyint(1) DEFAULT '1',
  `project_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_task_project1_idx` (`project_ID`),
  CONSTRAINT `fk_task_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  CONSTRAINT `fk_task_has_developer_developer1` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_developer_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_has_skill`
--

DROP TABLE IF EXISTS `task_has_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_has_skill` (
  `task_ID` int(10) NOT NULL,
  `skill_ID` int(10) NOT NULL,
  `type_ID` int(10) NOT NULL,
  `level_min` int(11) DEFAULT '0',
  PRIMARY KEY (`task_ID`,`skill_ID`,`type_ID`),
  KEY `fk_task_has_skill_skill1_idx` (`skill_ID`),
  KEY `fk_task_has_skill_task1_idx` (`task_ID`),
  KEY `fk_task_has_skill_type1_idx` (`type_ID`),
  CONSTRAINT `fk_task_has_skill_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_skill_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_skill_type1` FOREIGN KEY (`type_ID`) REFERENCES `type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `type` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-04 19:19:59
