package com.wth.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
//@javax.persistence.Table(name = "sender") //设置表名
//@org.hibernate.annotations.Table(appliesTo = "sender", comment = "测试信息")//设置表注释
public class Sender {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private int age;
    private String area;
    private String card;
    private String level;
    private String role;
    private String nickname;
    private String sex;
    private String title;
    private String user_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
