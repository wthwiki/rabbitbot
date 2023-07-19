package com.wth.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatResp implements Serializable {

    private int result;
    private String content;
}
