package com.wth.messagehandlers.message.impl;

import com.simplerobot.modules.utils.KQCodeUtils;
import com.wth.client.MsgClient;
import com.wth.config.BotConfig;
import com.wth.constant.*;
import com.wth.entity.chart.ChatGTP;
import com.wth.entity.get.Message;
import com.wth.entity.response.ChatResp;
import com.wth.factory.ThreadPoolFactory;
import com.wth.messagehandlers.message.IMessageEvent;
import com.wth.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class AICharHandler implements IMessageEvent {

    @Override
    public int weight() {
        return WeightForFunctionEnum.SIMPLE_AI.getWeight();
    }
    @Override
    public String name() {
        return "弱智rabbit";
    }
    private String[] cqs;

    public boolean matching(final Message message, final String command) {
        if(MessageEventEnum.privat.getType().equals(message.getMessageType())){
            // 私聊了机器人
            this.cqs = null;
            return true;
        }
        if(MessageEventEnum.group.getType().equals(message.getMessageType())){
            KQCodeUtils utils = KQCodeUtils.getInstance();
            String[] cqs = utils.getCqs(command, CqCodeTypeEnum.at.getType());
            if(cqs == null || cqs.length == 0){
                // 没有at机器人
                this.cqs = null;
                return false;
            }
            for (String cq : cqs) {
                String qq = utils.getParam(cq, "qq", CqCodeTypeEnum.at.getType());
                if(qq != null && qq.equals(message.getSelfId())){
                    // 表示at了机器人
                    this.cqs = cqs;
                    return true;
                }
            }
        }
        this.cqs = null;
        return false;
    }

    // 这个是调用的，淘汰了，现在
    @Override
    public boolean onMessage(final Message message,final String command) {
        if(!matching(message,command)){
            return false;
        }
        ThreadPoolFactory.getCommandHandlerThreadPool().execute(()->{
            String s = command;
            if(this.cqs != null){
                for (String cq : this.cqs) {
                    s = s.replace(cq,"");
                }
            }
            if(Strings.isBlank(s)){
                return;
            }

            HashMap<String, Object> urlParam = new HashMap<>(3);
            urlParam.put("key","free");
            urlParam.put("appid",0);
            urlParam.put("msg",s);
            ChatResp chatResp = null;
            try {
                chatResp = RestUtil.sendGetRequest(RestUtil.getRestTemplate(8 * 1000), ThirdPartyURL.AI_CHAT, urlParam, ChatResp.class);
            }catch (Exception e){
                MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(), MessageFormat.format("聊天api请求异常:{0}",e.getMessage()), GocqActionEnum.SEND_MSG,false);
                log.error("青云客api请求异常",e);
            }
            if(chatResp != null){
                String content = chatResp.getContent();
                if(content != null){
                    MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(),processContent(content),GocqActionEnum.SEND_MSG,false);
                }
            }
        });
        return true;
    }

    private static String reg ="(?<=\\{face:)[0-9]*(?=\\})";
    private static String regex = ".*\\{face:.*\\}.*";
    private String processContent(String content){
        log.info("content"+content);
        content = content.replace("{br}", "\n").replace("菲菲", BotConfig.NAME).replace("&quot;","“");
        if(!content.matches(regex)){
            return content;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) {
            matchStrs.add(matcher.group());
        }
        KQCodeUtils instance = KQCodeUtils.getInstance();
        for (int i = 0; i < matchStrs.size(); i++) {
            String id = matchStrs.get(i);
            String face = instance.toCq(CqCodeTypeEnum.face.getType(), "id="+id);
            content = content.replace("{face:" + id + "}", face);
        }
        return content;
    }
}
