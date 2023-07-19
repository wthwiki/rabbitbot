package com.wth.messagehandlers.message;

import com.wth.entity.get.Message;
import com.wth.messagehandlers.IMessageEventType;

public interface IGroupMessageEvent extends IMessageEventType {
    boolean onGroup(final Message message,final String command);
}
