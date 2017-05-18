package com.vertx.websocket.server.handler;

import com.vertx.websocket.server.common.Constants;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.RoutingContext;

/**
 * 用于监控页面的
 * <p>
 * Created by jiancai.wang on 2017/4/22.
 */
public class MaintainHandler {

    private Vertx vertx;
    private long periodicId;

    public MaintainHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    public void handle(RoutingContext routingContext) {

        /**
         * 需要暴露什么
         * 1. cluster info: server list
         * 2. 发布订阅的info: topic list、sender list、consumer list
         * 3. 发送消息数据和接受消息数
         */
        MetricsService metricsService = MetricsService.create(vertx);
        ServerWebSocket webSocket = routingContext.request().upgrade();
        periodicId = vertx.setPeriodic(Constants.MAINTAIN_METRICS_PERIOD, handler -> {
            //
            // event info
            JsonObject eventBusSnapshot = metricsService.getMetricsSnapshot(vertx.eventBus());
            // TODO : filter uninterested info
            webSocket.writeFinalTextFrame(eventBusSnapshot.toString());
        });
        webSocket.closeHandler(handle -> {
            if (periodicId != 0) vertx.cancelTimer(periodicId);
        });

    }
}
