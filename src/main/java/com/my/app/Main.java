package com.my.app;

public class Main {

    public static void main(String[] args) throws Exception{
        FeishuWebHook.send(FEISHU_GROUP.EVENT, "测试");
        Thread.currentThread().join();
    }
}
