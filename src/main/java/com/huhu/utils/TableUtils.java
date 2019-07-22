package com.huhu.utils;

import com.huhu.constants.CommonConstants;
import com.huhu.constants.StringConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;

import java.util.Date;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 13:53
 */
public class TableUtils {

    /**
     * 通过表名获取子包，比如 customer_current_location，则子包为 customer
     * @param tableName
     * @return
     */
    public static String firstSubPackage(String tableName) {
        for (int i = 1; i < tableName.length(); i ++) {
            if (tableName.charAt(i) == '_') {
                return tableName.substring(0, i).toLowerCase();
            }
        }
        return StringConstants.EMPTY;
    }

    public static PojoClass generatePojo(Table table, String doPackage) {
        String packageName = doPackage + "." + firstSubPackage(table.getName());

        PojoClass pojoClass = new PojoClass();
        pojoClass.set_package(packageName);
        pojoClass.setClassName(underlineToBigCamelCase(table.getName()));
        pojoClass.setTable(table);


        PojoClass.ClassComment classComment = new PojoClass.ClassComment(table.getComment(), CommonConstants.USER_NAME, new Date());
        pojoClass.setComment(classComment);

        for (Table.Column column : table.getColumnList()) {
            PojoClass.Field field = new PojoClass.Field();
            field.setName(underlineToLittleCamelCase(column.getName()));
            field.setColumnName(column.getName());
            field.setComment(column.getComment());
            field.setType(mysqlType2JavaType(column.getType()));
            pojoClass.addField(field);

            if ("Date".equals(field.getType())) {
                pojoClass.addImport("java.util.Date;");
            } else if ("BigDecimal".equals(field.getType())) {
                pojoClass.addImport("java.math.BigDecimal;");
            }
        }

        //System.out.println(table);
        return pojoClass;
    }

    /**
     * 下划线转换为大驼峰
     * @param token
     * @return
     */
    private static String underlineToBigCamelCase(String token) {
        StringBuilder result = new StringBuilder();
        String a[] = token.split("_");
        for (String s : a) {
            result.append(s.substring(0, 1).toUpperCase());
            result.append(s.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 下划线转换为小驼峰
     * @param token
     * @return
     */
    private static String underlineToLittleCamelCase(String token) {
        String bigCameCase = underlineToBigCamelCase(token);
        return bigCameCase.substring(0, 1).toLowerCase() + bigCameCase.substring(1);
    }


    /**
     * MySQL type 转为 JavaType
     * 
     * @param mysqlType
     * @return
     */
    private static String mysqlType2JavaType(String mysqlType) {
        String javaType = null;
        switch (mysqlType) {
            case "bigint":
               javaType = "Long";
               break;
            case "int":
            case "tinyint":
                javaType = "Integer";
                break;
            case "datetime":
            case "date":
            case "timestamp":
                javaType = "Date";
                break;
            case "char":
            case "varchar":
            case "blob":
                javaType = "String";
                break;
            case "decimal":
                javaType = "BigDecimal";
                break;
        }
        return javaType;
    }

}
