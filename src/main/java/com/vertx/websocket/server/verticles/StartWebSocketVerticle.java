package com.vertx.websocket.server.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Created by jiancai.wang on 2017/5/10.
 */
public class StartWebSocketVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(StartWebSocketVerticle.class);
    private static final String DEFAULT_CONFIG_RES = "configs/default-websocket.json";

    @Override
    public void start() throws Exception {

        // load config
        JsonObject config = Objects.isNull(config()) ? config() : loadProperties(DEFAULT_CONFIG_RES);
        Vertx vertx = getVertx();
        log.info("vertx initialized successfully, isClustered: {} detail: {} ", vertx.isClustered(), vertx);
        // deploy
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(config);
        // acceptor request
        vertx.deployVerticle(AcceptorVerticle.class.getName(), deploymentOptions);
        // heartbeat
        vertx.deployVerticle(HeartbeatVerticle.class.getName(), deploymentOptions);
    }


    private static JsonObject loadProperties(String resourceLocation) {
        JsonObject properties = new JsonObject();
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(resourceLocation);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            properties = new JsonObject(new String(bytes));
            log.info("Loaded websocket.json file from resourceLocation=" + resourceLocation);
        } catch (FileNotFoundException e) {
            log.error("Could not find websocket config file", e);
        } catch (IOException e) {
            log.error("Failed to load websocket config", e);
        }
        return properties;
    }
}
