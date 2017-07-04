package com.vertx.websocket.server.common;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
    private List<String> topicList;
    // 上线时间
    private Date timestamp;
    // 发送消息计数器
    public final AtomicLong receivedMsgCounter = new AtomicLong(0);

    public Producer(Vertx vertx, ServerWebSocket webSocket, List<String> topicList) {
        this.vertx = vertx;
        this.webSocket = webSocket;
        this.topicList = topicList;
        this.name = "producer_" + UUID.randomUUID();
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Producer{" +
                "name='" + name + '\'' +
                ", topic='" + topicList.stream().collect(Collectors.joining("[", "]", ", ")) + '\'' +
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
        webSocket.handler(buf -> {
            receivedMsgCounter.incrementAndGet();
            byte[] bytes = buf.copy().getBytes();
            for (String topic : topicList) {
                vertx.eventBus().publish(topic, bytes);
            }
        });
    }

    public String getName() {
        return name;
    }
}
