package com.iustu.vertxagent.dubbo.model;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

public class CommonHolder {

    // key: requestId     value: CommonFuture
    public static final AttributeKey<Map<Long, CommonFuture>> rpcFutureMapKey = AttributeKey.valueOf("CommonFutureMap");

//    private static final ConcurrentHashMap<ChannelId, HashMap<Long, CommonFuture>> channelMap = new ConcurrentHashMap<>();

//    public static final Object lock = new Object();

    public static void registerFuture(Channel channel, long requestId, CommonFuture commonFuture) {
//        HashMap<Long, CommonFuture> futureMap = channelMap.computeIfAbsent(channel.id(), k -> new HashMap<>());
//        futureMap.put(requestId, commonFuture);
        Attribute<Map<Long, CommonFuture>> attr = channel.attr(rpcFutureMapKey);
        Map<Long, CommonFuture> commonFutureMap = attr.get();
        if (commonFutureMap == null) {
            commonFutureMap = new HashMap<>();
            attr.setIfAbsent(commonFutureMap);
        }
        commonFutureMap.put(requestId, commonFuture);
//        CommonFuture oldFuture = commonFutureMap.put(requestId, commonFuture);
//        if (oldFuture != null) {
//            throw new IllegalStateException("requestId " + requestId + " exists");
//        }
    }

    public static CommonFuture getAndRemoveFuture(Channel channel, long requestId) {
//        HashMap<Long, CommonFuture> futureMap = channelMap.computeIfAbsent(channel.id(), k -> new HashMap<>());
//        return futureMap.remove(requestId);
        Attribute<Map<Long, CommonFuture>> attr = channel.attr(rpcFutureMapKey);
        Map<Long, CommonFuture> futureMap = attr.get();
        if (futureMap == null) {
            futureMap = new HashMap<>();
            attr.setIfAbsent(futureMap);
        }
        return futureMap.remove(requestId);
    }
}
