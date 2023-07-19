package com.wth.messagehandlers.notice;

import com.wth.entity.get.Message;

public interface IGroupRecallEvent extends INoticeEventType{
    /**
     *
     * @param message
     */
    void comeBackRecall(Message message);
}
