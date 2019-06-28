package com.huhu.init;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wilimm
 * @Date: 2019/5/6 14:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestInitParameters {

    @Autowired
    private InitParameters initParameters;


    @Test
    public void test() {
        System.out.println(initParameters.getJavaSrcPath());
    }
}
