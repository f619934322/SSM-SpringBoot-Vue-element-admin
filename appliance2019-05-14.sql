/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : appliance

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 14/05/2019 22:23:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appliance_apply
-- ----------------------------
DROP TABLE IF EXISTS `appliance_apply`;
CREATE TABLE `appliance_apply`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教学管理用品申领表自增ID，主键',
  `Inventory_ID` int(11) NULL DEFAULT NULL COMMENT '教学管理用品库存表ID',
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
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `Inventory_Apply`(`Inventory_ID`) USING BTREE,
  CONSTRAINT `Inventory_Apply` FOREIGN KEY (`Inventory_ID`) REFERENCES `appliance_inventory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_apply
-- ----------------------------
INSERT INTO `appliance_apply` VALUES (1, 1, 'java教程', 1, 3, '需要1本java书', 'admin', '2019-05-10', '通过', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_apply` VALUES (2, 2, '罗技鼠标', 1, 3, '需要使用1个鼠标', 'admin', '2019-05-10', '通过', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_apply` VALUES (3, 10, '订书机', 1, 3, '办公需要', 'admin', '2019-05-10', '通过', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);

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
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `Inventory_Demand`(`Inventory_ID`) USING BTREE,
  CONSTRAINT `Inventory_Demand` FOREIGN KEY (`Inventory_ID`) REFERENCES `appliance_inventory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_demand
-- ----------------------------
INSERT INTO `appliance_demand` VALUES (1, 1, 1, 'java教程', 10, '书籍资料', 450.00, 4, 'admin', '2019-05-10', '采购10本备用', 'java教程需要采购', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (2, 2, 1, '罗技鼠标', 10, '电子设备', 500.00, 4, 'admin', '2019-05-10', '10个鼠标', '需要鼠标', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (3, 3, 1, '金士顿U盘', 100, '电子设备', 2300.00, 4, 'admin', '2019-05-10', '采购100支U盘', '需要U盘', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (4, 4, 1, '水性笔', 100, '文具', 100.00, 4, 'admin', '2019-05-10', '100支水性笔采购', '需要100支水性笔', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (5, 5, 1, '办公椅', 5, '桌椅柜', 250.00, 4, 'admin', '2019-05-10', '5张通过', '布置办公室5张办公椅', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (6, 6, 1, '粉笔', 20, '文具', 200.00, 4, 'admin', '2019-05-10', '20盒粉笔', '需要粉笔', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (7, 7, 1, 'A4纸', 100, '文具', 2000.00, 4, 'admin', '2019-05-10', '100包', '需要A4纸', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (8, 8, 1, '电脑显示屏', 2, '电子设备', 1500.00, 4, 'admin', '2019-05-10', '2台', '电脑显示屏缺少2台', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (9, 9, 1, '气排球', 10, '体育用具', 100.00, 4, 'admin', '2019-05-10', '10个气排球', '校运会用10个', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (10, 10, 1, '订书机', 2, '文具', 30.00, 4, 'admin', '2019-05-10', '2个订书机', '缺少订书机', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (11, 11, 1, 'TypeC充电线', 2, '电子设备', 20.00, 4, 'admin', '2019-05-10', '充电线2根', '充电线2根', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_demand` VALUES (12, 8, 0, '电脑显示屏', 1, '电子设备', 750.00, 4, 'admin', '2019-05-10', '采购1台', '损坏一台需要补充1台', 'admin', NULL, '2019-05-10', NULL, 0);

-- ----------------------------
-- Table structure for appliance_inventory
-- ----------------------------
DROP TABLE IF EXISTS `appliance_inventory`;
CREATE TABLE `appliance_inventory`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教学管理用品库存表自增ID，主键',
  `Demand_ID` int(11) NULL DEFAULT NULL COMMENT '采购需求表ID,这里记载的是第一次（新增）的采购ID，更新的ID不需要重复写在这里',
  `Item_Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物品名称',
  `Item_Count` int(11) NOT NULL COMMENT '物品数量',
  `Item_Type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物品类型(去数据字典查)',
  `Commit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一次采购人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` int(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `Demand_Inventory`(`Demand_ID`) USING BTREE,
  CONSTRAINT `Demand_Inventory` FOREIGN KEY (`Demand_ID`) REFERENCES `appliance_demand` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appliance_inventory
-- ----------------------------
INSERT INTO `appliance_inventory` VALUES (1, 1, 'java教程', 9, '书籍资料', '采购10本备用', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_inventory` VALUES (2, 2, '罗技鼠标', 9, '电子设备', '10个鼠标', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_inventory` VALUES (3, 3, '金士顿U盘', 100, '电子设备', '采购100支U盘', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (4, 4, '水性笔', 100, '文具', '100支水性笔采购', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (5, 5, '办公椅', 5, '桌椅柜', '5张通过', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (6, 6, '粉笔', 20, '文具', '20盒粉笔', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (7, 7, 'A4纸', 100, '文具', '100包', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (8, 8, '电脑显示屏', 3, '电子设备', '2台', 'admin', '00000', '2019-05-10', '2019-05-14', 0);
INSERT INTO `appliance_inventory` VALUES (9, 9, '气排球', 10, '体育用具', '10个气排球', 'admin', NULL, '2019-05-10', NULL, 0);
INSERT INTO `appliance_inventory` VALUES (10, 10, '订书机', 1, '文具', '2个订书机', 'admin', 'admin', '2019-05-10', '2019-05-10', 0);
INSERT INTO `appliance_inventory` VALUES (11, 11, 'TypeC充电线', 2, '电子设备', '充电线2根', 'admin', NULL, '2019-05-10', NULL, 0);

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典表自增ID，主键',
  `Key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据字典Key',
  `Value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据字典Value',
  `Label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据字典Label',
  `Creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `Updator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人(第一次和创建人相同)',
  `Create_Time` date NULL DEFAULT NULL COMMENT '创建时间',
  `Update_Time` date NULL DEFAULT NULL COMMENT '更新时间(第一次和创建时间相同)',
  `Delete_Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff_info
-- ----------------------------
INSERT INTO `staff_info` VALUES (1, '00000', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 2, 'SYSTEM', NULL, '2019-05-10', NULL, 0);
INSERT INTO `staff_info` VALUES (2, '00001', 'normal', 'e10adc3949ba59abbe56e057f20f883e', 1, 'admin', NULL, '2019-05-10', NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
