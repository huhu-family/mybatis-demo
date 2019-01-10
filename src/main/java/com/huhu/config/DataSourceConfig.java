
package com.huhu.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xiang.rao created on 4/11/18 6:40 AM
 * @version $Id$
 */
@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
public class DataSourceConfig {

    @Bean
    public DataSource dataSourceOne() {
        return DruidDataSourceBuilder.create().build();
    }

}

