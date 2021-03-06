package com.huhu.domain.entity;

import com.huhu.constants.StringConstants;
import com.huhu.constants.CommonConstants;
import com.huhu.utils.DateUtils;
import com.huhu.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Java 源码结构
 *
 * @Author: wilimm
 * @Date: 2019/1/10 13:55
 */
@Data
public class PojoClass {

    /**
     * DO 的后缀
     */
    private static final String DO_SUFFIX = "DO";

    /**
     * DTO 的后缀
     */
    private static final String DTO_SUFFIX = "DTO";

    public PojoClass() {
        this(DO_SUFFIX);
    }

    public PojoClass(String classNameSuffix) {
        // 默认导入的类
        importList.add("lombok.Data;");
        importList.add("java.io.Serializable;");

        this.classNameSuffix = classNameSuffix;
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
     * Java DO 后缀
     */
    private String classNameSuffix;

    /**
     * POJO 对应的数据库表
     */
    private Table table;

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

    public String fullClassName() {
        return _package + "." + className + classNameSuffix;
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

        /**
         * 表中的字段名称
         */
        private String columnName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("package ").append(_package).append(";");

        result.append(StringConstants.NEW_LINE);
        result.append(StringConstants.NEW_LINE);

        for (String _import : importList) {
            result.append("import ").append(_import).append(StringConstants.NEW_LINE);
        }

        result.append(StringConstants.NEW_LINE);

        result.append("/**").append(StringConstants.NEW_LINE)
                .append(" * ").append(comment.getComment()).append(StringConstants.NEW_LINE)
                .append(" * ").append(StringConstants.NEW_LINE)
                .append(" * @Author: ").append(comment.getUser()).append(StringConstants.NEW_LINE)
                .append(" * @Date: ").append(DateUtils.format(comment.getCreateTime())).append(StringConstants.NEW_LINE)
                .append(" */").append(StringConstants.NEW_LINE);

        // 默认的注解
        result.append("@Data").append(StringConstants.NEW_LINE);

        result.append("public class ").append(className).append(classNameSuffix).append(" implements Serializable ")
                .append(" {").append(StringConstants.NEW_LINE);

        result.append(StringConstants.NEW_LINE);

        result.append(StringConstants.TAB).append("private static final long serialVersionUID = ")
                .append(serialVersionUID).append(";").append(StringConstants.NEW_LINE);

        for (Field field : fieldList) {
            result.append(StringConstants.NEW_LINE);

            result.append(StringConstants.TAB).append("/**").append(StringConstants.NEW_LINE)
                    .append(StringConstants.TAB).append(" * ");

            if (Objects.isNull(field.getComment())) {
                result.append(field.getName());
            } else {
                result.append(field.getComment());
            }

            result.append(StringConstants.NEW_LINE)
                    .append(StringConstants.TAB).append(" */").append(StringConstants.NEW_LINE);

            result.append(StringConstants.TAB).append("private ")
                    .append(field.getType()).append(" ").append(field.getName())
                    .append(";").append(StringConstants.NEW_LINE);
        }

        result.append("}").append(StringConstants.NEW_LINE);

        return result.toString();
    }

    public String toDTOString() {
        StringBuilder result = new StringBuilder();
        result.append("package ").append(_package).append(";");

        result.append(StringConstants.NEW_LINE);
        result.append(StringConstants.NEW_LINE);

        result.append("import lombok.Data;").append(StringConstants.NEW_LINE);
        result.append("import io.swagger.annotations.ApiModelProperty;").append(StringConstants.NEW_LINE);
        result.append("import java.util.Date;").append(StringConstants.NEW_LINE);



        result.append(StringConstants.NEW_LINE);

        result.append("/**").append(StringConstants.NEW_LINE)
                .append(" * @Author: ").append(comment.getUser()).append(StringConstants.NEW_LINE)
                .append(" * @Date: ").append(DateUtils.format(comment.getCreateTime())).append(StringConstants.NEW_LINE)
                .append(" */").append(StringConstants.NEW_LINE);

        // 默认的注解
        result.append("@Data").append(StringConstants.NEW_LINE);

        result.append("public class ").append(className).append(DTO_SUFFIX)
                .append(" {").append(StringConstants.NEW_LINE);

        for (Field field : fieldList) {
            result.append(StringConstants.NEW_LINE);

            result.append(StringConstants.TAB)
                    .append("@ApiModelProperty(")
                    .append("\"").append(field.getComment()).append("\"")
                    .append(")")
                    .append(StringConstants.NEW_LINE);

            result.append(StringConstants.TAB).append("private ")
                    .append(field.getType()).append(" ").append(field.getName())
                    .append(";").append(StringConstants.NEW_LINE);
        }

        result.append("}").append(StringConstants.NEW_LINE);

        return result.toString();
    }

    public void writeToFile(FileUtils fileUtils) {
        String content = this.toString();

        String fileName = this.getClassName() + this.getClassNameSuffix() + CommonConstants.FILE_SUFFIX_JAVA;
        fileUtils.writeJavaToFile(this._package, fileName, content);
    }
}
