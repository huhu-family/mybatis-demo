package com.pagoda.huhu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: wilimm
 * @Date: 2018/9/14 10:24
 */
@Data
@NoArgsConstructor
public class DemoDO {

    public DemoDO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private Integer id;
    private String name;
    private Integer age;

}
