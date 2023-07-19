package com.wth.service;

import com.wth.entity.Message;
import com.wth.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageMapper mapper;

    public Message select(){
        return mapper.selectById("1");
    }

}
