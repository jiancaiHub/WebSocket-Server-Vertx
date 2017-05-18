package com.vertx.websocket.server.verticles;

import com.vertx.websocket.server.common.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by jiancai.wang on 2017/5/9.
 */
public class HeartbeatVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatVerticle.class);
    private static volatile boolean CONSOLE_CONNECT_FLAG = true;

    @Override
    public void start() throws Exception {

        String wsServerCode = config().getString("serverCode");
        String wsHttpServerHost = config().getString("serverHost");
        Integer wsHttpServerPort = config().getInteger("serverPort");
        String remoteHost = config().getString("remoteHost");
        Integer remotePort = config().getInteger("remotePort");


        WebClient webClient = WebClient.create(vertx);
        JsonObject heartbeat = new JsonObject()
                .put("wsServerCode", wsServerCode)
                .put("wsHttpServerHost", wsHttpServerHost)
                .put("wsHttpServerPort", wsHttpServerPort)
                .put("timestamp", new Date().getTime());

        vertx.setPeriodic(Constants.HEARTBEAT_PERIOD, handle -> {
            webClient.post(remotePort, remoteHost, Constants.HEARTBEAT_REST_URL)
                    .sendJsonObject(heartbeat, resp -> {
                        if (resp.failed() && CONSOLE_CONNECT_FLAG) {
                            CONSOLE_CONNECT_FLAG = false;
                            log.error("Failed to send heartbeat to console server, cause: "+ resp.cause());
                        }
                    });
        });
    }
}
