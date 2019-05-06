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
     * Java 文件所在的根目录，Maven 项目通常为项目根目录的 src\main\java 子目录
     */
    String javaSrcPath;

    /**
     * xml 文件所在的路径，项目中存放 MyBatis XML 文件的目录
     */
    String xmlPath;

}
