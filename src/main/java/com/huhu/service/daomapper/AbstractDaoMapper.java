package com.huhu.service.daomapper;

import com.huhu.domain.entity.PojoClass;
import org.dom4j.Element;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 16:39
 */
public abstract class AbstractDaoMapper {

    /**
     * Java POJO 类
     */
    private PojoClass pojoClass;

    /**
     * 方法名称
     */
    private String methodName;

    public AbstractDaoMapper(PojoClass pojoClass, String methodName) {
        this.pojoClass = pojoClass;
        this.methodName = methodName;
    }

    private DaoStrategy daoStrategy;
    private MapperStrategy mapperStrategy;

    public String genMethod() {
        return daoStrategy.genMethod(pojoClass, methodName);
    }

    public Element genXmlElement() {
        return mapperStrategy.genXmlElement(pojoClass, methodName);
    }
}
