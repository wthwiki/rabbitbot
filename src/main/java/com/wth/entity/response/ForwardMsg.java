package com.wth.entity.response;

@lombok.Data
public class ForwardMsg {
    private String type = "node";
    private Data data;

    @lombok.Data
    public static class Data{
        private String name;
        private String uin;
        private String content;
    }
}
