-- MySQL dump 10.13  Distrib 9.4.0, for Win64 (x86_64)
--
-- Host: localhost    Database: qlbanhang
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `chitiethoadon`
--

DROP TABLE IF EXISTS `chitiethoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitiethoadon` (
  `hoaDonId` bigint NOT NULL,
  `sanPhamId` int NOT NULL,
  `soLuong` int NOT NULL,
  `donGia` double NOT NULL,
  PRIMARY KEY (`hoaDonId`,`sanPhamId`),
  KEY `sanPhamId` (`sanPhamId`),
  CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`hoaDonId`) REFERENCES `hoadon` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitiethoadon`
--

LOCK TABLES `chitiethoadon` WRITE;
/*!40000 ALTER TABLE `chitiethoadon` DISABLE KEYS */;
INSERT INTO `chitiethoadon` VALUES (5,1,1,45000000),(6,4,10,10000000),(7,4,1,10000000),(8,4,80,10000000),(9,1,9,45000000),(9,4,9,10000000);
/*!40000 ALTER TABLE `chitiethoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoadon`
--

DROP TABLE IF EXISTS `hoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoadon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ngayLap` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tongTien` double DEFAULT NULL,
  `khachHangId` bigint DEFAULT NULL,
  `taiKhoanId` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `khachHangId` (`khachHangId`),
  KEY `taiKhoanId` (`taiKhoanId`),
  CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`),
  CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`taiKhoanId`) REFERENCES `taikhoan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoadon`
--

LOCK TABLES `hoadon` WRITE;
/*!40000 ALTER TABLE `hoadon` DISABLE KEYS */;
INSERT INTO `hoadon` VALUES (5,'2025-09-19 10:26:50',45000000,3,6),(6,'2025-09-19 10:28:20',100000000,3,6),(7,'2025-09-19 11:45:38',10000000,4,8),(8,'2025-09-19 11:48:07',800000000,6,8),(9,'2025-09-19 11:55:15',495000000,7,1);
/*!40000 ALTER TABLE `hoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `hoTen` varchar(100) NOT NULL,
  `soDienThoai` varchar(20) NOT NULL,
  `diaChi` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES (3,'Vũ Minh Hiếu','0967700119','Bắc Giang'),(4,'Nguyễn Đức Mạnh','0981722381','Hà Nội 2'),(5,'Nguyễn Đình Đăng Hiếu','không có','Hà Nội 2'),(6,'Putin','không có','Nga'),(7,'Đô 500','không có','Mỹ');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sanpham`
--

DROP TABLE IF EXISTS `sanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sanpham` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten` varchar(200) NOT NULL,
  `hang` varchar(100) DEFAULT NULL,
  `gia` double NOT NULL,
  `tonKho` int DEFAULT '0',
  `loai` varchar(50) DEFAULT NULL,
  `tinhTrang` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sanpham`
--

LOCK TABLES `sanpham` WRITE;
/*!40000 ALTER TABLE `sanpham` DISABLE KEYS */;
INSERT INTO `sanpham` VALUES (1,'Laptop Dell XPS 15','Dell',45000000,9999999,'Laptop','Mới'),(4,'máy tính cũ ramdom','ramdom',10000000,0,'Laptop','Mới'),(5,'Túi Mù đồ công nghệ','ramdom',1000000,999990000,'Khác','Mới');
/*!40000 ALTER TABLE `sanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenDangNhap` varchar(50) NOT NULL,
  `matKhau` varchar(100) NOT NULL,
  `vaiTro` enum('admin','nhanvien') NOT NULL DEFAULT 'nhanvien',
  `thoiGianDangNhapCuoi` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenDangNhap` (`tenDangNhap`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES (1,'admin','1','admin','2025-09-19 04:54:52'),(2,'vuminhhieu','1','admin','2025-09-19 07:15:01'),(6,'123','1','nhanvien',NULL),(7,'manhgay','1','nhanvien',NULL),(8,'Vũ Minh Hiếu','1','nhanvien','2025-09-19 07:21:18');
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-19 14:49:02
