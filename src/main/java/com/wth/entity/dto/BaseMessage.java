package com.wth.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("message")
public class BaseMessage {
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
    private Integer font;
    private String messageId;
    private Integer messageSeq;
    private String anonymous;
}
