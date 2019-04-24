package com.huhu.init;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  初始化参数
 *
 * @Author: wilimm
 * @Date: 2019/4/23 10:20
 */
@Component
@Data
@ConfigurationProperties(prefix = "package-config")
public class InitParameters {

    /**
     *  DO 所在的包
     */
    String doPackage;

    /**
     *  DAO 所在的包
     */
    String daoPackage;

    /**
     * Java 文件所在的路径
     */
    String javaSrcPath;

    /**
     * xml 文件所在的路径
     */
    String xmlPath;

}
