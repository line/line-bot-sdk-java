package com.linecorp.bot.spring.boot.custom;

import com.linecorp.bot.client.LineMessagingClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kimjh on 2017-04-14.
 */
public class LineMessagingClientFactory
{
    private final ConcurrentMap<String, LineMessagingClient> lineMessagingClientMap;

    public LineMessagingClientFactory(Map<String, LineMessagingClient> map)
    {
        lineMessagingClientMap = new ConcurrentHashMap<>();
        lineMessagingClientMap.putAll(map);
    }

    public LineMessagingClient get(String key)
    {
        return lineMessagingClientMap.get(key);
    }
}
