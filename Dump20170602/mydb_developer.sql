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
  KEY `FK_Photo` (`photo_ID`),
  CONSTRAINT `FK_Photo` FOREIGN KEY (`photo_ID`) REFERENCES `developer` (`ID`),
  CONSTRAINT `FK_curriculumPdf` FOREIGN KEY (`curriculum_pdf_ID`) REFERENCES `files` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `developer`
--

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` VALUES (1,'davide','iacobelli','iacobs','blabla@blabla','1995-05-27','aooooo','Phasellus vitae dapibus sapien. \nVestibulum euismod arcu ac leo mattis, eget feugiat nisi venenatis. \nInteger porta, risus non laoreet congue, neque nisl efficitur velit, \nnec congue metus enim in ligula. Donec dignissim diam non mollis semper.\nDonec maximus metus quis massa malesuada sodales. Aenean a ullamcorper odio. \nAenean ac orci vitae tellus aliquet tempus non sit amet dolor. \nPraesent pulvinar, leo vitae venenatis vehicula, magna ante maximus eros,\n a facilisis sem mi eu nulla. Phasellus id lobortis mi.',NULL,'eeeeeehheeheheh',NULL),(3,'Luca','Balestrieri','bales','bales@gmail.it','1995-11-01','123','',NULL,'',NULL),(9,'Manuel','Di Pietro','ameche12@gmail.com','ame@gmail.it','1995-11-01','123','',NULL,'',NULL),(11,'ae','ao','aa','aa@bb','1995-11-01','123','',NULL,'',NULL),(12,'aeee','apooo','aaaa','aa@bb','1995-11-01','123','',NULL,'',NULL),(13,'aeee','apooot4','aaaa455','aa@bb','1995-11-01','34','',NULL,'',NULL),(14,'aeee','apooot4','aaaa45','aa@bb','1995-11-01','12','',NULL,'',NULL),(15,'aeee','apooot4','aaaaa','aa@bb','1995-11-01','12','',NULL,'',NULL),(16,'aeee','apooot4','bbb','aa@bb','1995-11-01','123','',NULL,'',NULL),(17,'aeee','apooot4','bbbb','aa@bb','1995-11-01','123','',NULL,'',NULL),(18,'aooooo','aoooo','aooo','swdd@gem.it','1995-11-01','23','',NULL,'',NULL),(19,'aooooo','aoooo','aooo232','swdd@gem.it','1995-11-01','12','',NULL,'',NULL),(20,'asoo','woj','fjjo','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(21,'asoo','woj','fjjoO','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(22,'asoo','woj','fij','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(23,'asoo','woj','gig','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(25,'asoo','woj','gigi','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(26,'asoo','woj','gigio','2ewqeq@ef.wt','1995-11-01','123','',NULL,'',NULL),(27,'asoo','woj','gigiooo','2ewqeq@ef.wt','1995-11-01','12','',NULL,'',NULL),(28,'wewe','ae','12','aaaa@bbbb','1995-11-01','123','',NULL,'',NULL),(29,'abcd','abcd','1234','abc@abc','1995-11-01','123','',NULL,'',NULL),(30,'ciao','ciao','ciao','ciao@ciao','1995-11-01','ciao','',NULL,'',NULL),(31,'ciao','ciao','we','we@we.it','1995-11-01','we','',NULL,'',NULL),(33,'ciao','ciao','wewe','wewe@we.it','1995-11-01','qw','',NULL,'',NULL),(34,'ciao','ciao','wewewe','wewwee@we.it','1995-11-01','we','',NULL,'',NULL),(35,'ciao','ciao','wewewew','wewwewe@we.it','1995-11-01','we','',NULL,'',NULL),(36,'wee','wew','w','w@w.ot','1995-11-01','23','',NULL,'',NULL),(37,'bla','bl','bla','bla@bla.it','1995-11-01','bl','',NULL,'',NULL),(38,'dab','db','dabs','db@as.it','1995-11-01','23','',NULL,'',NULL),(39,'bau','bau','bau','bau@bau.it','1995-11-01','as','',NULL,'',NULL),(40,'bau','bau','bauu','bauu@bau.it','1995-11-01','1','',NULL,'',NULL),(41,'io','io','io','io@io.it','1995-11-01','io','',NULL,'',NULL),(42,'ababa','ababab','abababab','abababa@efe.ti','1995-11-01','123','',NULL,'',NULL),(43,'yuu','tuu','yuu','yuu@yuu.iy','1995-11-01','yuyu','',NULL,'',NULL),(44,'eee','eee','eee','eee@eee.it','1995-11-01','eee','',NULL,'',NULL),(45,'qqq','qqq','qqq','q-q@q.it','1995-11-01','q','',NULL,'',NULL),(46,'ooo','ooo','ooo','00@00.it','1995-11-01','ooo','',NULL,'',NULL),(47,'12312','12313','123132','1231@12312.it','1995-11-01','123','',NULL,'',NULL),(48,'123','123','123','123@123.ia','1995-04-27','123','',NULL,'',NULL),(49,'123','123','12331','123231@123.ia','1995-04-27','123','',NULL,'',NULL),(51,'123','123','123311','1232311@123.ia','1995-04-27','123','',NULL,'',NULL),(53,'345','454','54545','3423@gierj.iy','1998-11-01','1','',NULL,'',NULL),(54,'888','888','888','8@8.it','1998-11-01','88','',NULL,'',NULL),(56,'000','000','0','0@0.it','1998-11-01','000','234234234234234234234234',NULL,'',NULL),(57,'007','007','007','007@0.it','1998-11-01','000','rtertewrgtwgthwrthwrhrthwrhwrt',19,'',18),(58,'Davide ','Iacobelli ','davideiacobs','davideiacobelli@hotmail.com','1995-04-27','123','Studente di Informatica presso Università degli Studi dell\'Aquila',21,'',20),(59,'2352354','235234524','23523523','342894@3o32.it','1998-11-01','1','34235423423',23,'',22),(60,'234','3423','32423','23@23.it','1998-11-01','12','',NULL,'',NULL),(63,'12','12','1232434','24234230341234@wefe.it','1998-11-01','12','23r23r23rf2w3f2w2wf',27,'',26),(65,'123','123','12323123','123@4534534.it','1998-11-01','123','',29,'',28),(66,'erere','rere','rerer','rter@ere-iy','1998-11-01','123','',NULL,'',NULL),(67,'rere','rere','re','re@re.it','1998-11-01','re','',NULL,'',NULL),(68,'rere','rere','rere','rere@re.it','1998-11-01','123','',NULL,'',NULL),(69,'yihgvbyigfygv','gvcuyhgf','gjghchgcv','dctgxcty@uhiugh.i','1998-11-01','123','guiyvyhuvuyvgyuvuvuyvuovuyvuyvyuvyuvyvb',31,'',30),(70,'2341234','3412','wedfwefd','qwdf@fwqef.it','1998-11-01','123','',NULL,'',NULL),(71,'2341234','3412','wedfwe3rdfd','qw3rdf@fwqef.it','1998-11-01','123','',NULL,'',NULL),(72,'000','0000','0000','000@00.it','1998-11-01','000','',NULL,'',NULL),(73,'000','0000','00000','0000@00.it','1998-11-01','123','',35,'',34),(74,'343542354234','32452354','235235235235','3423452@eiroeirj.it','1998-11-01','123','',37,'',36),(75,'2341234','12','1231231231','1231231231@rgoerj.iyq','1998-11-01','123','',39,'',38),(76,'yyy','yyy','yyy','77#@fwf-it','1998-11-01','123','',41,'',40),(77,'999','999','999','999@999.it','1998-11-01','999','',43,'',42),(78,'hhh','hhh','hhh','hhh@a.it','1998-11-01','123','',NULL,'',NULL),(79,'77777','77777','777777','03412342@wefe.it','1998-11-01','123','',45,'',44),(80,'kkkk','kkk','kkk','kkk@gmail.it','1998-11-01','123','',NULL,'',46),(81,'444','54','123321','ihihf@gmaofk.it','1998-11-01','123','',NULL,'',NULL),(82,'444','54','1233211','ihih1f@gmaofk.it','1998-11-01','123','',NULL,'',NULL),(83,'444','54','123321112','ihih11f@gmaofk.it','1998-11-01','123','',NULL,'',NULL),(84,'444','54','1233211121','ihih11f1@gmaofk.it','1998-11-01','123','',NULL,'',NULL),(85,'444','54','123321112112','ihih11f11@gmaofk.it','1998-11-01','123','',NULL,'',NULL),(86,'444','54','012312','ihih1111f11@gmaofk.it','1998-11-01','123','',NULL,'',NULL);
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

-- Dump completed on 2017-06-02 19:52:56
