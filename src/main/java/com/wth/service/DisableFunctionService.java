package com.wth.service;

import com.wth.entity.dto.DisableFunction;
import com.wth.entity.get.Message;
import com.wth.mapper.DisableFunctionMapper;
import com.wth.messagehandlers.IMessageEventType;
import com.wth.messagehandlers.message.IMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DisableFunctionService {
    @Autowired
    private DisableFunctionMapper disableFunctionMapper;

    public void insert(IMessageEventType messageEventType, Class<? extends IMessageEventType> aClass, Message message) {
        DisableFunction param = new DisableFunction();
        param.setGlobal(false);
        param.setDisableTime(new Date());
        param.setName(messageEventType.name());
        param.setWeight(messageEventType.weight());
        param.setClassName(aClass.getName());
        param.setGroupId(message.getGroupId());
        disableFunctionMapper.insert(param);
    }
}
