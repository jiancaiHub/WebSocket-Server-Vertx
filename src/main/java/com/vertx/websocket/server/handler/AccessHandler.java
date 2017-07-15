package com.vertx.websocket.server.handler;


import com.vertx.websocket.server.common.Constants;
import com.vertx.websocket.server.common.Producer;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by jiancai.wang on 2017/4/19.
 */
public class AccessHandler {

    private Vertx vertx;

    public AccessHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    public void handle(RoutingContext routingContext) {
        // get topic form request
        String topicInfo = routingContext.request().getParam(Constants.DEFAULT_TOPIC_KEY);
        String[] topicArray = Objects.isNull(topicInfo) ? Constants.DEFAULT_TOPIC_LIST : topicInfo.trim().split(",");

        Producer producer = new Producer(vertx, routingContext.request().upgrade(), Stream.of(topicArray).collect(Collectors.toList()));
        producer.onOpen().access();
    }

}
