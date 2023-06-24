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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_children`
--

LOCK TABLES `menu_children` WRITE;
/*!40000 ALTER TABLE `menu_children` DISABLE KEYS */;
INSERT INTO `menu_children` VALUES ('2','基本信息管理','student.html','1'),('3','成绩管理','score.html','1'),('4','班级管理','class.html','1'),('5','课程管理','subject.html','1'),('6','考试管理','exam.html','1'),('7','基本信息管理','teacher.html','2'),('8','考评管理','time.html','2');
/*!40000 ALTER TABLE `menu_children` ENABLE KEYS */;
UNLOCK TABLES;

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

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (98,'123991226','123789','1'),(61,'123157761','123789','1'),(100,'123456098','123789','1');
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('123789','renjiadong','五年级','二班','15850656907','18','男','1','78303e38-6ed3-4d50-b09d-36680c643584.png'),('138675','jaychou','三年级','六班','12378945754','20','男','1','c30c2e00-dbad-42ca-8d3e-8ecdcaa6c5b5.jpg'),('138674','王力宏','三年级','四班','123743345754','20','女','1',NULL),('138673','林俊杰','三年级','二班','1345976754','90','女','1',NULL),('138672','陶喆','三年级','一班','3456754','28','男','1',NULL),('138678','胡彦斌','三年级','三班','43654676754','30','女','1',NULL),('138689','李治廷','大五','三班','345754','30','女','1',NULL),('138612','王杰','大四','三班','123345376754','10','男','1',NULL),('138656','张三丰','大三','三班','1237854654','234','女','1',NULL),('138679087','金庸','大二','一班','456456754','10','男','1',NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
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
  `grade` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES ('123157761','语文课',12),('123991226','数学课',12),('123456098','概率论与数理统计',14),('123456096','高等数学',13);
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
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
  `password` varchar(12) DEFAULT NULL,
  `station` varchar(6) DEFAULT NULL,
  `telephone` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123456','2023-06-01','男','renjiadong','991226','1','15877428085'),('1960220388858779','2023-06-06','男','liyuntao','123456','1','15850656907'),('12','2023-06-02','女','1','000000','1','12345678909');
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

-- Dump completed on 2023-06-24 19:56:17
