package com.huhu.domain.entity;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.huhu.constants.CharacterConstants;
import com.huhu.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Java 源码结构
 *
 * @Author: wilimm
 * @Date: 2019/1/10 13:55
 */
@Data
public class PojoClass {

    public PojoClass() {
        // 默认导入的类
        importList.add("lombok.Data;");
        importList.add("java.io.Serializable");
    }

    /**
     * 包名
     */
    private String _package;

    /**
     * import 列表
     */
    private Set<String> importList = new HashSet<>();

    /**
     * 类注释
     */
    private ClassComment comment;

    /**
     * Java 类名
     */
    private String className;

    /**
     * serialVersionUID 使用默认值 1L
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字段列表
     */
    private List<Field> fieldList = new ArrayList<>();

    public void addImport(String _import) {
        importList.add(_import);
    }

    public void addField(Field field) {
        fieldList.add(field);
    }

    @Data
    @AllArgsConstructor
    public static class ClassComment {
        /**
         * 注释
         */
        private String comment;

        /**
         * 用户名
         */
        private String user;

        /**
         * 创建时间
         */
        private Date createTime;
    }
    @Data
    public static class Field {

        /**
         * 注释
         */
        private String comment;

        /**
         * 字段类型
         */
        private String type;

        /**
         * 字段名称
         */
        private String name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("package ").append(_package).append(CharacterConstants.NEW_LINE);

        result.append(CharacterConstants.NEW_LINE);

        for (String _import : importList) {
            result.append("import ").append(_import).append(CharacterConstants.NEW_LINE);
        }

        result.append(CharacterConstants.NEW_LINE);

        result.append("/**").append(CharacterConstants.NEW_LINE)
                .append(" * ").append(comment.getComment()).append(CharacterConstants.NEW_LINE)
                .append(" * ").append(CharacterConstants.NEW_LINE)
                .append(" * @Author: ").append(comment.getUser()).append(CharacterConstants.NEW_LINE)
                .append(" * @Date: ").append(DateUtils.format(comment.getCreateTime())).append(CharacterConstants.NEW_LINE)
                .append(" */").append(CharacterConstants.NEW_LINE);

        // 默认的注解
        result.append("@Data").append(CharacterConstants.NEW_LINE);

        result.append("public class ").append(className).append(" implements Serializable ")
                .append(" {").append(CharacterConstants.NEW_LINE);

        result.append(CharacterConstants.NEW_LINE);

        result.append(CharacterConstants.TAB).append("private static final long serialVersionUID = ")
                .append(serialVersionUID).append(";").append(CharacterConstants.NEW_LINE);

        for (Field field : fieldList) {
            result.append(CharacterConstants.NEW_LINE);

            result.append(CharacterConstants.TAB).append("/**").append(CharacterConstants.NEW_LINE)
                    .append(CharacterConstants.TAB).append(" * ").append(field.getComment()).append(CharacterConstants.NEW_LINE)
                    .append(CharacterConstants.TAB).append(" */").append(CharacterConstants.NEW_LINE);

            result.append(CharacterConstants.TAB).append("private ")
                    .append(field.getType()).append(" ").append(field.getName())
                    .append(";").append(CharacterConstants.NEW_LINE);
        }

        result.append("}").append(CharacterConstants.NEW_LINE);

        return result.toString();
    }
}