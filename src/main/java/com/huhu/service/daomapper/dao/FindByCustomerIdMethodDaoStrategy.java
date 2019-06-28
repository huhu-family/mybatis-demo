package com.huhu.service.daomapper.dao;

import com.huhu.constants.StringConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.service.daomapper.DaoStrategy;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 17:03
 */
public class FindByCustomerIdMethodDaoStrategy implements DaoStrategy {
    @Override
    public String genMethod(PojoClass pojoClass, String methodName) {
        if (!pojoClass.getTable().isUniqueCustomerId()) {
            return null;
        }

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
}
