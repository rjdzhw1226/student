-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 192.168.0.110    Database: student_demo
-- ------------------------------------------------------
-- Server version	5.7.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `id` varchar(16) DEFAULT NULL,
  `father_id` varchar(16) DEFAULT NULL,
  `teacher_id` varchar(16) DEFAULT NULL,
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` VALUES ('31','7','1037723',4),('32','7','1037724',2),('33','7','1037725',1),('34','7','1037726',2),('35','9','1037727',3),('36','22','1037728',3),('37','24','1037729',2);
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `friend_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (1,'123456','1960220388858779'),(2,'123456','12'),(3,'123456','6327077362849781'),(4,'123456','6071087473075326'),(5,'123456','9896493248225895'),(6,'123456','3914442883713273'),(7,'123456','8135027218201925'),(8,'123456','6423897125250249'),(9,'1960220388858779','12'),(10,'1960220388858779','6327077362849781'),(11,'1960220388858779','6071087473075326'),(12,'1960220388858779','9896493248225895'),(13,'1960220388858779','3914442883713273'),(14,'1960220388858779','8135027218201925'),(15,'1960220388858779','6423897125250249'),(16,'1960220388858779','123456'),(17,'12','6327077362849781'),(18,'12','6071087473075326'),(19,'12','9896493248225895'),(20,'12','3914442883713273'),(21,'12','8135027218201925'),(22,'12','6423897125250249'),(23,'12','1960220388858779'),(24,'12','123456'),(25,'6327077362849781','12'),(26,'6071087473075326','12'),(27,'9896493248225895','12'),(28,'3914442883713273','12'),(29,'8135027218201925','12'),(30,'6423897125250249','12');
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id` varchar(16) NOT NULL,
  `title` varchar(64) NOT NULL,
  `fatherItem_id` varchar(16) DEFAULT NULL,
  `text_id` varchar(16) DEFAULT NULL,
  `type` varchar(12) DEFAULT NULL,
  `sign` varchar(2) DEFAULT NULL,
  `father_id` varchar(16) DEFAULT NULL COMMENT '对应的问卷id'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES ('0','1',NULL,'0','radio',NULL,'1141FVY34Y573F7W'),('1','1','1','1','matrix',NULL,'1141FVY34Y573F7W'),('2','12334214','2','2','checkbox',NULL,'1141FVY34Y573F7W');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` varchar(12) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `children` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES ('1','学生管理','1'),('2','老师管理','2');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_children`
--

DROP TABLE IF EXISTS `menu_children`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_children` (
  `id` varchar(12) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `url` varchar(32) DEFAULT NULL,
  `id_connect` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_children`
--

LOCK TABLES `menu_children` WRITE;
/*!40000 ALTER TABLE `menu_children` DISABLE KEYS */;
INSERT INTO `menu_children` VALUES ('2','基本信息管理','student.html','1'),('3','成绩管理','score.html','1'),('4','班级管理','class.html','1'),('5','课程管理','subject.html','1'),('6','考试管理','exam.html','1'),('7','基本信息管理','teacher.html','2'),('8','考评管理','time.html','2'),('9','位置管理','map.html','1'),('10','抢课管理','test.html','2'),('11','文件识别','file.html','1'),('12','文件管理','dir.html','2'),('13','文档生成','person.html','2'),('14','地图重生','worldNew.html','2'),('15','在线聊天','wechat.html','2');
/*!40000 ALTER TABLE `menu_children` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `middle`
--

DROP TABLE IF EXISTS `middle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `middle` (
  `id` varchar(16) NOT NULL,
  `student_id` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `middle`
--

LOCK TABLES `middle` WRITE;
/*!40000 ALTER TABLE `middle` DISABLE KEYS */;
INSERT INTO `middle` VALUES ('C481098E5KQ7LG26','123789'),('FDQ1Z06A1ZR977V4','138674'),('1141FVY34Y573F7W','138673');
/*!40000 ALTER TABLE `middle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem`
--

DROP TABLE IF EXISTS `problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem` (
  `id` varchar(16) NOT NULL,
  `time` datetime NOT NULL,
  `name` varchar(64) NOT NULL,
  `middle_id` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem`
--

LOCK TABLES `problem` WRITE;
/*!40000 ALTER TABLE `problem` DISABLE KEYS */;
INSERT INTO `problem` VALUES ('1141FVY34Y573F7W','2023-08-10 19:09:13','1','DNY3K8I2L7SZUILY');
/*!40000 ALTER TABLE `problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '分数',
  `subId` varchar(16) DEFAULT NULL COMMENT '课程id',
  `ownerId` varchar(16) DEFAULT NULL COMMENT '学生id',
  `station` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否有效 1有效 0无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (98,'123991226','123789','1'),(61,'123157761','123789','1'),(100,'123456098','123789','1'),(20,'123157761','138675','1'),(60,'123157761','138674','1'),(80,'123157761','138673','1'),(70,'123157761','138672','1'),(55,'123157761','138678','1'),(7,'123157761','138689','1'),(97,'123157761','138612','1'),(100,'123157761','138656','1'),(80,'123157761','138679087','1'),(70,'123157761','123217','1'),(55,'123157761','1923478','1'),(7,'123157761','65768768','1'),(97,'123157761','1232319','1'),(100,'123157761','1235676534','1'),(1,'123157761','34','1');
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sign`
--

DROP TABLE IF EXISTS `sign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sign` (
  `username` varchar(12) CHARACTER SET utf8 DEFAULT NULL,
  `date` date DEFAULT NULL,
  `content` varchar(16) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sign`
--

LOCK TABLES `sign` WRITE;
/*!40000 ALTER TABLE `sign` DISABLE KEYS */;
INSERT INTO `sign` VALUES ('1','2023-08-02','已签到'),('1','2023-10-09','已签到'),('1','2023-10-09','已签到'),('','2023-09-25','已签到'),('','2023-08-29','已签到'),('','2023-08-02','已签到'),('','2023-09-29','已签到'),('','2023-08-31','已签到');
/*!40000 ALTER TABLE `sign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` varchar(16) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `grade` varchar(6) DEFAULT NULL,
  `grade_class` varchar(6) DEFAULT NULL,
  `phone` varchar(12) DEFAULT NULL,
  `age` varchar(6) DEFAULT NULL,
  `gender` varchar(6) DEFAULT NULL,
  `station` varchar(1) NOT NULL DEFAULT '1',
  `url` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('123789','renjiadong','五年级','二班','15850656907','18','男','0','78303e38-6ed3-4d50-b09d-36680c643584.png'),('138675','jaychou','三年级','四班','12378945754','20','男','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138674','lihongwang','三年级','四班','123743345754','20','女','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138673','jj','三年级','二班','1345976754','90','女','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138672','taozhe','三年级','一班','3456754','28','男','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138678','huyanbin','三年级','三班','43654676754','30','女','0','test.jpg'),('138689','lizhiting','大四','三班','345754','30','女','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138612','wj','大四','三班','123345376754','10','男','1','test.jpg'),('123217','xuzixin','三年级','三班','12356676887','22','女','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('1923478','agnesefirst','五年级','二班','2934032840','18','男','1','6e596624-d473-4d9a-b394-b2709460a537.png'),('65768768','agnese','三年级','二班','5467547345','32','女','1','img.png'),('1232319','demide','三年级','二班','23432432','30','女','1','1006960_normal.jpg'),('1235676534','aina','大二','一班','1234245','22','女','1','aina.png'),('34','zanna','五年级','二班','145213434','20','女','1','zanna.png'),('192','betsy','三年级','二班','12367578','32','女','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studentTest`
--

DROP TABLE IF EXISTS `studentTest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studentTest` (
  `id` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `grade` varchar(128) DEFAULT NULL,
  `grade_class` varchar(128) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `age` varchar(128) DEFAULT NULL,
  `gender` varchar(128) DEFAULT NULL,
  `station` varchar(128) DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studentTest`
--

LOCK TABLES `studentTest` WRITE;
/*!40000 ALTER TABLE `studentTest` DISABLE KEYS */;
/*!40000 ALTER TABLE `studentTest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studenttest`
--

DROP TABLE IF EXISTS `studenttest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studenttest` (
  `id` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `grade` varchar(128) DEFAULT NULL,
  `grade_class` varchar(128) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `age` varchar(128) DEFAULT NULL,
  `gender` varchar(128) DEFAULT NULL,
  `station` varchar(128) DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studenttest`
--

LOCK TABLES `studenttest` WRITE;
/*!40000 ALTER TABLE `studenttest` DISABLE KEYS */;
/*!40000 ALTER TABLE `studenttest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `id` varchar(16) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `grade_max` int(11) NOT NULL,
  `grade_min` int(11) NOT NULL,
  `teacher_id` varchar(16) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES ('123157761','语文课',16,5,'1037723',100),('123991226','数学课',16,5,'1037724',5),('123456098','概率论与数理统计',22,22,'1037725',10),('123456096','高等数学',21,21,'1037726',2),('123456097','体育课',23,5,'1037727',10),('123987231','信号与系统',22,22,'1037728',25),('356789076','英语课',23,7,'1037729',15);
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submiddle`
--

DROP TABLE IF EXISTS `submiddle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submiddle` (
  `subId` varchar(16) NOT NULL,
  `stuName` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submiddle`
--

LOCK TABLES `submiddle` WRITE;
/*!40000 ALTER TABLE `submiddle` DISABLE KEYS */;
/*!40000 ALTER TABLE `submiddle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id` varchar(16) DEFAULT NULL,
  `name` varchar(16) DEFAULT NULL,
  `grade_id` varchar(16) DEFAULT NULL,
  `class_id` varchar(16) DEFAULT NULL,
  `phone` varchar(12) DEFAULT NULL,
  `position` varchar(16) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('1037723','马彦龙','31','7','13807964503','4',0),('1037724','晁张强','32','7','1234561','1',0),('1037725','晁沛','33','7','1234562','4',0),('1037726','齐雪丹','34','7','1234563','2',0),('1037727','任美娇','35','9','1234564','3',0),('1037728','张剑','36','22','1234565','1',0),('1037729','王乐言','37','24','1234566','4',0);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text`
--

DROP TABLE IF EXISTS `text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `text` (
  `id` varchar(16) NOT NULL,
  `Tikey` varchar(128) NOT NULL,
  `TiValue` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text`
--

LOCK TABLES `text` WRITE;
/*!40000 ALTER TABLE `text` DISABLE KEYS */;
INSERT INTO `text` VALUES ('0','0','13'),('0','1','123'),('1','0','12'),('2','0','1'),('2','1','2'),('2','2','3');
/*!40000 ALTER TABLE `text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree`
--

DROP TABLE IF EXISTS `tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tree` (
  `id` varchar(16) NOT NULL COMMENT 'id',
  `label` varchar(32) DEFAULT NULL COMMENT '描述',
  `father_id` varchar(16) DEFAULT NULL COMMENT '父id',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '层级',
  `number` int(11) NOT NULL DEFAULT '0' COMMENT '子节点个数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree`
--

LOCK TABLES `tree` WRITE;
/*!40000 ALTER TABLE `tree` DISABLE KEYS */;
INSERT INTO `tree` VALUES ('10','六年级','1',2,0),('11','初一','2',2,0),('12','初二','2',2,0),('13','初三','2',2,0),('14','高一','3',2,0),('15','高二','3',2,0),('16','高三','3',2,0),('2','初中部','0',1,3),('3','高中部','0',1,3),('4','大学部','0',1,4),('5','一年级','1',2,0),('6','二年级','1',2,0),('7','三年级','1',2,1),('8','四年级','1',2,0),('9','五年级','1',2,0),('1','小学部','0',1,6),('17','本科学部','4',2,4),('18','研究生院','4',2,3),('19','博士生院','4',2,3),('20','博士后流动站','4',2,0),('21','大一','17',3,0),('22','大二','17',3,0),('23','大三','17',3,0),('24','大四','17',3,0),('25','研一','18',3,0),('26','研二','18',3,0),('27','研三','18',3,0),('28','博一','19',3,0),('29','博二','19',3,0),('30','博三','19',3,0),('31','二班','7',3,0),('32','四班','7',3,0),('33','一班','7',3,0),('34','三班','7',3,0),('35','二班','9',3,0),('36','一班','22',4,0),('37','三班','24',4,0);
/*!40000 ALTER TABLE `tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(16) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` varchar(6) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `station` varchar(6) DEFAULT NULL,
  `telephone` varchar(12) DEFAULT NULL,
  `image` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123456','2023-06-01','男','renjiadong','$2a$10$M.sFJsweXHWeBytp4YKq0eaMpNrXWGTsNVGeNgZlGaX6d/JZMbUh2','1','15877428085','headImg.jpg'),('1960220388858779','2023-06-06','男','liyuntao','123456','1','15850656907','user2.jpg'),('12','2023-06-02','女','1','$2a$10$M.sFJsweXHWeBytp4YKq0eaMpNrXWGTsNVGeNgZlGaX6d/JZMbUh2','1','12345678909','user3.jpg'),('6327077362849781','2023-08-09','男','Conn','$2a$10$M.sFJsweXHWeBytp4YKq0eaMpNrXWGTsNVGeNgZlGaX6d/JZMbUh2',NULL,'13379476990','user4.jpg'),('6071087473075326','2023-08-09','男','renjiadong','$2a$10$YkXma8.dtf8YmQlXYpVFnOEB.TSvHw492LfMpT8tmfse0oXqG.pdC','1','15877428085','user5.jpg'),('9896493248225895','2023-08-29','女','xiongli','$2a$10$M.sFJsweXHWeBytp4YKq0eaMpNrXWGTsNVGeNgZlGaX6d/JZMbUh2','1','15829495510','user6.jpg'),('3914442883713273','2023-08-15','男','lyt','$2a$10$YkXma8.dtf8YmQlXYpVFnOEB.TSvHw492LfMpT8tmfse0oXqG.pdC',NULL,'13911112222',NULL),('8135027218201925','2023-11-02','男','rjdfff','$2a$10$lMQEI2h.3OUSXCdW8SK9iePLfZXASxOovBRyTHechNumZOeU7D3i6','1','15877428085',NULL),('6423897125250249','2015-11-09','女','xgli','$2a$10$u06Q2Ylmi.4lfu93fE6duep8AlmZpA3gQ8x2f4Aa33KrZyFK4XRIi',NULL,'15829495510',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-24  3:49:08
