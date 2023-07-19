package com.wth.entity.response;

import lombok.Data;

/**
 * WebSocket JSON 格式
 * @param <T>
 */
@Data
public class Request<T> {
    // 事件类型
    private String action;
    // 事件的参数
    private T params;
    // 来甄别 "这个响应是哪个请求发出的", 你可以为每一个请求都使用一个唯一标识符来甄别
    private String echo;
}
