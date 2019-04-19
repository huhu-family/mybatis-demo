package com.huhu.service;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;
import com.huhu.utils.TableUtils;
import org.dom4j.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 16:14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestDaoService {

    /**
     * DO 所在的包
     */
    private static final String DO_PACKAGE = "me.huakai.dao.entity.video";

    /**
     * Dao 所在的包
     */
    private static final String DAO_PACKAGE = "me.huakai.dao.mysql.video";

    @Autowired
    private TableService tableService;

    @Autowired
    private MapperXmlService mapperXmlService;
    @Autowired
    private DaoService daoService;

    @Test
    public void test() throws IOException {
        Table table = tableService.getTable("action_top");
        PojoClass pojoClass = TableUtils.tableToPojo(table, DO_PACKAGE);

        System.out.println();
        System.out.println();
        System.out.println();

        String daoClass = daoService.generateDaoInterface(pojoClass, DAO_PACKAGE);
        System.out.println(daoClass);

        System.out.println();
        System.out.println();
        System.out.println();

        Document document = mapperXmlService.generateMapperXml(pojoClass, DAO_PACKAGE);
        mapperXmlService.printDoc(document);

        System.out.println();
        System.out.println();
        System.out.println();
    }
}
