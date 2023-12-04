

package com.my.app;

public enum FEISHU_GROUP {

    EVENT("https://open.feishu.cn/open-apis/bot/v2/hook/ac8c0a86-ef8c","oVbgGcoEXg")
    ;

    String url;
    String secret;

    FEISHU_GROUP(String url, String secret) {
        this.url = url;
        this.secret = secret;
    }
}
