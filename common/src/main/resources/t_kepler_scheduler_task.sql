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

 Date: 16/03/2020 19:25:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_kepler_scheduler_task
-- ----------------------------
DROP TABLE IF EXISTS `t_kepler_scheduler_task`;
CREATE TABLE `t_kepler_scheduler_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_name` varchar(256) DEFAULT NULL,
  `job_id` int(11) DEFAULT NULL COMMENT '任务id',
  `task_state` tinyint(4) DEFAULT NULL COMMENT '任务状态,1-SUBMIT, 2-PENDING, 3-WAITING, 4-RUNNING, 5-KILL, 6-SUCCESS, 7-FAIL',
  `dag_id` int(11) DEFAULT NULL,
  `task_trigger_type` int(11) DEFAULT NULL,
  `oss_path` varchar(512) DEFAULT NULL,
  `creator_name` varchar(256) DEFAULT NULL COMMENT '任务触发人',
  `creator_email` varchar(50) DEFAULT NULL COMMENT '任务触发人邮件',
  `worker_groups` varchar(45) DEFAULT NULL COMMENT '机器组，可以指定多个',
  `worker_host` varchar(50) DEFAULT NULL COMMENT '任务执行节点',
  `worker_port` int(11) DEFAULT NULL COMMENT '执行节点的 rpc 端口',
  `max_retry_times` int(11) DEFAULT NULL,
  `retry_interval` int(11) DEFAULT NULL,
  `retry_times` tinyint(4) DEFAULT NULL COMMENT '重试次数',
  `execution_timeout` int(11) DEFAULT NULL,
  `schedule_cron` varchar(45) DEFAULT NULL COMMENT 'Cron 表达式',
  `offset_ms` int(11) DEFAULT NULL COMMENT '传给 worker 的 ds 相对于 schedule_time 偏移量',
  `parallelism` int(11) DEFAULT NULL COMMENT 'complement run parallelism',
  `source_host` varchar(50) DEFAULT NULL,
  `dependencies_json` longtext,
  `is_self_dependent` int(11) DEFAULT '0' COMMENT '是否自依赖 0: 否, 1:是',
  `job_priority` int(11) DEFAULT '1',
  `job_type` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL COMMENT '任务进程id',
  `application_id` varchar(50) DEFAULT NULL COMMENT 'yarn任务应用id',
  `schedule_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `pending_time` timestamp NULL DEFAULT NULL,
  `waiting_time` timestamp NULL DEFAULT NULL,
  `dispatched_time` timestamp NULL DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `elapse_time` int(11) DEFAULT NULL COMMENT '任务花费时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
