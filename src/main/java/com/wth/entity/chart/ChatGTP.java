package com.wth.entity.chart;

import com.alibaba.fastjson2.JSON;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.wth.config.ChartConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;


@Component
public class ChatGTP implements Runnable{
    private static ChartConfig chartConfig;
    private static Chatbot chatbot;
    private static final ArrayBlockingQueue<String> sendMsg=new ArrayBlockingQueue<String>(5);
    private static final ArrayBlockingQueue<String> backMsg=new ArrayBlockingQueue<String>(5);

    public static  String addMsg(String msg,String[] args) throws InterruptedException {
        if(sendMsg.size()!=0) sendMsg.clear();
        sendMsg.put(msg);
        return getRspMsg();
//        while (true){
//            getRspMsg();
//            return backMsg.take();
//        }
    }
    public static String getRspMsg() throws InterruptedException {
        String prompt;
        prompt = sendMsg.take();
        System.out.println("prompt"+prompt);
        if (prompt.startsWith("!")) {
            if (prompt.equals("!help")) {
            } else if (prompt.equals("!reset")) {
                chatbot.resetChat();
            } else if (prompt.equals("!refresh")) {
                chatbot.refreshSession();
                System.out.println("Session refreshed.\n");
            } else if (prompt.equals("!rollback")) {
                chatbot.rollbackConversation();
                System.out.println("Chat session rolled back.");
            } else if (prompt.equals("!config")) {
                System.out.println(JSON.toJSONString(chatbot.getConfig()));
            } else if (prompt.equals("!exit")) {
            }
            return "";
        }
        try {
            Map<String, Object> message = chatbot.getChatResponse(prompt, "stream");
            // Split the message by newlines
            String[] messageParts = message.get("message").toString().split("\n");
            for (String part : messageParts) {
                String[] wrappedParts = part.split("\n");
                StringBuilder sb=new StringBuilder();
                for (String wrappedPart : wrappedParts) {
                    sb.append(wrappedPart);
                }
//                    backMsg.put(sb.toString());
                System.out.println("superAI返回消息");
                return sb.toString();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }

        return "";
    }

    @Autowired
    public void setChartConfig(ChartConfig config){
        chartConfig=config;
    }

    @SneakyThrows
    @Override
    public void run() {
        chatbot=new Chatbot(chartConfig, null);
    }
}

