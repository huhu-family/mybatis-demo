package com.pagoda.huhu.dao;

import com.pagoda.huhu.entity.DemoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: wilimm
 * @Date: 2018/9/14 10:25
 */
@Repository
public interface DemoDao {
    boolean save(DemoDO demoDO);
    boolean saveOrUpdate(DemoDO demoDO);

    DemoDO find(@Param("id") Integer id);
}
