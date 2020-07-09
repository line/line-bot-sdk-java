package com.linecorp.bot.spring.boot;

import java.util.function.Consumer;

import okhttp3.OkHttpClient;

public interface OkHttpClientConfigurer extends Consumer<OkHttpClient.Builder> {

}
