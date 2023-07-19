package com.wth.entity.chart;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    private static final ArrayBlockingQueue<String> sendMsg=new ArrayBlockingQueue<String>(5);
    public static void main(String[] args) throws InterruptedException {
        sendMsg.put("he");
        String t1 = sendMsg.take();
        System.out.println(t1);

        String t2 = sendMsg.take();
        System.out.println(t2);
    }
}
