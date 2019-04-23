package com.huhu.service;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;
import com.huhu.init.InitParameters;
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

    @Autowired
    private InitParameters initParameters;
    @Autowired
    private TableService tableService;
    @Autowired
    private MapperXmlService mapperXmlService;
    @Autowired
    private DaoService daoService;

    @Test
    public void test() throws IOException {
        Table table = tableService.getTable("customer_juvenile_mode");
        PojoClass pojoClass = TableUtils.generatePojo(table, initParameters.getDoPackage());

        System.out.println();
        System.out.println();
        System.out.println();

        String daoClass = daoService.generateDaoInterface(pojoClass, initParameters.getDaoPackage());
        System.out.println(daoClass);

        System.out.println();
        System.out.println();
        System.out.println();

        Document document = mapperXmlService.generateMapperXml(pojoClass, initParameters.getDaoPackage());
        mapperXmlService.printDoc(document);

        System.out.println();
        System.out.println();
        System.out.println();
    }
}
