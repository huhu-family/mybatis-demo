DROP TABLE IF EXISTS `freeze_ip`;
CREATE TABLE `freeze_ip` (
   `id` bigint(19) NOT NULL AUTO_INCREMENT,
   `customer_id` bigint(19) NOT NULL DEFAULT '-1' COMMENT '用户编号',
   `ip` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'IPv4',
   `freeze_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '冻结状态, 1: 冻结，2:正常',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uniq_customer_id_ip` (`customer_id`, `ip`),
   KEY `index_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='冻结IP记录表';