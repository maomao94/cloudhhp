/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.15 : Database - easyweb-shiro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`easyweb-shiro` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `easyweb-shiro`;

/*Table structure for table `sys_authorities` */

DROP TABLE IF EXISTS `sys_authorities`;

CREATE TABLE `sys_authorities` (
  `authority_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `authority_name` varchar(20) NOT NULL COMMENT '权限名称',
  `authority` varchar(40) DEFAULT NULL COMMENT '授权标识',
  `menu_url` varchar(40) DEFAULT NULL COMMENT '菜单url',
  `parent_id` int(11) NOT NULL DEFAULT '-1' COMMENT '父id,-1表示无父级',
  `is_menu` int(1) NOT NULL DEFAULT '0' COMMENT '权限类型,0菜单,1按钮',
  `order_number` int(3) NOT NULL DEFAULT '0' COMMENT '排序号',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='权限表';

/*Data for the table `sys_authorities` */

insert  into `sys_authorities`(`authority_id`,`authority_name`,`authority`,`menu_url`,`parent_id`,`is_menu`,`order_number`,`menu_icon`,`create_time`,`update_time`) values (1,'系统管理',NULL,NULL,-1,0,1,'layui-icon layui-icon-set','2018-06-29 11:05:41','2019-02-13 13:36:40'),(2,'用户管理',NULL,'system/user',1,0,2,NULL,'2018-06-29 11:05:41','2019-02-12 09:16:42'),(3,'查看用户','user:view',NULL,2,1,3,NULL,'2018-07-21 13:54:16','2019-02-12 09:17:57'),(4,'更新用户','user:update',NULL,2,1,4,NULL,'2018-06-29 11:05:41','2019-02-12 09:17:41'),(5,'角色管理',NULL,'system/role',1,0,5,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:10'),(6,'查看角色','role:view',NULL,5,1,6,NULL,'2018-07-21 13:54:59','2019-02-12 09:26:11'),(7,'更新角色','role:update',NULL,5,1,7,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:12'),(8,'权限管理',NULL,'system/authorities',1,0,8,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:13'),(9,'查看权限','authorities:view',NULL,8,1,9,NULL,'2018-07-21 13:55:57','2019-02-12 09:26:14'),(10,'更新权限','authorities:update',NULL,8,1,10,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:15'),(11,'登录日志','loginRecord:view','system/loginRecord',1,0,11,NULL,'2018-06-29 11:05:41','2019-02-12 09:26:16'),(12,'系统监控','','',-1,0,12,'layui-icon layui-icon-engine','2019-02-13 14:32:17','2019-02-13 14:36:49'),(13,'Druid监控','','druid',12,0,13,'','2019-02-13 14:36:45','2019-02-13 14:36:45'),(14,'文件管理','','file/view',1,0,12,'','2019-09-02 22:27:12','2019-09-02 22:27:12');

/*Table structure for table `sys_dictionary` */

DROP TABLE IF EXISTS `sys_dictionary`;

CREATE TABLE `sys_dictionary` (
  `dict_code` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(50) NOT NULL COMMENT '字典名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_code`,`create_time`,`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='字典';

/*Data for the table `sys_dictionary` */

insert  into `sys_dictionary`(`dict_code`,`dict_name`,`description`,`create_time`,`update_time`) values (1,'性别',NULL,'2019-02-11 08:49:43','2019-02-11 08:50:12');

/*Table structure for table `sys_dictionary_data` */

DROP TABLE IF EXISTS `sys_dictionary_data`;

CREATE TABLE `sys_dictionary_data` (
  `dictdata_code` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典项主键',
  `dict_code` int(11) NOT NULL COMMENT '字典主键',
  `dictdata_name` varchar(40) NOT NULL COMMENT '字典项值',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `sort_number` int(1) NOT NULL COMMENT '排序',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dictdata_code`),
  KEY `FK_Reference_36` (`dict_code`),
  CONSTRAINT `sys_dictionary_data_ibfk_1` FOREIGN KEY (`dict_code`) REFERENCES `sys_dictionary` (`dict_code`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='字典项';

/*Data for the table `sys_dictionary_data` */

insert  into `sys_dictionary_data`(`dictdata_code`,`dict_code`,`dictdata_name`,`is_delete`,`sort_number`,`description`,`create_time`,`update_time`) values (1,1,'男',0,0,NULL,'2019-02-11 08:50:38','2019-02-11 08:51:02'),(2,1,'女',0,1,NULL,'2019-02-11 08:50:38','2019-02-11 08:51:02');

/*Table structure for table `sys_login_record` */

DROP TABLE IF EXISTS `sys_login_record`;

CREATE TABLE `sys_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `os_name` varchar(40) DEFAULT NULL COMMENT '操作系统',
  `device` varchar(40) DEFAULT NULL COMMENT '设备名',
  `browser_type` varchar(40) DEFAULT NULL COMMENT '浏览器类型',
  `ip_address` varchar(40) DEFAULT NULL COMMENT 'ip地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_login_record_user` (`user_id`),
  CONSTRAINT `sys_login_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_login_record` */

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) NOT NULL COMMENT '角色名称',
  `comments` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`comments`,`is_delete`,`create_time`,`update_time`) values (1,'管理员','系统管理员',0,'2018-07-21 09:58:31','2019-02-12 09:28:59'),(2,'普通用户','系统普通用户',0,'2018-07-21 09:58:35','2019-02-12 09:29:07');

