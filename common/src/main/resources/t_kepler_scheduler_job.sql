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

 Date: 16/03/2020 19:25:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_kepler_scheduler_job
-- ----------------------------
DROP TABLE IF EXISTS `t_kepler_scheduler_job`;
CREATE TABLE `t_kepler_scheduler_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(512) DEFAULT NULL COMMENT '任务名称',
  `job_type` tinyint(4) DEFAULT NULL COMMENT '任务类型，1-sql,2-spark,3-shell',
  `job_configuration` longtext COMMENT '任务配置',
  `job_priority` int(4) DEFAULT NULL COMMENT '任务优先级',
  `job_cycle` int(11) DEFAULT NULL COMMENT '调度周期',
  `creator_id` varchar(50) DEFAULT NULL COMMENT '任务创建人',
  `creator_name` varchar(256) DEFAULT NULL COMMENT '任务创建人Name',
  `owner_ids` varchar(512) DEFAULT NULL COMMENT '任务ownerID',
  `owner_names` varchar(256) DEFAULT NULL COMMENT '任务ownerName',
  `alert_users` varchar(512) DEFAULT NULL COMMENT '任务告警人',
  `alert_ids` varchar(128) DEFAULT NULL COMMENT '任务告警类型 1-sms,2-wechat,3-email',
  `schedule_cron` varchar(50) DEFAULT NULL COMMENT '任务执行表达式',
  `offset_ms` int(11) DEFAULT NULL COMMENT '传给 worker 的 ds 相对于 schedule_time 偏移量',
  `config` varchar(256) DEFAULT NULL COMMENT 'corn配置',
  `is_self_dependent` tinyint(1) DEFAULT NULL COMMENT '是否自依赖',
  `max_retry_times` int(4) DEFAULT NULL COMMENT '任务重试次数',
  `retry_interval` bigint(10) DEFAULT NULL COMMENT '重试间隔',
  `execution_timeout` int(10) DEFAULT '0' COMMENT '执行超时时间',
  `worker_groups` varchar(45) DEFAULT NULL COMMENT '机器组，可以指定多个',
  `job_release_state` tinyint(4) DEFAULT '-1' COMMENT '任务状态，-1-草稿，1-上线，0-下线 -2删除',
  `description` varchar(512) DEFAULT NULL COMMENT '任务描述',
  `business_line` bigint(11) DEFAULT NULL COMMENT '任务所属业务线',
  `version` varchar(128) DEFAULT NULL COMMENT '版本id',
  `oss_path` varchar(512) DEFAULT NULL COMMENT '文件地址',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '首次启动时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT =0  DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
