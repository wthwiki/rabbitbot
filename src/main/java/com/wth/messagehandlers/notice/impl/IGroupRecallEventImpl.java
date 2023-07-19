package com.wth.messagehandlers.notice.impl;

import com.wth.client.MsgClient;
import com.wth.constant.GocqActionEnum;
import com.wth.entity.dto.BaseMessage;
import com.wth.entity.dto.BaseSender;
import com.wth.entity.get.Message;
import com.wth.factory.ThreadPoolFactory;
import com.wth.mapper.SenderMapper;
import com.wth.messagehandlers.message.impl.BaseMessageHandler;
import com.wth.messagehandlers.notice.IGroupRecallEvent;
import com.wth.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IGroupRecallEventImpl implements IGroupRecallEvent {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SenderMapper senderMapper;
    @Override
    public void comeBackRecall(Message message) {

        ThreadPoolFactory.getCommandHandlerThreadPool().execute(()->{
            BaseMessage bMess = messageService.getMessageByMessageId(message.getMessageId());
            log.info("bMess:{}",bMess);
            Message mess = new Message();
            BeanUtils.copyProperties(bMess,mess);
            System.out.println("message:"+message);
            BaseSender baseSender = senderMapper.selectById(message.getMessageId());
            message.setMessage(baseSender.getNickname()+":撤回了\n"+bMess.getMessage());
            MsgClient.sendMsg(message.getUserId(),mess.getGroupId(),bMess.getMessageType(),message.getMessage(), GocqActionEnum.SEND_MSG,false);
        });
    }
}
