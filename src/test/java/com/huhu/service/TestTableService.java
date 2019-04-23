package com.huhu.service;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;
import com.huhu.init.InitParameters;
import com.huhu.utils.TableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wilimm
 * @Date: 2019/1/10 12:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestTableService {

    @Autowired
    private InitParameters initParameters;
    @Autowired
    private TableService tableService;

    @Test
    public void testGetTable() {
        Table table = tableService.getTable("customer_juvenile_mode");
        PojoClass pojoClass = TableUtils.generatePojo(table, initParameters.getDoPackage());

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println(pojoClass.toString());

       // System.out.println(pojoClass.toDTOString());

        System.out.println();
        System.out.println();
        System.out.println();

    }
}
