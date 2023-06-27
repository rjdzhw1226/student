-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: student_demo
-- ------------------------------------------------------
-- Server version	8.0.26

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
  `count` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

INSERT INTO `class` VALUES ('31','7','1037723',4),('32','7','1037724',2),('33','7','1037725',1),('34','7','1037726',2),('35','9','1037727',3),('36','22','1037728',3),('37','24','1037729',2);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` VALUES ('1','学生管理','1'),('2','老师管理','2');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_children`
--

INSERT INTO `menu_children` VALUES ('2','基本信息管理','student.html','1'),('3','成绩管理','score.html','1'),('4','班级管理','class.html','1'),('5','课程管理','subject.html','1'),('6','考试管理','exam.html','1'),('7','基本信息管理','teacher.html','2'),('8','考评管理','time.html','2');

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `score` int NOT NULL DEFAULT '0' COMMENT '分数',
  `subId` varchar(16) DEFAULT NULL COMMENT '课程id',
  `ownerId` varchar(16) DEFAULT NULL COMMENT '学生id',
  `station` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否有效 1有效 0无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

INSERT INTO `score` VALUES (98,'123991226','123789','1'),(61,'123157761','123789','1'),(100,'123456098','123789','1'),(20,'123157761','138675','1'),(60,'123157761','138674','1'),(80,'123157761','138673','1'),(70,'123157761','138672','1'),(55,'123157761','138678','1'),(7,'123157761','138689','1'),(97,'123157761','138612','1'),(100,'123157761','138656','1'),(80,'123157761','138679087','1'),(70,'123157761','123217','1'),(55,'123157761','1923478','1'),(7,'123157761','65768768','1'),(97,'123157761','1232319','1'),(100,'123157761','1235676534','1'),(1,'123157761','34','1');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

INSERT INTO `student` VALUES ('123789','renjiadong','五年级','二班','15850656907','18','男','1','78303e38-6ed3-4d50-b09d-36680c643584.png'),('138675','jaychou','三年级','四班','12378945754','20','男','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138674','lihongwang','三年级','四班','123743345754','20','女','1',NULL),('138673','jj','三年级','二班','1345976754','90','女','1',NULL),('138672','taozhe','三年级','一班','3456754','28','男','1',NULL),('138678','huyanbin','三年级','三班','43654676754','30','女','0',NULL),('138689','lizhiting','大四','三班','345754','30','女','1',NULL),('138612','wj','大四','三班','123345376754','10','男','1',NULL),('123217','xuzixin','三年级','三班','12356676887','22','女','1',NULL),('1923478','agnesefirst','五年级','二班','2934032840','18','男','1','6e596624-d473-4d9a-b394-b2709460a537.png'),('65768768','agnese','三年级','二班','5467547345','32','女','1','img.png'),('1232319','demide','三年级','二班','23432432','30','女','1','1006960_normal.jpg'),('1235676534','aina','大二','一班','1234245','22','女','1','aina.png'),('34','zanna','五年级','二班','145213434','20','女','1','zanna.png'),('192','betsy','三年级','二班','12367578','32','女','1',NULL);

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `id` varchar(16) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `grade` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` VALUES ('123157761','语文课',16),('123991226','数学课',16),('123456098','概率论与数理统计',22),('123456096','高等数学',21);

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
  `position` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` VALUES ('1037723','马彦龙','31','7','13807964503','4'),('1037724','晁张强','32','7','1234561','1'),('1037725','晁沛','33','7','1234562','4'),('1037726','齐雪丹','34','7','1234563','2'),('1037727','任美娇','35','9','1234564','3'),('1037728','张剑','36','22','1234565','1'),('1037729','王乐言','37','24','1234566','4');

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
  `level` int NOT NULL DEFAULT '1' COMMENT '层级',
  `number` int NOT NULL DEFAULT '0' COMMENT '子节点个数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree`
--

INSERT INTO `tree` VALUES ('10','六年级','1',2,0),('11','初一','2',2,0),('12','初二','2',2,0),('13','初三','2',2,0),('14','高一','3',2,0),('15','高二','3',2,0),('16','高三','3',2,0),('2','初中部','0',1,3),('3','高中部','0',1,3),('4','大学部','0',1,4),('5','一年级','1',2,0),('6','二年级','1',2,0),('7','三年级','1',2,1),('8','四年级','1',2,0),('9','五年级','1',2,0),('1','小学部','0',1,6),('17','本科学部','4',2,4),('18','研究生院','4',2,3),('19','博士生院','4',2,3),('20','博士后流动站','4',2,0),('21','大一','17',3,0),('22','大二','17',3,0),('23','大三','17',3,0),('24','大四','17',3,0),('25','研一','18',3,0),('26','研二','18',3,0),('27','研三','18',3,0),('28','博一','19',3,0),('29','博二','19',3,0),('30','博三','19',3,0),('31','二班','7',3,0),('32','四班','7',3,0),('33','一班','7',3,0),('34','三班','7',3,0),('35','二班','9',3,0),('36','一班','22',4,0),('37','三班','24',4,0);

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
  `password` varchar(12) DEFAULT NULL,
  `station` varchar(6) DEFAULT NULL,
  `telephone` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES ('123456','2023-06-01','男','renjiadong','991226','1','15877428085'),('1960220388858779','2023-06-06','男','liyuntao','123456','1','15850656907'),('12','2023-06-02','女','1','000000','1','12345678909');
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-27 15:50:28
