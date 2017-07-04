package com.vertx.websocket.server.common;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jiancai.wang on 2017/4/20.
 */
public class Producer implements Serializable {

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
    public final AtomicLong receivedMsgCounter = new AtomicLong(0);

    public Producer(Vertx vertx, ServerWebSocket webSocket, String topic) {
        this.vertx = vertx;
        this.webSocket = webSocket;
        this.topic = topic;
        this.name = "producer_" + UUID.randomUUID();
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Producer{" +
                "name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public Producer onOpen() {

        log.info("Producer online [ {} ]", this.getName());
        webSocket.closeHandler(close ->
            log.info("Producer offline [ {} ]", this.getName())
        );
        return this;
    }

    public void access() {
        // 消费数据
        webSocket.handler(buf -> {
            receivedMsgCounter.incrementAndGet();
            byte[] bytes = buf.copy().getBytes();
            vertx.eventBus().publish(topic, bytes);
        });
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }
}
