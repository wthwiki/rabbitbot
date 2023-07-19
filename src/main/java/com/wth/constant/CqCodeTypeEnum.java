package com.wth.constant;

/**
 * 这是根据官方文档来的，
 * 不同的类型代表着不同的含义
 *  消息类型
 *  [CQ:类型,参数=值,参数=值]
 */
public enum CqCodeTypeEnum {
    at("at"),
    image("image"),
    face("face"),
    forward("forward"),
    reply("reply"),
    tts("tts"),
    //戳一戳
    poke("poke"),
    record("record"),
    music("music");


    private String type;
    CqCodeTypeEnum(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
