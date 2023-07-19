package com.wth.messagehandlers;

import com.wth.entity.get.Message;
import com.wth.messagehandlers.notice.IGroupRecallEvent;
import com.wth.messagehandlers.notice.INoticeEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NoticeHandlerRegister {
    private static Map<String, INoticeEventType> noticeEventTypeMap;

    @Autowired
    public void setNoticeEventTypeMap(Map<String,INoticeEventType> noticeEventTypeMap){
        NoticeHandlerRegister.noticeEventTypeMap=noticeEventTypeMap;
    }

    private static List<INoticeEventType> container = new ArrayList<>();
    @PostConstruct
    private void loadEvent(){
        log.info("加载通知处理类...");
        for (INoticeEventType value : noticeEventTypeMap.values()) {
            NoticeHandlerRegister.attach(value);
        }
        log.info("加载了{}个通知处理类",container.size());
    }

    private static void attach(INoticeEventType value) {
        container.add(value);
    }
    public static void handle(final Message message){
        for(INoticeEventType noticeEventType:container){
            IGroupRecallEvent noticeEventType1 = (IGroupRecallEvent) noticeEventType;
            noticeEventType1.comeBackRecall(message);
        }
    }
}
