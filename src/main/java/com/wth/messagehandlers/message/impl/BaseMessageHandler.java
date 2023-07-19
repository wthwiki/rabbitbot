package com.wth.messagehandlers.message.impl;

import com.wth.client.MsgClient;
import com.wth.constant.WeightForFunctionEnum;
import com.wth.entity.get.Message;
import com.wth.messagehandlers.message.IMessageEvent;
import org.springframework.stereotype.Component;

@Component
public class BaseMessageHandler  <T> implements IMessageEvent {
    @Override
    public int weight() {
        return WeightForFunctionEnum.BASE_MESSAGE.getWeight();
    }

    @Override
    public boolean onMessage(Message message, String command) {
        return false;
    }

    public boolean onMessage(T message,final String command) {

        MsgClient.sendMsg("");
        return true;
    }

    @Override
    public String name() {
        return null;
    }
}
