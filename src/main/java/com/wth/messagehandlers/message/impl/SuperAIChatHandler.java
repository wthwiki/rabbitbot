package com.wth.messagehandlers.message.impl;

import com.simplerobot.modules.utils.KQCodeUtils;
import com.wth.client.MsgClient;
import com.wth.config.BotConfig;
import com.wth.constant.*;
import com.wth.entity.chart.ChatGTP;
import com.wth.entity.get.Message;
import com.wth.entity.response.ForwardMsg;
import com.wth.factory.ThreadPoolFactory;
import com.wth.messagehandlers.message.IMessageEvent;
import com.wth.utils.CommonUtil;
import com.wth.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SuperAIChatHandler implements IMessageEvent {
    @Override
    public int weight() {
        return WeightForFunctionEnum.SUPER_AI.getWeight();
    }
    private String[] cqs=new String[]{"ai"};


    @Override
    public boolean onMessage(final Message message,final String command) {
        Pattern compile = Pattern.compile(RegexEnum.SUPER_AI_CHAT.getValue());
        Matcher matcher = compile.matcher(command);
        if(command.startsWith("ai")){
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
                String chatResp = null;
                try {
                    MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(),"耐心等待...", GocqActionEnum.SEND_MSG,true);
                    chatResp = ChatGTP.addMsg(s,null);
                }catch (Exception e){
                    MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(), MessageFormat.format("AIapi请求异常:{0}",e.getMessage()), GocqActionEnum.SEND_MSG,false);
                    log.error("superAI请求异常",e);
                }
                if(chatResp != null){
                    String content = chatResp;
                    if(content != null){
                        MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(),processContent(content),GocqActionEnum.SEND_MSG,false);
                    }
                }
            });
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String name() {
        return "superAI";
    }

//    private static class Task implements Runnable{
//        Message message;
//        String keyword;
//        Integer page;
//
//        public Task(Message message, String keyword, Integer page) {
//            this.message = message;
//            this.keyword = keyword;
//            this.page = page;
//        }
//
//        @Override
//        public void run() {
//            try {
//                String htmlStr = HttpClientUtil.doGet(HttpClientUtil.getHttpClient(10 * 1000), MessageFormat.format(ThirdPartyURL.BT_SEARCH + "/s/{0}_rel_{1}.html", keyword, page), null);
//                if(Strings.isBlank(htmlStr)){
//                    MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(),"bt搜索请求发生异常", GocqActionEnum.SEND_MSG,true);
//                    return;
//                }
//                Document document = Jsoup.parse(htmlStr);
//                Elements list = document.getElementsByClass("search-item");
//                if (CollectionUtils.isEmpty(list)) {
//                    noData(message,keyword);
//                    return;
//                }
//                List<String> res = new ArrayList<>(list.size());
//                for (Element element : list) {
//                    Elements a = element.getElementsByTag("a");
//                    if (CollectionUtils.isEmpty(a)) {
//                        continue;
//                    }
//                    StringBuilder strBuilder = new StringBuilder();
//                    Element title = a.get(0);
//                    String detailHref = title.attr("href");
//                    // 追加标题
//                    strBuilder.append(title.text()).append("\n");
//                    String s = ThirdPartyURL.BT_SEARCH + detailHref;
//                    try {
//                        // 请求详情链接
//                        requestDetail(strBuilder,s);
//                    }catch (Exception e){
//                        log.error("bt获取详情异常:{}",s,e);
//                        continue;
//                    }
//                    res.add(strBuilder.toString());
//                }
//                if(res.size() == 0){
//                    noData(message,keyword);
//                    return;
//                }
//                List<ForwardMsg> param = new ArrayList<>(res.size());
//                for (String re : res) {
//                    param.add(CommonUtil.createForwardMsgItem(re,message.getSelfId(), BotConfig.NAME));
//                }
//                if(MessageEventEnum.group.getType().equals(message.getMessageType())){
//                    MsgClient.sendMsg(GocqActionEnum.SEND_GROUP_FORWARD_MSG,message.getGroupId(),param);
//                }else if(MessageEventEnum.privat.getType().equals(message.getMessageType())){
//                    MsgClient.sendMsg(GocqActionEnum.SEND_PRIVATE_FORWARD_MSG,message.getUserId(),param);
//                }
//            }catch (Exception e){
//                MsgClient.sendMsg(message.getUserId(),message.getGroupId(),message.getMessageType(),MessageFormat.format("bt搜索异常:{0}",e.getMessage()), GocqActionEnum.SEND_MSG,true);
//                log.error("bt搜图异常",e);
//            }
//
//        }
//    }


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
