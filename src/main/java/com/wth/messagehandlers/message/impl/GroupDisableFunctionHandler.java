package com.wth.messagehandlers.message.impl;

import com.wth.client.MsgClient;
import com.wth.config.BotConfig;
import com.wth.constant.GocqActionEnum;
import com.wth.constant.MessageEventEnum;
import com.wth.constant.RegexEnum;
import com.wth.entity.dto.DisableFunction;
import com.wth.entity.get.Message;
import com.wth.factory.ThreadPoolFactory;
import com.wth.messagehandlers.IMessageEventType;
import com.wth.messagehandlers.MessageHandleRegister;
import com.wth.messagehandlers.message.IGroupMessageEvent;
import com.wth.messagehandlers.message.IMessageEvent;
import com.wth.service.DisableFunctionService;
import com.wth.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Slf4j
@Component
public class GroupDisableFunctionHandler implements IGroupMessageEvent {

    @Autowired
    private DisableFunctionService disableFunctionService;

    @Override
    public int weight() {
        return 107;
    }

    @Override
    public String name() {
        return "禁用群功能";
    }


    @Override
    public boolean onGroup(final Message message,final String command) {
        if(!message.getUserId().equals(BotConfig.SUPER_USER)){
            return false;
        }
        String fun = CommonUtil.commandReplaceFirst(command, RegexEnum.GROUP_DISABLE_FUNCTION);
        if (Strings.isBlank(fun)){
            return false;
        }
        ThreadPoolFactory.getCommandHandlerThreadPool().execute(new Task(message,fun,disableFunctionService));
        return true;
    }

    private class Task implements Runnable{
        private Message message;
        private String fun;
        private DisableFunctionService service;
        public Task(Message message,String fun,DisableFunctionService service){
            this.message = message;
            this.fun = fun;
            this.service = service;
        }
        @Override
        public void run() {
            try {
                IMessageEventType messageEventType = MessageHandleRegister.findHandler(fun);
                if (messageEventType == null) {
                    MsgClient.sendMessage(message.getUserId(),message.getGroupId(), MessageEventEnum.group, MessageFormat.format("没有功能:[{0}]",fun), GocqActionEnum.SEND_MSG,true);
                    return;
                }
                Class<? extends IMessageEventType> aClass = messageEventType.getClass();
                if (MessageHandleRegister.isBanFunctionByGroup(aClass, message.getGroupId())) {
                    MsgClient.sendMessage(message.getUserId(),message.getGroupId(),MessageEventEnum.group, MessageFormat.format("群功能:[{0}]已经被禁用",fun), GocqActionEnum.SEND_MSG,true);
                    return;
                }
                MessageHandleRegister.setGroupBanFunction(message.getGroupId(),aClass);
                service.insert(messageEventType,aClass,message);
                MsgClient.sendMessage(message.getUserId(),message.getGroupId(),MessageEventEnum.group,MessageFormat.format("群禁用[{0}]成功",fun), GocqActionEnum.SEND_MSG,true);
            }catch (Exception e){
                MsgClient.sendMessage(message.getUserId(),message.getGroupId(), MessageEventEnum.group, MessageFormat.format("禁用群功能[{0}]时发生异常:{1}",fun,e.getMessage()), GocqActionEnum.SEND_MSG,true);
                log.error("禁用群功能时发生异常",e);
            }
        }
    }
}
