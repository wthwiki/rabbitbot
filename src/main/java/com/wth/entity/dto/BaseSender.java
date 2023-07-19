package com.wth.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sender")
public class BaseSender {
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
}
