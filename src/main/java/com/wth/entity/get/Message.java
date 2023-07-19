package com.wth.entity.get;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 这是go-cq传过来的原始消息类
 */
@Data
@ToString
public class Message implements Serializable {

    private Long id;
    private String postType;
    private String metaEventType;
    // 这个是聊天类型。
    private String messageType;
    private String noticeType;
    // 操作人id 比如群管理员a踢了一个人,那么该值为a的qq号
    private String operatorId;
    private Long time;
    private String selfId;
    private String subType;
    private String userId;
    private String senderId;
    private String groupId;
    private String targetId;
    private String message;
    private String rawMessage;
    private Sender sender;
    private Integer font;
    private String messageId;
    private Integer messageSeq;
    private String anonymous;
}
