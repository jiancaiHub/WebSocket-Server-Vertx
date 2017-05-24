package com.vertx.websocket.server.handler;

import com.vertx.websocket.server.common.Constants;
import com.vertx.websocket.server.common.Consumer;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

/**
 * Created by jiancai.wang on 2017/4/19.
 */
public class MonitorHandler {

    private Vertx vertx;

    public MonitorHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    public void handle(RoutingContext routingContext) {
        // get topic form request
        String topic = routingContext.request().getParam(Constants.DEFAULT_TOPIC_KEY);
        topic = Objects.isNull(topic) ? Constants.DEFAULT_TOPIC : topic;

        Consumer consumer = new Consumer(vertx, routingContext.request().upgrade(), topic);
        consumer.onOpen().onConsume();

    }
}
