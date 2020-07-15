package com.cabbage.bean;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;

    private String account;

    private String password;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer vaild;
}