package com.wth.thread;

import com.wth.constant.event.PostTypeEnum;
import com.wth.entity.get.Message;
import com.wth.messagehandlers.MessageHandleRegister;
import com.wth.messagehandlers.NoticeHandlerRegister;
import com.wth.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 该类为处理请求消息任务的线程类。
 */
@Slf4j
public class MsgEventTask implements Runnable{
    Message message ;

    public MsgEventTask(Message message) {
        this.message = message;
    }

    /**
     *
     */
    @Override
    public void run() {
        // 普通消息
        if(PostTypeEnum.message.toString().equals(message.getPostType())){
            final String command = this.message.getMessage();
            log.info("[{}]收到了来自用户[{}]的消息:{}",message.getMessageType(),message.getUserId(),command);
            if(command!=null){
                MessageHandleRegister.handle(message,command);
            }
            //bot的通知
        }else if(PostTypeEnum.notice.toString().equals(message.getPostType())){
            // TODO
            NoticeHandlerRegister.handle(message);
            // 系统消息
        }else if(PostTypeEnum.meta_event.toString().equals(message.getPostType())){
            // TODO
        }else{

        }
    }
}
