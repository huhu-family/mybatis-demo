package com.huhu.service.daomapper.dao;

import com.huhu.constants.StringConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.service.daomapper.DaoStrategy;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 17:04
 */
public class DeleteByIdDaoStrategy implements DaoStrategy {
    @Override
    public String genMethod(PojoClass pojoClass, String methodName) {
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
