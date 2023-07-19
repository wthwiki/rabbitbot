package com.wth.entity.response;

import lombok.Data;

import java.util.Collection;

/**
 * 这个类是go-cq返回消息的参数类
 * 每个属性在不同消息类型的时候有不同含义，具体参考go-cq的http文档。
 */
@Data

public class Params {
    // 消息类型
    private String message_type;
    private String user_id;
    private String group_id;
    private Object message;
    private Collection messages;
    private boolean auto_escape=true;
}
