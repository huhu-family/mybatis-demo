package com.huhu.dao.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 11:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestTableDao {
    @Autowired
    private TableDao tableDao;

    @Test
    public void testShowTable() {
        Map<String, String> demo = tableDao.showCreateTable("demo");
        System.out.println(demo);
        System.out.println(demo.get("Create Table"));
    }

}
