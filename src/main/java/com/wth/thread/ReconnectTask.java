package com.wth.thread;

import com.wth.client.MsgClient;

public class ReconnectTask implements Runnable{
    @Override
    public void run() {
        while(true){
            if(MsgClient.connect("ws://127.0.0.1:8887")){
                break;
            }else{
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行这个线程的run方法
     */
    public static void execute(){
        new Thread(new ReconnectTask()).start();
    }
}
