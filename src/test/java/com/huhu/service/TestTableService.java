package com.huhu.service;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;
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

    /**
     * DO 所在的包
     */
    private static final String DO_PACKAGE = "com.huhu.domain.entity";

    @Autowired
    private TableService tableService;

    @Test
    public void testGetTable() {
        Table table = tableService.getTable("action_top");
        PojoClass pojoClass = TableUtils.tableToPojo(table, DO_PACKAGE);

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
