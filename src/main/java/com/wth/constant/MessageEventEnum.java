package com.wth.constant;

// 发送消息的事件
public enum MessageEventEnum {
    group("group"),
    privat("private");
    private String type;
    MessageEventEnum(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
}

