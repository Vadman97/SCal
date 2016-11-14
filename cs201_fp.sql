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
USE cs201_fp;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Discussions`
--

LOCK TABLES `Discussions` WRITE;
/*!40000 ALTER TABLE `Discussions` DISABLE KEYS */;
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
  KEY `FK_EnrolledClasses_Users` (`user_id`),
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
/*!40000 ALTER TABLE `EnrolledClasses` ENABLE KEYS */;
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
  KEY `FK_EventRelationships_Users` (`user_id`),
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
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(255) DEFAULT NULL,
  `color` enum('red','orange','yellow','green','blue','indigo','violet','cyan','magenta') NOT NULL DEFAULT 'blue',
  `notify` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Events`
--

LOCK TABLES `Events` WRITE;
/*!40000 ALTER TABLE `Events` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Exams`
--

LOCK TABLES `Exams` WRITE;
/*!40000 ALTER TABLE `Exams` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Labs`
--

LOCK TABLES `Labs` WRITE;
/*!40000 ALTER TABLE `Labs` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notifications`
--

LOCK TABLES `Notifications` WRITE;
/*!40000 ALTER TABLE `Notifications` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USCClasses`
--

LOCK TABLES `USCClasses` WRITE;
/*!40000 ALTER TABLE `USCClasses` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (2,'test','537937FF92398C4454ECDE0E0DCFEB74CDAA780C776589BC31F59290235F36B920D733D59A6F87AA57051E999D4C514CFBC4A680C0DF2711BFA89B0DFAFC154B','swag@swag.com','2016-11-13 03:31:06'),(3,'vadim','0D2F44BE8166D5812502A6A908E2E695A5686DE9300B93BC2AF415915F80A5A61F342DB9089B85FE1F2F9F96130BC1056E5AF51EB802BC14C206632C2A4B65BC','vkorolik@gmail.com','2016-11-13 03:51:12');
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

-- Dump completed on 2016-11-13 21:51:26
