package com.huhu.service.daomapper;

import com.huhu.domain.entity.PojoClass;
import org.dom4j.Element;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 16:46
 */
public interface MapperStrategy {

    /**
     * 生成 Mybatis Mapper 节点
     *
     * @param pojoClass
     * @param methodName
     * @return
     */
    Element genXmlElement(PojoClass pojoClass, String methodName);
}
