
package com.my.app;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Response;

public class FeishuWebHook {
    public static void send(FEISHU_GROUP fs, String contentText) {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> emitter) throws Throwable {
                Map<String, String> headers = new HashMap<>();
                JSONObject content = null;
                try {

                    headers.put("Content-Type", "application/json");
                    content = new JSONObject();
                    content.put("text", contentText);
                    int timestamp = (int) (System.currentTimeMillis() / 1000);
                    String sign = GenSign(fs.secret, timestamp);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", content.toString());
                    jsonObject.put("msg_type", "text");
                    jsonObject.put("timestamp", timestamp);
                    jsonObject.put("sign", sign);
                    Response response = OkHttpUtils.get().requestBody(headers, fs.url, jsonObject.toString());
                    if (response != null) {
                        int code = response.code();
                        String bodyString = response.body().string();
                    } else {
                    }
                } catch (Exception e) {
                } finally {
                    headers.clear();
                    if (content != null) {
                        content.clear();
                    }
                    headers = null;
                    content = null;
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Throwable {

            }
        });
    }

    private static String GenSign(String secret, int timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        // 把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        // 使用HmacSHA256算法计算签名
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.getEncoder().encode(signData));
    }
}
