package com.huhu.service;

import com.huhu.constants.CharacterConstants;
import com.huhu.constants.CommonConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.utils.DateUtils;
import com.huhu.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 16:07
 */
@Service
public class DaoService {



    public DaoService() {
        this(CommonConstants.DAO_SUFFIX);
    }

    public DaoService(String classNameSuffix) {
        // 默认导入的类
        importList.add("org.springframework.stereotype.Repository;");
        importList.add("org.apache.ibatis.annotations.Param;");

        this.classNameSuffix = classNameSuffix;
    }

    /**
     * import 列表
     */
    private Set<String> importList = new HashSet<>();

    /**
     * Java Dao 后缀
     */
    private String classNameSuffix;

    public void generateDaoInterfaceToFile(PojoClass pojoClass, String daoPackage) {
        String content = generateDaoInterface(pojoClass, daoPackage);

        String fileName = pojoClass.getClassName() + this.classNameSuffix + CommonConstants.FILE_SUFFIX_JAVA;
        FileUtils.writeJavaToFile(daoPackage, fileName, content);

    }

    public String generateDaoInterface(PojoClass pojoClass, String daoPackage) {
        StringBuilder result = new StringBuilder();
        result.append("package ").append(daoPackage).append(";");

        result.append(CharacterConstants.NEW_LINE);
        result.append(CharacterConstants.NEW_LINE);

        for (String _import : importList) {
            result.append("import ").append(_import).append(CharacterConstants.NEW_LINE);
        }

        result.append(CharacterConstants.NEW_LINE);

        result.append("/**").append(CharacterConstants.NEW_LINE)
                .append(" * ").append(CharacterConstants.NEW_LINE)
                .append(" * @Author: ").append(pojoClass.getComment().getUser()).append(CharacterConstants.NEW_LINE)
                .append(" * @Date: ").append(DateUtils.format(pojoClass.getComment().getCreateTime())).append(CharacterConstants.NEW_LINE)
                .append(" */").append(CharacterConstants.NEW_LINE);

        // 默认的注解
        result.append("@Repository").append(CharacterConstants.NEW_LINE);

        result.append("public interface ")
                .append(pojoClass.getClassName())
                .append(classNameSuffix)
                .append(" {")
                .append(CharacterConstants.NEW_LINE);

        // 填充方法
        String saveMethod = genSaveMethod(pojoClass, "save");
        result.append(saveMethod);

        result.append(CharacterConstants.NEW_LINE);

        String saveOrUpdateMethod = genSaveMethod(pojoClass, "saveOrUpdate");
        result.append(saveOrUpdateMethod);

        result.append(CharacterConstants.NEW_LINE);

        String findByIdMethod = genFindByIdMethod(pojoClass);
        result.append(findByIdMethod);

        result.append(CharacterConstants.NEW_LINE);

        String updateByPrimaryKeySelective = genUpdateByPrimaryKeySelective(pojoClass);
        result.append(updateByPrimaryKeySelective);


        result.append("}").append(CharacterConstants.NEW_LINE);

        return result.toString();
    }

    private String genSaveMethod(PojoClass pojoClass, String methodName) {
        StringBuilder method = new StringBuilder();

        method.append(CharacterConstants.TAB);

        method.append("void ")
                .append(methodName)
                .append("(")
                .append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" entity")
                .append(");");

        method.append(CharacterConstants.NEW_LINE);

        return method.toString();
    }

    private String genFindByIdMethod(PojoClass pojoClass) {
        StringBuilder method = new StringBuilder();

        method.append(CharacterConstants.TAB);

        method.append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" findById")
                .append("(@Param(")
                .append("\"id\")")
                .append(" Long id")
                .append(");");

        method.append(CharacterConstants.NEW_LINE);

        return method.toString();
    }

    private String genUpdateByPrimaryKeySelective(PojoClass pojoClass) {
        StringBuilder method = new StringBuilder();

        method.append(CharacterConstants.TAB);

        method.append("int ")
                .append("updateByPrimaryKeySelective")
                .append("(")
                .append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" entity")
                .append(");");

        method.append(CharacterConstants.NEW_LINE);

        return method.toString();
    }
}

