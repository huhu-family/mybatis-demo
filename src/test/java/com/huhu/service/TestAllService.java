package com.huhu.service;

import com.huhu.domain.entity.PojoClass;
import com.huhu.domain.entity.Table;
import com.huhu.init.InitParameters;
import com.huhu.utils.FileUtils;
import com.huhu.utils.TableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @Author: wilimm
 * @Date: 2019/4/23 11:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestAllService {

    @Autowired
    private InitParameters initParameters;
    @Autowired
    private TableService tableService;
    @Autowired
    private MapperXmlService mapperXmlService;
    @Autowired
    private DaoService daoService;
    @Autowired
    private FileUtils fileUtils;


    @Test
    public void test() throws IOException {
        Table table = tableService.getTable("customer_current_location");

        PojoClass pojoClass = TableUtils.generatePojo(table, initParameters.getDoPackage());

        System.out.println();
        System.out.println();
        System.out.println();

        pojoClass.writeToFile(fileUtils);

        System.out.println();
        System.out.println();
        System.out.println();

        daoService.generateDaoInterfaceToFile(pojoClass, initParameters.getDaoPackage());


        System.out.println();
        System.out.println();
        System.out.println();

        mapperXmlService.generateMapperXmlToFile(pojoClass, initParameters.getDaoPackage());

        System.out.println();
        System.out.println();
        System.out.println();
    }
}
