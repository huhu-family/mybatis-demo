package com.huhu.dao;

import com.huhu.domain.entity.DemoDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wilimm
 * @Date: 2018/10/12 17:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestDemoDao {
    @Autowired
    private DemoDao demoDao;

    @Test
    public void testSave() {
        DemoDO demoDO = new DemoDO();
        demoDO.setAge(10);
        demoDO.setName("宋。:tomato::tomato::tomato:");
        demoDao.save(demoDO);
    }


    @Test
    public void saveOrUpdate() {
        DemoDO demoDO = new DemoDO();
        demoDO.setId(10);
        demoDO.setAge(1000);
        demoDO.setName("1000");
        boolean saved = demoDao.saveOrUpdate(demoDO);
        System.out.println("==============================" + saved);
    }

    @Test
    public void testFind() {
        DemoDO demoDO = demoDao.find(1);
        System.out.println(demoDO);
        System.out.println(demoDO.getAge() == 66);
        System.out.println(demoDO.getId() == 6);
    }
}
