package com.huhu.service.daomapper;

import com.huhu.domain.entity.PojoClass;
import org.dom4j.Element;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 16:43
 */
public class DaoMapperSave extends AbstractDaoMapper {

    public DaoMapperSave(PojoClass pojoClass, String methodName) {
        super(pojoClass, methodName);
    }

    @Override
    public String genMethod() {
        return null;
    }

    @Override
    public Element genXmlElement() {
        return null;
    }
}
