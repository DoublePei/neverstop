/*
 Navicat Premium Data Transfer

 Source Server         : AAAA
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : 47.93.254.72:3306
 Source Schema         : kepler

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 16/03/2020 19:25:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_kepler_scheduler_node
-- ----------------------------
DROP TABLE IF EXISTS `t_kepler_scheduler_node`;
CREATE TABLE `t_kepler_scheduler_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_type` tinyint(4) DEFAULT NULL COMMENT '节点类型，1-master,2-worker',
  `node_state` tinyint(4) DEFAULT NULL COMMENT '节点状态，1-added,2-removed',
  `node_groups` varchar(50) DEFAULT NULL COMMENT '节点组',
  `node_host` varchar(50) DEFAULT NULL COMMENT '机器  ip',
  `node_rpc_port` int(4) DEFAULT NULL COMMENT '节点端口',
  `cpu_usage` double(5,2) DEFAULT NULL COMMENT 'cpu使用率',
  `memory_usage` double(5,2) DEFAULT NULL COMMENT '内存使用率',
  `max_tasks` int(11) DEFAULT NULL COMMENT '最大可运行的任务数',
  `running_tasks` int(11) DEFAULT NULL COMMENT '正在运行的任务数',
  `last_heartbeat_time` datetime DEFAULT NULL COMMENT '节点心跳时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
