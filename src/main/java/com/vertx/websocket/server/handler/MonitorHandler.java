package com.vertx.websocket.server.handler;

import com.vertx.websocket.server.common.Constants;
import com.vertx.websocket.server.common.Consumer;
import com.vertx.websocket.server.util.UrlPathUtil;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

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
        Map<String, String> params = UrlPathUtil.paresParams(routingContext.request().uri());
        // get topic and rm the
        String topic = params.getOrDefault(Constants.DEFAULT_TOPIC_KEY, Constants.DEFAULT_TOPIC);
        if (params.containsKey(Constants.DEFAULT_TOPIC_KEY)) params.remove(Constants.DEFAULT_TOPIC_KEY);
        Consumer consumer = new Consumer(vertx, routingContext.request().upgrade(), topic, params);
        consumer.onOpen().onConsume();

    }
}
