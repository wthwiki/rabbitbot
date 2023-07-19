package com.wth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wth.entity.dto.BaseMessage;
import com.wth.entity.dto.BaseSender;
import com.wth.entity.get.Message;
import com.wth.entity.get.Sender;
import com.wth.mapper.MessageMapper;
import com.wth.mapper.SenderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SenderMapper senderMapper;

    public void insertMessage(Message message) {
        BaseMessage baseMessage = new BaseMessage();
        BeanUtils.copyProperties(message, baseMessage);
        BaseSender sender = new BaseSender();
        BeanUtils.copyProperties(message.getSender(),sender);
//        TODO

        messageMapper.insert(baseMessage);
        sender.setId(Long.parseLong(message.getMessageId()));
        senderMapper.insert(sender);
    }
    public BaseMessage getMessageByMessageId (String message_id){
        QueryWrapper<BaseMessage> wrapper = new QueryWrapper<>();
        wrapper.eq("message_id",message_id);
        List<BaseMessage> baseMessages = messageMapper.selectList(wrapper);

        BaseMessage message=null;
        if(baseMessages!=null&&baseMessages.size()!=0){
           message= baseMessages.get(0);
       }
        return message;
    }
}