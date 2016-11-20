-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: localhost    Database: cs201_fp
-- ------------------------------------------------------
-- Server version	5.7.16-0ubuntu0.16.04.1

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
-- Table structure for table `Discussions`
--

DROP TABLE IF EXISTS `Discussions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Discussions` (
  `discussion_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `section_id` mediumint(8) unsigned NOT NULL,
  `class_id` bigint(20) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `monday` bit(1) NOT NULL DEFAULT b'0',
  `tuesday` bit(1) NOT NULL DEFAULT b'0',
  `wednesday` bit(1) NOT NULL DEFAULT b'0',
  `thursday` bit(1) NOT NULL DEFAULT b'0',
  `friday` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`discussion_id`),
  UNIQUE KEY `Column 2` (`section_id`),
  KEY `FK_Discussions_USCClasses` (`class_id`),
  CONSTRAINT `FK_Discussions_USCClasses` FOREIGN KEY (`class_id`) REFERENCES `USCClasses` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Discussions`
--

LOCK TABLES `Discussions` WRITE;
/*!40000 ALTER TABLE `Discussions` DISABLE KEYS */;
INSERT INTO `Discussions` VALUES (1,28980,1,'16:20:00','17:10:00','\0','\0','','\0','\0');
/*!40000 ALTER TABLE `Discussions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EnrolledClasses`
--

DROP TABLE IF EXISTS `EnrolledClasses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EnrolledClasses` (
  `user_id` bigint(20) NOT NULL,
  `class_id` bigint(20) NOT NULL,
  UNIQUE KEY `user_id_class_id` (`user_id`,`class_id`),
  KEY `FK_EnrolledClasses_USCClasses` (`class_id`),
  CONSTRAINT `FK_EnrolledClasses_USCClasses` FOREIGN KEY (`class_id`) REFERENCES `USCClasses` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_EnrolledClasses_Users` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EnrolledClasses`
--

LOCK TABLES `EnrolledClasses` WRITE;
/*!40000 ALTER TABLE `EnrolledClasses` DISABLE KEYS */;
INSERT INTO `EnrolledClasses` VALUES (3,1);
/*!40000 ALTER TABLE `EnrolledClasses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EnrolledDiscussions`
--

DROP TABLE IF EXISTS `EnrolledDiscussions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EnrolledDiscussions` (
  `user_id` bigint(20) NOT NULL,
  `discussion_id` bigint(20) NOT NULL,
  UNIQUE KEY `user_id_discussion_id` (`user_id`,`discussion_id`),
  KEY `FK_EnrolledClasses_Users` (`user_id`),
  KEY `FK_EnrolledClasses_USCClasses` (`discussion_id`),
  CONSTRAINT `EnrolledDiscussions_ibfk_1` FOREIGN KEY (`discussion_id`) REFERENCES `Discussions` (`discussion_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EnrolledDiscussions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EnrolledDiscussions`
--

LOCK TABLES `EnrolledDiscussions` WRITE;
/*!40000 ALTER TABLE `EnrolledDiscussions` DISABLE KEYS */;
INSERT INTO `EnrolledDiscussions` VALUES (3,1);
/*!40000 ALTER TABLE `EnrolledDiscussions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EnrolledLabs`
--

DROP TABLE IF EXISTS `EnrolledLabs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EnrolledLabs` (
  `user_id` bigint(20) NOT NULL,
  `lab_id` bigint(20) NOT NULL,
  UNIQUE KEY `user_id_lab_id` (`user_id`,`lab_id`),
  KEY `FK_EnrolledClasses_Users` (`user_id`),
  KEY `FK_EnrolledClasses_USCClasses` (`lab_id`),
  CONSTRAINT `EnrolledLabs_ibfk_1` FOREIGN KEY (`lab_id`) REFERENCES `Labs` (`lab_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EnrolledLabs_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EnrolledLabs`
--

LOCK TABLES `EnrolledLabs` WRITE;
/*!40000 ALTER TABLE `EnrolledLabs` DISABLE KEYS */;
INSERT INTO `EnrolledLabs` VALUES (3,2);
/*!40000 ALTER TABLE `EnrolledLabs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EventRelationships`
--

DROP TABLE IF EXISTS `EventRelationships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EventRelationships` (
  `user_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `relationship_type` enum('owned','shared') NOT NULL DEFAULT 'owned',
  UNIQUE KEY `user_id_event_id_relationship_type` (`user_id`,`event_id`),
  KEY `FK_EventRelationships_Events` (`event_id`),
  CONSTRAINT `FK_EventRelationships_Events` FOREIGN KEY (`event_id`) REFERENCES `Events` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_EventRelationships_Users` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EventRelationships`
--

LOCK TABLES `EventRelationships` WRITE;
/*!40000 ALTER TABLE `EventRelationships` DISABLE KEYS */;
INSERT INTO `EventRelationships` VALUES (3,29,'owned'),(3,30,'owned'),(3,39,'owned'),(3,40,'owned'),(6,41,'owned'),(7,43,'owned');
/*!40000 ALTER TABLE `EventRelationships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Events`
--

DROP TABLE IF EXISTS `Events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Events` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT 'New Event',
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `color` enum('red','orange','yellow','green','blue','cyan','magenta') NOT NULL DEFAULT 'blue',
  `notify` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Events`
--

LOCK TABLES `Events` WRITE;
/*!40000 ALTER TABLE `Events` DISABLE KEYS */;
INSERT INTO `Events` VALUES (29,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','HNB','my more swag event','red',''),(30,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(32,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(33,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(35,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(36,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(37,'test event','2016-11-16 13:50:33','2016-11-16 15:40:00','basement lol','','blue',''),(39,'best event','2016-12-16 13:50:33','2016-12-16 15:40:00','HNB','','red',''),(40,'best event','2016-12-16 13:50:33','2016-12-16 15:40:00','HNB','','red',''),(41,'New Event!','2016-11-16 14:48:37','2016-11-16 14:48:37','','','blue',''),(43,'gautams event','2016-11-20 12:00:00','2016-11-20 14:00:00','my butt','lol','magenta','\0');
/*!40000 ALTER TABLE `Events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Exams`
--

DROP TABLE IF EXISTS `Exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Exams` (
  `exam_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_id` bigint(20) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`exam_id`),
  KEY `FK_Exams_USCClasses` (`class_id`),
  CONSTRAINT `FK_Exams_USCClasses` FOREIGN KEY (`class_id`) REFERENCES `USCClasses` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Exams`
--

LOCK TABLES `Exams` WRITE;
/*!40000 ALTER TABLE `Exams` DISABLE KEYS */;
INSERT INTO `Exams` VALUES (1,1,'2016-12-02 19:00:00','2016-12-02 21:00:00');
/*!40000 ALTER TABLE `Exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Friendships`
--

DROP TABLE IF EXISTS `Friendships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Friendships` (
  `student_one` bigint(20) NOT NULL,
  `student_two` bigint(20) NOT NULL,
  `approved` enum('pending','accepted','declined') NOT NULL DEFAULT 'pending',
  KEY `FK_Friendships_Users` (`student_one`),
  KEY `FK_Friendships_Users_2` (`student_two`),
  CONSTRAINT `FK_Friendships_Users` FOREIGN KEY (`student_one`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_Friendships_Users_2` FOREIGN KEY (`student_two`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Friendships`
--

LOCK TABLES `Friendships` WRITE;
/*!40000 ALTER TABLE `Friendships` DISABLE KEYS */;
INSERT INTO `Friendships` VALUES (3,6,'pending'),(7,3,'accepted');
/*!40000 ALTER TABLE `Friendships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Labs`
--

DROP TABLE IF EXISTS `Labs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Labs` (
  `lab_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `section_id` mediumint(8) unsigned NOT NULL,
  `class_id` bigint(20) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `monday` bit(1) NOT NULL DEFAULT b'0',
  `tuesday` bit(1) NOT NULL DEFAULT b'0',
  `wednesday` bit(1) NOT NULL DEFAULT b'0',
  `thursday` bit(1) NOT NULL DEFAULT b'0',
  `friday` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`lab_id`),
  UNIQUE KEY `Column 2` (`section_id`),
  KEY `FK_Discussions_USCClasses` (`class_id`),
  CONSTRAINT `Labs_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `USCClasses` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Labs`
--

LOCK TABLES `Labs` WRITE;
/*!40000 ALTER TABLE `Labs` DISABLE KEYS */;
INSERT INTO `Labs` VALUES (1,30238,1,'12:00:00','13:50:00','\0','\0','','\0','\0'),(2,30241,1,'18:00:00','19:50:00','\0','','\0','\0','\0');
/*!40000 ALTER TABLE `Labs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notifications`
--

DROP TABLE IF EXISTS `Notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `notification_type` enum('email','online') NOT NULL DEFAULT 'online',
  `completed` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notifications`
--

LOCK TABLES `Notifications` WRITE;
/*!40000 ALTER TABLE `Notifications` DISABLE KEYS */;
INSERT INTO `Notifications` VALUES (1,3,40,'email',''),(2,3,40,'email','');
/*!40000 ALTER TABLE `Notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StudyRecommendations`
--

DROP TABLE IF EXISTS `StudyRecommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StudyRecommendations` (
  `target_user_id` bigint(20) NOT NULL,
  `recommended_user_id` bigint(20) NOT NULL,
  `class_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `notification_id` bigint(20) NOT NULL,
  KEY `FK_StudyRecommendations_Users` (`target_user_id`),
  KEY `FK_StudyRecommendations_Users_2` (`recommended_user_id`),
  KEY `FK_StudyRecommendations_USCClasses` (`class_id`),
  KEY `FK_StudyRecommendations_Events` (`event_id`),
  KEY `FK_StudyRecommendations_Notifications` (`notification_id`),
  CONSTRAINT `FK_StudyRecommendations_Events` FOREIGN KEY (`event_id`) REFERENCES `Events` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_StudyRecommendations_Notifications` FOREIGN KEY (`notification_id`) REFERENCES `Notifications` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_StudyRecommendations_USCClasses` FOREIGN KEY (`class_id`) REFERENCES `USCClasses` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_StudyRecommendations_Users` FOREIGN KEY (`target_user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_StudyRecommendations_Users_2` FOREIGN KEY (`recommended_user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StudyRecommendations`
--

LOCK TABLES `StudyRecommendations` WRITE;
/*!40000 ALTER TABLE `StudyRecommendations` DISABLE KEYS */;
/*!40000 ALTER TABLE `StudyRecommendations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USCClasses`
--

DROP TABLE IF EXISTS `USCClasses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USCClasses` (
  `class_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `section_id` mediumint(8) unsigned NOT NULL,
  `dept` varchar(10) NOT NULL,
  `class_num` smallint(5) unsigned NOT NULL,
  `semester` mediumint(9) NOT NULL COMMENT 'format: 20163',
  `name` varchar(100) NOT NULL,
  `professor` varchar(50) NOT NULL,
  `location` varchar(255) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `monday` bit(1) NOT NULL DEFAULT b'0',
  `tuesday` bit(1) NOT NULL DEFAULT b'0',
  `wednesday` bit(1) NOT NULL DEFAULT b'0',
  `thursday` bit(1) NOT NULL DEFAULT b'0',
  `friday` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`class_id`),
  UNIQUE KEY `Column 2` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USCClasses`
--

LOCK TABLES `USCClasses` WRITE;
/*!40000 ALTER TABLE `USCClasses` DISABLE KEYS */;
INSERT INTO `USCClasses` VALUES (1,29909,'CSCI',201,20163,'Principles of Software Development','Jeffrey Miller Ph.D.','WPH B27','09:30:00','10:50:00','\0','','\0','','\0');
/*!40000 ALTER TABLE `USCClasses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (2,'test','537937FF92398C4454ECDE0E0DCFEB74CDAA780C776589BC31F59290235F36B920D733D59A6F87AA57051E999D4C514CFBC4A680C0DF2711BFA89B0DFAFC154B','swag@swag.com','2016-11-13 03:31:06'),(3,'vadim','0D2F44BE8166D5812502A6A908E2E695A5686DE9300B93BC2AF415915F80A5A61F342DB9089B85FE1F2F9F96130BC1056E5AF51EB802BC14C206632C2A4B65BC','vkorolik@gmail.com','2016-11-13 03:51:12'),(6,'ayylmao','0BA388B050F946467054A2339207B40DA1CAA81C1F226589F099B0A6A58482673659BD81082A0A42A4A7FBE2D1507B4D4B3759C23A70D052C211AF4A289398DC','swag@swag.swag','2016-11-15 06:39:58'),(7,'gautam','07BB9FCF5C104457F1B09C7CA92E4ABE87EF46AD6E10301E4BCA676658CCF406C4394EAB969C40DF695D5BBAC8332C414B8C25EB9D0B21A87EC465A2C2C1ACE2','gparanja@usc.edu','2016-11-17 07:40:06'),(8,'john','2E11705A4BEF4FD9F9E07EB9C5D5BF05C4A8458B7C8750BAB475F24C311774A5C8501BF26AAD9548BFF6D6889C77FBEA068E89C98E52B8B2A2E2E0640CD1EC95','jjohn12sm@gmail.com','2016-11-18 22:26:54');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-20  0:01:34
