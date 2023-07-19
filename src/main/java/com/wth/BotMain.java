package com.wth;


import com.wth.entity.chart.ChatGTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class BotMain {
//    @Autowired

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(BotMain.class, args);
//        Object chartConfig = run.getBean("chartConfig");
//        System.out.println(chartConfig);
//        Thread thread = new Thread(new ChatGTP());
//        thread.start();
//        System.out.println("开始");
//        String date = ChatGTP.addMsg("现在日期", null);
//        System.out.println(date);
    }
}