package com.vertx.websocket.main;

import com.vertx.websocket.server.verticles.AcceptorVerticle;
import com.vertx.websocket.server.verticles.HeartbeatVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by jiancai.wang on 2017/4/19.
 */
public class StartWebSocket {

    private static final Logger log = LoggerFactory.getLogger(StartWebSocket.class);

    public static void main(String[] args) {


        // some init params
        JsonObject conf = new JsonObject();

        ClusterManager clusterManager = new ZookeeperClusterManager();
        VertxOptions options = new VertxOptions()
                .setClusterManager(clusterManager)
                .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                // 初始化vertx
                Vertx vertx = res.result();
                log.info("vertx initialized successfully, isClustered: {} detail: {} ", vertx.isClustered(), vertx);
                // deploy
                DeploymentOptions deploymentOptions = new DeploymentOptions();
                deploymentOptions.setConfig(conf);
                // acceptor request
                vertx.deployVerticle(AcceptorVerticle.class.getName(), deploymentOptions);
                // heartbeat
                vertx.deployVerticle(HeartbeatVerticle.class.getName(), deploymentOptions);
            } else {
                log.error("Failed to init vertx, {}", res.cause());
            }
        });
    }
}
