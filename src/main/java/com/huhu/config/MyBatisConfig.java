package com.huhu.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * @Author: wilimm
 * @Date: 2018/8/30 11:23
 */

@Configuration
@MapperScan("com.huhu.dao")
public class MyBatisConfig {

    /**   mybatis 配置路径     */
    private static String MYBATIS_CONFIG = "mybatis/mybatis-config.xml";
    /**   mybatis mapper resource 路径     */
    private static String MAPPER_PATH = "mybatis/**/*Dao.xml";

    @Autowired
    private DataSource dataSource;

    /**
     *创建sqlSessionFactoryBean 实例
     * 并且设置configtion 如驼峰命名.等等
     * 设置mapper 映射路径
     * 设置datasource数据源
     * @return
     */
    @Bean
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /** 设置mybatis configuration 扫描路径 */
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        /** 添加mapper 扫描路径 */
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + MAPPER_PATH;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        /** 设置datasource */
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(value = "mybatisTransactionManager")
    @Resource
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
