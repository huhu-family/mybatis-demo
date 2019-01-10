package com.huhu.dao.common;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 11:38
 */
@Repository
public interface TableDao {

    /**
     * 返回结果示例：
     *
     * {Table=demo, Create Table=CREATE TABLE `demo` (
     *   `id` int(11) NOT NULL AUTO_INCREMENT,
     *   `name` varchar(32) NOT NULL DEFAULT '',
     *   `age` int(11) NOT NULL DEFAULT '-1',
     *   PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4}
     *
     * @param tableName
     * @return
     */
    Map<String, String> showCreateTable(@Param("tableName") String tableName);
}
