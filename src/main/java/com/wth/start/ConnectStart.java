package com.wth.start;

import com.wth.client.MsgClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 程序可以理解从这里开始
 */
@Component
public class ConnectStart implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 客户端连接本地的8887端口，也就是go-cq服务器；
        if(!MsgClient.connect("ws://127.0.0.1:8887")){
            // 失败重连
            MsgClient.reConnect();
        }
    }
}
