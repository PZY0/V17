package com.qianfeng.v17msg.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author pangzhenyu
 * @Date 2019/11/27
 */
public class ChannelUtils {
    private static final Map<String, Channel> map = new ConcurrentHashMap<>();

    public static void add(String userId,Channel channel){
        map.put(userId,channel);
    }

    public static void remove(String userId){
        map.remove(userId);
    }

    public static void remove(Channel channel){
        Set<Map.Entry<String, Channel>> entries = map.entrySet();
        for (Map.Entry<String, Channel> entry : entries) {
            if(entry.getValue() == channel){
                map.remove(entry.getKey());
                return;
            }
        }
    }

    public static Channel getChannel(String userId){
        return map.get(userId);
    }
}
