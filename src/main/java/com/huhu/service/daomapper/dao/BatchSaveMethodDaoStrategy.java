package com.huhu.service.daomapper.dao;

import com.huhu.constants.StringConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.service.daomapper.DaoStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成 save 类方法
 *
 *      包含 save、saveOrUpdate
 *
 * @Author: wilimm
 * @Date: 2019/6/28 16:48
 */
public class BatchSaveMethodDaoStrategy implements DaoStrategy {

    @Override
    public String genMethod(PojoClass pojoClass, String methodName) {
        StringBuilder method = new StringBuilder();

        method.append(StringConstants.TAB);

        method.append("void ")
                .append(methodName)
                .append("(@Param(")
                .append("\"entityList\")")
                .append(" List<")
                .append(pojoClass.getClassName()).append(pojoClass.getClassNameSuffix())
                .append(">")
                .append(" entityList")
                .append(");");

        method.append(StringConstants.NEW_LINE);

        return method.toString();
    }

    @Override
    public List<String> needImportList() {
        return new ArrayList<String>() {
            {
                add("java.util.List;");
            }
        };
    }
}
