package com.huhu.service.daomapper;

import com.huhu.domain.entity.PojoClass;

import java.util.List;

/**
 * @Author: wilimm
 * @Date: 2019/6/28 16:45
 */
public interface DaoStrategy {

    /**
     * 生成 Dao 方法
     *
     * @param pojoClass
     * @param methodName
     * @return
     */
    String genMethod(PojoClass pojoClass, String methodName);

    default List<String> needImportList() {
        return null;
    }
}
