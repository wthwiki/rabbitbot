package com.wth.messagehandlers.message;

import com.wth.entity.get.Message;
import com.wth.messagehandlers.IMessageEventType;

/**
 * 用于定义消息事件处理规则
 */
public interface IMessageEvent extends IMessageEventType {

    boolean onMessage(Message message ,final String command);

    boolean enable=true;
}
