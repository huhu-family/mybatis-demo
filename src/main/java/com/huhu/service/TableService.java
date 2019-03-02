package com.huhu.service;

import com.huhu.dao.common.TableDao;
import com.huhu.domain.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 11:58
 */
@Service
public class TableService {
    @Autowired
    private TableDao tableDao;

    /**
     * Mysql show create table 返回建表语句的列名
     */
    private static final String CREATE_TABLE = "Create Table";


    /**
     * MySQL 返回建表语句中使用的 【行分隔符】
     *
     */
    private static final String LINE_SEPARATOR = "\n";

    public Table getTable(String tableName) {
        Table table = new Table();
        try {
            Map<String, String> tableMap = tableDao.showCreateTable(tableName);
            String createTableSql = tableMap.get(CREATE_TABLE);
            table.setCreateTableSQL(createTableSql);
            String[] lines = createTableSql.split(LINE_SEPARATOR);

            table.setName(tableName);

            for (int i = 1; i < lines.length; i ++) {
                String[] columnSQL = lines[i].trim().split("\\s+");
                // 如果到了 PRIMARY 行，说明列已经遍历完了
                if ("PRIMARY".equalsIgnoreCase(columnSQL[0])) {
                    break;
                }

                Table.Column column = new Table.Column();
                String columnName = columnSQL[0].replaceAll("`", "");
                column.setName(columnName);

                String columnType = columnSQL[1];
                if (columnType.contains("(")) {
                    columnType = columnType.substring(0, columnType.indexOf("("));
                }
                column.setType(columnType.toLowerCase());

                // 默认 id 是主键，且自增
                if ("id".equals(columnName)) {
                    column.setComment("自增主键");
                } else {
                    // columnSQL[2]=NOT
                    // columnSQL[3]=NULL
                    // columnSQL[4]=DEFAULT
                    String defalutValue = columnSQL[5];
                    column.setDefaultValue(defalutValue.replaceAll("'", ""));

                    int commentIndex = 6;
                    while(!"COMMENT".equalsIgnoreCase(columnSQL[commentIndex])) {
                        commentIndex++;
                    }
                    StringBuilder columnComment = new StringBuilder();
                    for (int j = commentIndex + 1; j < columnSQL.length; j++){
                        columnComment.append(columnSQL[j]).append(" ");
                    }
                    // 去掉前面的 ' 符号，以及后面的 ', 和空格
                    column.setComment(columnComment.substring(1, columnComment.length() - 3));
                }

                table.addColumn(column);
            }
            String lastLine = lines[lines.length - 1];
            // 去掉前面的 =' 符号，以及后面的' 符号
            String tableComment = lastLine.substring(lastLine.lastIndexOf("=") + 2, lastLine.length() - 1);
            table.setComment(tableComment);

            return table;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("doesn't exist")) {
                // TODO 自定义异常
                throw new RuntimeException(tableName + " 表不存在", e);
            }
        }
        return null;
    }
}