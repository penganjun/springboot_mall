package com.ajpeng.mall.mmall.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userName", columnDefinition = "varchar(50) COMMENT '用户名'")
    private String userName;

    @Column(columnDefinition = "varchar(50) COMMENT '密码'")
    private String password;

}
