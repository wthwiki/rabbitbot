package com.wth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "t_disable_function") //设置表名
@org.hibernate.annotations.Table(appliesTo = "t_disable_function", comment = "测试信息")//设置表注释
//@TableName(value = "t_disable_function")
public class DisableFunction {
//    @TableId(value = "id",type = IdType.AUTO)
    @Id
    private Integer id;
    private String className;
    private String name;
    private int weight;
    private Boolean global;
    private String groupId;
    private Date disableTime;
}
