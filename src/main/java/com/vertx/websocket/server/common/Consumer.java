package com.vertx.websocket.server.common;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于数据消费，在数据推送前做相关的过滤或加工
 * <p>
 * Created by jiancai.wang on 2016/11/28.
 */
public class Consumer implements Serializable {

    /**
     * 消费者可以拥有多个Topic, Purchaser会将消费者加到不同主题的Group中
     */
    private Logger log = LoggerFactory.getLogger(Producer.class);
    //
    private Vertx vertx;
    //
    private ServerWebSocket webSocket;
    // 标识符
    private String name;
    // 消息主题
    private String topic;
    // 消息过滤条件
    private Map<String, String> filters;
    // 上线时间
    private Date timestamp;
    // 发送消息计数器
    private final AtomicLong receivedMsgCounter = new AtomicLong(0);
    //
    private MessageConsumer<byte[]> messageConsumer;

    public Consumer(Vertx vertx, ServerWebSocket webSocket, String topic, Map<String, String> filters) {
        this.vertx = vertx;
        this.webSocket = webSocket;
        this.topic = topic;
        this.filters = filters;
        this.name = "customer_" + UUID.randomUUID();
        this.timestamp = new Date();
    }

    public Consumer onOpen() {
        log.info("Consumer online [ {} ]", this.getName());
        webSocket.closeHandler(close -> {
            if (messageConsumer.isRegistered()) messageConsumer.unregister();
            log.info("Consumer offline [ {} ]", this.getName());
        });
        return this;
    }

    public Consumer onConsume() {
        // 推送数据
        messageConsumer = vertx.eventBus().consumer(topic, bytes -> {
            if (bytes != null) {
                receivedMsgCounter.incrementAndGet();
                String msg = filtering(new String(bytes.body()));
                if (msg != null) {
                    webSocket.writeFinalTextFrame(msg);
                }
            }
        });
        return this;
    }

    private String filtering(String msg) {
        String result = null;
        try {
            // 过滤器为空，直接透传
            if (filters.isEmpty()) {
                return msg;
            }
            JsonObject dataJson = new JsonObject(msg);
            // 判断过滤字眼是否命中
            Boolean filterFlag = true;
            Iterator<Map.Entry<String, String>> iterator = filters.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> f = iterator.next();
                if (isHit(dataJson, f.getKey(), f.getValue())) {
                    filterFlag = false;
                }
            }
            if (!filterFlag) result = dataJson.toString();
        } catch (Exception e) {
            log.error("Unable to complete the message filtering. msg: {}, isHit: {}", msg, filters);
        }
        return result;
    }

    private boolean isHit(JsonObject dataJson, String k, String v) {
        if (!dataJson.containsKey(k)) {
            return false;
        } else {
            return dataJson.getValue(k).toString().equals(v);
        }
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getName() {
        return name;
    }


}
