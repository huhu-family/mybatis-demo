package com.huhu.service;

import com.huhu.constants.StringConstants;
import com.huhu.constants.CommonConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.utils.DateUtils;
import com.huhu.utils.FileUtils;
import com.huhu.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 16:07
 */
@Service
public class DaoService {

    /**
     * import 列表
     */
    private Set<String> importList = new HashSet<>();

    public DaoService() {
        // 默认导入的类
        importList.add("org.springframework.stereotype.Repository;");
        importList.add("org.apache.ibatis.annotations.Param;");

    }

    @Autowired
    private FileUtils fileUtils;


    public void generateDaoInterfaceToFile(PojoClass pojoClass, String daoPackage) {
        String packageName = daoPackage + "." + TableUtils.firstSubPackage(pojoClass.getTable().getName());

        String content = generateDaoInterface(pojoClass, packageName);

        String fileName = pojoClass.getClassName() + CommonConstants.DAO_SUFFIX + CommonConstants.FILE_SUFFIX_JAVA;
        fileUtils.writeJavaToFile(packageName, fileName, content);

    }

    public String generateDaoInterface(PojoClass pojoClass, String daoPackage) {
        StringBuilder result = new StringBuilder();
        result.append("package ").append(daoPackage).append(";");

        result.append(StringConstants.NEW_LINE);
        result.append(StringConstants.NEW_LINE);

        for (String _import : importList) {
            result.append("import ").append(_import).append(StringConstants.NEW_LINE);
        }

        result.append(StringConstants.NEW_LINE);

        result.append("/**").append(StringConstants.NEW_LINE)
                .append(" * ").append(StringConstants.NEW_LINE)
                .append(" * @Author: ").append(pojoClass.getComment().getUser()).append(StringConstants.NEW_LINE)
                .append(" * @Date: ").append(DateUtils.format(pojoClass.getComment().getCreateTime())).append(StringConstants.NEW_LINE)
                .append(" */").append(StringConstants.NEW_LINE);

        // 默认的注解
        result.append("@Repository").append(StringConstants.NEW_LINE);

        result.append("public interface ")
                .append(pojoClass.getClassName())
                .append(CommonConstants.DAO_SUFFIX)
                .append(" {")
                .append(StringConstants.NEW_LINE);

        // 填充方法
        String saveMethod = genSaveMethod(pojoClass, "save");
        result.append(saveMethod);

        result.append(StringConstants.NEW_LINE);

        String saveOrUpdateMethod = genSaveMethod(pojoClass, "saveOrUpdate");
        result.append(saveOrUpdateMethod);

        result.append(StringConstants.NEW_LINE);

        String insertSelectiveMethod = genSaveMethod(pojoClass, "insertSelective");
        result.append(insertSelectiveMethod);

        result.append(StringConstants.NEW_LINE);

        String findByIdMethod = genFindByIdMethod(pojoClass);
        result.append(findByIdMethod);

        if (pojoClass.getTable().isUniqueCustomerId()) {
            String findByCustomerIdMethod = genFindByCustomerIdMethod(pojoClass);

            result.append(StringConstants.NEW_LINE);
            result.append(findByCustomerIdMethod);
        }

        result.append(StringConstants.NEW_LINE);

        String updateByPrimaryKeySelective = genUpdateByPrimaryKeySelective(pojoClass);
        result.append(updateByPrimaryKeySelective);

        result.append(StringConstants.NEW_LINE);

        String deleteById = genDeleteById();
        result.append(deleteById);


        result.append("}").append(StringConstants.NEW_LINE);

        return result.toString();
    }


    private String genSaveMethod(PojoClass pojoClass, String methodName) {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append("void ")
                .append(methodName)
                .append("(")
                .append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" entity")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }

    private String genFindByIdMethod(PojoClass pojoClass) {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" findById")
                .append("(@Param(")
                .append("\"id\")")
                .append(" Long id")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }

    private String genFindByCustomerIdMethod(PojoClass pojoClass) {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" findByCustomerId")
                .append("(@Param(")
                .append("\"customerId\")")
                .append(" Long customerId")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }

    private String genUpdateByPrimaryKeySelective(PojoClass pojoClass) {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append("int ")
                .append("updateByPrimaryKeySelective")
                .append("(")
                .append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(" entity")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }


    private String genDeleteById() {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append("int ")
                .append(" deleteById")
                .append("(@Param(")
                .append("\"id\")")
                .append(" Long id")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }
}

