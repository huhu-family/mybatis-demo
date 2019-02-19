package com.huhu.utils;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;

import java.util.Date;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 13:53
 */
public class TableUtils {

    /**
     * DO 所在的包
     */
    private static final String DO_PACKAGE = "com.huhu.domain.entity";

    /**
     * DO 的后缀
     */
    private static final String DO_SUFFIX = "DO";

    /**
     * 用户名
     */
    private static final String USER_NAME = "wilimm";

    public static PojoClass tableToPojo(Table table) {
        PojoClass pojoClass = new PojoClass();
        pojoClass.set_package(DO_PACKAGE);
        pojoClass.setClassName(underlineToBigCamelCase(table.getName()) + DO_SUFFIX);

        PojoClass.ClassComment classComment = new PojoClass.ClassComment(table.getComment(), USER_NAME, new Date());
        pojoClass.setComment(classComment);

        for (Table.Column column : table.getColumnList()) {
            PojoClass.Field field = new PojoClass.Field();
            field.setName(underlineToLittleCamelCase(column.getName()));
            field.setComment(column.getComment());
            field.setType(mysqlType2JavaType(column.getType()));
            pojoClass.addField(field);

            if ("Date".equals(field.getType())) {
                pojoClass.addImport("java.util.Date;");
            }
        }

        System.out.println(table);
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
                javaType = "Date";
                break;
            case "char":
            case "varchar":
                javaType = "String";
                break;
            case "decimal":
                javaType = "BigDecimal";
                break;
        }
        return javaType;
    }

}
