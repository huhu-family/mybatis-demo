package com.huhu.domain.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 11:58
 */
@Data
public class Table {
    /**
     * 表名
     */
    private String name;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 建表语句 DDL
     */
    private String createTableSQL;

    /**
     * customer_id 是否是唯一键
     */
    private boolean uniqueCustomerId;

    /**
     * 列集合
     */
    private List<Column> columnList = new ArrayList<>();

    public void addColumn(Column column) {
        columnList.add(column);
    }


    @Data
    public static class Column {
        /**
         * 列名
         */
        private String name;

        /**
         * 列的类型
         */
        private String type;

        /**
         * 值是否可以为 NULL
         *      true：不能为空
         *      false：可以为空
         */
        private boolean notNull = true;

        /**
         * 默认值
         */
        private String defaultValue;

        /**
         * 列注释
         */
        private String comment;
    }
}
