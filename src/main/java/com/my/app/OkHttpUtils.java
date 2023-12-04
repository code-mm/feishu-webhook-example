

package com.my.app;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {

    private static volatile OkHttpUtils instance;

    public static long CONNECTTIMEOUT = 60;

    public static long READTIMEOUT = 60;

    public static long WRITETIMEOUT = 60;

    private OkHttpClient client;

    private OkHttpUtils() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(CONNECTTIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(READTIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS);
            builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(@NotNull String s) {
                    System.out.println("debug http:" + s);
                }
            }));
            client = builder.build();
        }
    }

    public static OkHttpUtils get() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                instance = new OkHttpUtils();
            }
        }
        return instance;
    }

    public static final MediaType mediaType = MediaType.Companion.parse("application/json; charset=utf-8");

    public Response requestBody(Map<String, String> headers, String url, String body) {

        try {
            Request.Builder request = new Request.Builder();
            if (headers != null) {
                for (String k : headers.keySet()) {
                    request.addHeader(k, headers.get(k));
                }
            }
            request.url(url);
            RequestBody requestBody = RequestBody.Companion.create(body, mediaType);
            request.post(requestBody);
            return client.newCall(request.build()).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OkHttpClient getClient() {
        return client;
    }

}