/*Table structure for table `sys_role_authorities` */

DROP TABLE IF EXISTS `sys_role_authorities`;

CREATE TABLE `sys_role_authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `authority_id` int(11) NOT NULL COMMENT '权限id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_role_permission_pm` (`authority_id`),
  KEY `FK_sys_role_permission_role` (`role_id`),
  CONSTRAINT `sys_role_authorities_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_authorities_ibfk_2` FOREIGN KEY (`authority_id`) REFERENCES `sys_authorities` (`authority_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';

/*Data for the table `sys_role_authorities` */

insert  into `sys_role_authorities`(`id`,`role_id`,`authority_id`,`create_time`) values (1,1,1,'2019-09-02 22:27:28'),(2,1,2,'2019-09-02 22:27:28'),(3,1,3,'2019-09-02 22:27:28'),(4,1,4,'2019-09-02 22:27:28'),(5,1,5,'2019-09-02 22:27:28'),(6,1,6,'2019-09-02 22:27:28'),(7,1,7,'2019-09-02 22:27:28'),(8,1,8,'2019-09-02 22:27:28'),(9,1,9,'2019-09-02 22:27:28'),(10,1,10,'2019-09-02 22:27:28'),(11,1,11,'2019-09-02 22:27:28'),(12,1,12,'2019-09-02 22:27:28'),(13,1,13,'2019-09-02 22:27:28'),(14,1,14,'2019-09-02 22:27:28');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nick_name` varchar(20) NOT NULL COMMENT '昵称',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `sex` varchar(1) NOT NULL DEFAULT '男' COMMENT '性别',
  `phone` varchar(12) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `email_verified` int(1) DEFAULT '0' COMMENT '邮箱是否验证,0未验证,1已验证',
  `true_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `department_id` int(11) DEFAULT NULL COMMENT '部门id',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1冻结',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_account` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`username`,`password`,`nick_name`,`avatar`,`sex`,`phone`,`email`,`email_verified`,`true_name`,`id_card`,`birthday`,`department_id`,`state`,`create_time`,`update_time`) values (1,'admin','822f69df5fff8f8983e69a515744a9df','管理员',NULL,'男',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2018-07-21 10:03:40','2019-02-12 11:45:15');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_user_role` (`user_id`),
  KEY `FK_sys_user_role_role` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`,`create_time`) values (1,1,1,'2019-02-12 14:59:57');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
