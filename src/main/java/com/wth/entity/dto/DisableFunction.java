package com.wth.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_disable_function")
public class DisableFunction {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String className;
    private String name;
    private int weight;
    private Boolean global;
    private String groupId;
    private Date disableTime;
}
