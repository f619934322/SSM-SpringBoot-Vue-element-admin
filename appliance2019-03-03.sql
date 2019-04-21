/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : appliance

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 03/03/2019 01:47:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appliance_apply
-- ----------------------------
DROP TABLE IF EXISTS `appliance_apply`;
CREATE TABLE `appliance_apply`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教学管理用品申领表自增ID，主键',
  `Inventory_ID` int(11) NOT NULL COMMENT '教学管理用品库存表ID',
  `Item_Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物品名称',
  `Item_Count` int(11) NOT NULL COMMENT '申领数量',
  `Status` int(1) NOT NULL COMMENT '审核状态（0未通过，1驳回，2已通过，3已领取）',
  `Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `Reviewer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `Review_Time` date NULL DEFAULT NULL COMMENT '审核时间',
  `Review_Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '申领人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` int(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_apply
-- ----------------------------
INSERT INTO `appliance_apply` VALUES (1, 1, '00000', 20, 3, '测试领取', 'admin', '2019-02-28', NULL, 'SYSTEM', 'admin', '2019-01-26', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (2, 1, '测试物品1', 1, 3, 'guuu', 'admin', NULL, 'a  333', 'SYSTEM', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (3, 1, '测试物品', 90, 2, '无敌', 'admin', NULL, '哦哦哦', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (4, 1, '测试物品', 82, 2, '-82', 'admin', NULL, '-82!!!', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (5, 1, '测试物品', 2, 2, '？？', 'admin', NULL, '？', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (6, 1, '测试物品', 55, 0, '阿飞', NULL, NULL, NULL, 'admin', NULL, '2019-02-28', NULL, 0);
INSERT INTO `appliance_apply` VALUES (7, 1, '测试物品', 82, 2, '这回', 'admin', NULL, '?!2019年2月28日 03:46:32', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (8, 1, '测试物品', 35, 2, '35', 'admin', NULL, '46', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (9, 1, '测试物品', 47, 2, '22', 'admin', '2019-02-28', NULL, 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (10, 11, '测试未审核的', 55, 2, '55', 'admin', '2019-02-28', 'uu', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);
INSERT INTO `appliance_apply` VALUES (11, 1, '测试物品', 53, 2, '53', 'admin', '2019-02-28', '1', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);

-- ----------------------------
-- Table structure for appliance_demand
-- ----------------------------
DROP TABLE IF EXISTS `appliance_demand`;
CREATE TABLE `appliance_demand`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教学管理用品采购需求表自增ID，主键',
  `Inventory_ID` int(11) NULL DEFAULT NULL COMMENT '教学管理用品库存表ID\r\n',
  `Added_Flag` int(1) NOT NULL COMMENT '需求标识（补充0，新增1）',
  `Item_Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '需要采购物品名称',
  `Item_Count` int(11) NULL DEFAULT NULL COMMENT '物品数量',
  `Item_Type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物品类型',
  `Purchase_Price` decimal(10, 2) NULL DEFAULT NULL COMMENT '采购单价',
  `Status` int(1) NOT NULL COMMENT '审核与采购状态(0未审核，1驳回，2已通过，3采购失败，4采购完成)',
  `Reviewer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `Review_Time` date NULL DEFAULT NULL COMMENT '审核时间',
  `Review_Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核与采购备注',
  `Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注（第一次）',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` int(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_demand
-- ----------------------------
INSERT INTO `appliance_demand` VALUES (1, 1, 1, '测试物品', 100, 'TS', 100.00, 4, 'SYSTEM', '2019-01-26', '测试添加', '测试添加', 'SYSTEM', 'SYSTEM', '2019-01-26', '2019-01-26', 0);
INSERT INTO `appliance_demand` VALUES (2, 1, 0, '测试物品', 100, 'TS', 100.00, 4, 'SYSTEM', '2019-01-26', '测试添加', '', 'SYSTEM', 'SYSTEM', '2019-01-26', '2019-01-26', 0);
INSERT INTO `appliance_demand` VALUES (3, 2, 1, '测试物品2', 100, 'TS', 50.00, 3, 'admin', '2019-02-23', '测试添加', '测试添加', 'SYSTEM', 'admin', '2019-02-23', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (4, NULL, 0, '测试物品3', NULL, 'TS', NULL, 4, NULL, NULL, NULL, '没什么好讲', 'SYSTEM', NULL, '2019-02-25', NULL, 0);
INSERT INTO `appliance_demand` VALUES (5, NULL, 1, '测试物品4', NULL, 'TS', NULL, 4, NULL, NULL, NULL, '啊啊？', '00000', NULL, '2019-02-25', NULL, 0);
INSERT INTO `appliance_demand` VALUES (6, 7, 1, '测试物品5', NULL, 'TS', NULL, 4, NULL, NULL, NULL, '测试物品5申请', '00000', NULL, '2019-02-25', NULL, 0);
INSERT INTO `appliance_demand` VALUES (7, 2, 0, '测试物品6', NULL, 'TS', NULL, 4, NULL, NULL, NULL, '阿法狗', 'SYSTEM', NULL, '2019-02-25', NULL, 0);
INSERT INTO `appliance_demand` VALUES (8, 2, 0, '测试物品6', 0, 'TS', 0.00, 3, 'admin', NULL, NULL, '嗷嗷啊啊？', '00000', 'admin', '2019-02-25', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (9, 2, 0, '测试物品2', 244, 'TS', 23.00, 3, 'admin', NULL, NULL, '我要补充!', '00000', 'admin', '2019-02-25', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (10, 1, 0, '测试物品', 0, 'TS', 0.00, 1, 'admin', NULL, NULL, '补充补充222', '00000', 'admin', '2019-02-25', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (11, 1, 0, '测试物品', 0, 'TS', 0.00, 3, 'admin', NULL, NULL, '关掉', '00000', 'admin', '2019-02-25', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (12, 7, 0, '测试物品5', NULL, 'TS', NULL, 4, NULL, NULL, NULL, '测试补充5', '00000', NULL, '2019-02-26', NULL, 0);
INSERT INTO `appliance_demand` VALUES (13, 8, 1, '测试物品7', 552, 'TS', 996.00, 4, NULL, NULL, NULL, '测试物品7', '00000', 'admin', '2019-02-26', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (14, 1, 0, '测试物品', 102, 'TS', 667.00, 4, NULL, NULL, NULL, '嗯？', '00000', NULL, '2019-02-26', NULL, 0);
INSERT INTO `appliance_demand` VALUES (15, 8, 0, '测试物品7', 111, 'TS', 52.00, 4, NULL, NULL, NULL, '777', 'admin', NULL, '2019-02-26', NULL, 0);
INSERT INTO `appliance_demand` VALUES (16, NULL, 1, '测试物品8', 2, 'TS', 2.00, 2, 'admin', '2019-02-26', NULL, '888', 'admin', 'admin', '2019-02-26', '2019-02-26', 0);
INSERT INTO `appliance_demand` VALUES (17, 8, 0, '测试物品7', 22, 'TS', 63.00, 4, 'admin', '2019-02-26', NULL, '补充77', 'admin', NULL, '2019-02-26', NULL, 0);
INSERT INTO `appliance_demand` VALUES (18, NULL, 1, '测试物品9', 666, 'TS', 55.00, 2, 'admin', '2019-02-27', NULL, '999', 'admin', 'admin', '2019-02-27', '2019-02-27', 0);
INSERT INTO `appliance_demand` VALUES (19, 2, 0, '测试物品2', 102, 'TS', 44.00, 4, 'admin', '2019-02-27', NULL, '1232019-02-27 03:49:11', 'admin', NULL, '2019-02-27', NULL, 0);
INSERT INTO `appliance_demand` VALUES (20, 8, 0, '测试物品7', 1, 'TS', 23.00, 4, 'admin', '2019-02-27', NULL, '685+1', 'admin', NULL, '2019-02-27', NULL, 0);
INSERT INTO `appliance_demand` VALUES (21, 8, 0, '测试物品7', 1, 'TS', 1.00, 3, 'admin', '2019-02-28', NULL, '686+1', 'admin', 'admin', '2019-02-27', '2019-02-28', 0);
INSERT INTO `appliance_demand` VALUES (22, 10, 1, '新增2019-02-27 04:05:43', 1, 'TS', 1.00, 4, 'admin', '2019-02-27', '备注填入', '2019年2月27日 04:05:48', 'admin', 'admin', '2019-02-27', '2019-02-27', 0);
INSERT INTO `appliance_demand` VALUES (23, 11, 1, '测试未审核的', 555, 'TS', 16.00, 4, 'admin', '2019-02-28', 's', '嗷嗷嗷', 'admin', 'admin', '2019-02-27', '2019-02-28', 0);

-- ----------------------------
-- Table structure for appliance_inventory
-- ----------------------------
DROP TABLE IF EXISTS `appliance_inventory`;
CREATE TABLE `appliance_inventory`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教学管理用品库存表自增ID，主键',
  `Demand_ID` int(11) NOT NULL COMMENT '采购需求表ID,这里记载的是第一次（新增）的采购ID，更新的ID不需要重复写在这里',
  `Item_Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物品名称',
  `Item_Count` int(11) NOT NULL COMMENT '物品数量',
  `Item_Type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物品类型(去数据字典查)',
  `Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一次采购人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` int(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_inventory
-- ----------------------------
INSERT INTO `appliance_inventory` VALUES (1, 1, '测试物品', 100, 'TS', '测试添加', 'SYSTEM', 'admin', '2019-01-26', '2019-02-28', 0);
INSERT INTO `appliance_inventory` VALUES (2, 3, '测试物品2', 204, 'TS', '编辑012', 'SYSTEM', 'admin', '2019-02-23', '2019-02-27', 0);
INSERT INTO `appliance_inventory` VALUES (7, 6, '测试物品5', 3221, 'TS', '新增', 'admin', NULL, '2019-02-26', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (8, 13, '测试物品7', 686, 'TS', '去去去', 'admin', 'admin', '2019-02-26', '2019-02-27', 0);
INSERT INTO `appliance_inventory` VALUES (10, 22, '新增2019-02-27 04:05:43', 1, 'TS', '备注填入', 'admin', NULL, '2019-02-27', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (11, 23, '测试未审核的', 500, 'TS', 's', 'admin', 'admin', '2019-02-28', '2019-02-28', 0);

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典表自增ID，主键',
  `Key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据字典Key',
  `Value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据字典Value',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for staff_info
-- ----------------------------
DROP TABLE IF EXISTS `staff_info`;
CREATE TABLE `staff_info`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表自增ID，主键',
  `Staff_Number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '工号',
  `Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `Password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `User_Type` int(1) NOT NULL COMMENT '用户类型(1为普通教职工，2为管理人员或采购人员)',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` int(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff_info
-- ----------------------------
INSERT INTO `staff_info` VALUES (1, '00000', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 2, NULL, NULL, '2019-01-26', '2019-01-26', 0);

SET FOREIGN_KEY_CHECKS = 1;
