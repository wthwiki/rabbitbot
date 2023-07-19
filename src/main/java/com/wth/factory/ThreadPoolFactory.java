package com.wth.factory;


import com.wth.utils.SystemInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * 线程池
 */
@Slf4j
public class ThreadPoolFactory {
    private ThreadPoolFactory(){}
    // 创建命令处理线程池
    private final static ThreadPoolExecutor commandHandlerThreadPool = new ThreadPoolExecutor(5,20,10L, TimeUnit.MINUTES,new ArrayBlockingQueue(20),
            new CustomizableThreadFactory("pool-handleCommand-"),new ThreadPoolFactory.ShareRunsPolicy());
    // 创建消息事件线程池
    private final static ThreadPoolExecutor eventThreadPool = new ThreadPoolExecutor(5,16,15L, TimeUnit.MINUTES,new ArrayBlockingQueue(80),
            new CustomizableThreadFactory("pool-event-"),new ThreadPoolFactory.ShareRunsPolicy());


    public static void resetThreadPoolSize(){
        int availableProcessors = SystemInfo.AVAILABLE_PROCESSORS;
        if(availableProcessors > 0){
            commandHandlerThreadPool.setCorePoolSize(availableProcessors + 1);
            commandHandlerThreadPool.setMaximumPoolSize(availableProcessors * 3);

            eventThreadPool.setCorePoolSize(availableProcessors + 1);
            eventThreadPool.setMaximumPoolSize(availableProcessors * 2);
            log.info("根据cpu线程数重置线程池容量完成");
        }
    }

    public static Executor getCommandHandlerThreadPool(){
        return commandHandlerThreadPool;
    }

    public static Executor getEventThreadPool(){
        return eventThreadPool;
    }
    public final static ExecutorService sharePool = new ThreadPoolExecutor(1, 1,15L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static class ShareRunsPolicy implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                sharePool.execute(r);
            }
        }
    }
}

