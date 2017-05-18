package com.vertx.websocket.server.common;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
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
    // 上线时间
    private Date timestamp;
    // 发送消息计数器
    private final AtomicLong receivedMsgCounter = new AtomicLong(0);
    //
    private MessageConsumer<byte[]> messageConsumer;

    public Consumer(Vertx vertx, ServerWebSocket webSocket, String topic) {
        this.vertx = vertx;
        this.webSocket = webSocket;
        this.topic = topic;
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
                String str = new String(bytes.body());
                webSocket.writeFinalTextFrame(str);
            }
        });
        return this;
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

    public String getTopic() {
        return topic;
    }
}
