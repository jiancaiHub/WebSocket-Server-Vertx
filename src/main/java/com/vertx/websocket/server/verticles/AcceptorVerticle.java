package com.vertx.websocket.server.verticles;


import com.vertx.websocket.server.handler.AccessHandler;
import com.vertx.websocket.server.handler.MaintainHandler;
import com.vertx.websocket.server.handler.MonitorHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiancai.wang on 2017/4/19.
 */
public class AcceptorVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(AcceptorVerticle.class);

    private static final String SERVER_PATH_MONITOR = "/iov/websocket/monitor";
    private static final String SERVER_PATH_ACCESS = "/iov/websocket/access";
    private static final String SERVER_PATH_MAINTAIN = "/iov/websocket/maintain";

    @Override
    public void start() throws Exception {

        String serverHost = config().getString("serverHost");
        Integer serverPort = config().getInteger("serverPort");

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        // cross-domain
        router.route().handler(BodyHandler.create());
        CorsHandler corsHandler = CorsHandler.create("*");
        corsHandler.allowedMethod(HttpMethod.GET);
        corsHandler.allowedMethod(HttpMethod.POST);
        corsHandler.allowedMethod(HttpMethod.PUT);
        corsHandler.allowedMethod(HttpMethod.DELETE);
        corsHandler.allowedHeader("Authorization");
        corsHandler.allowedHeader("Content-Type");
        corsHandler.allowedHeader("Access-Control-Allow-Origin");
        corsHandler.allowedHeader("Access-Control-Allow-Headers");
        router.route().handler(corsHandler);

        AccessHandler accessHandler = new AccessHandler(vertx);
        MonitorHandler monitorHandler = new MonitorHandler(vertx);
        MaintainHandler maintainHandler = new MaintainHandler(vertx);

        router.route(SERVER_PATH_ACCESS).handler(accessHandler::handle);
        router.route(SERVER_PATH_MONITOR).handler(monitorHandler::handle);
        router.route(SERVER_PATH_MAINTAIN).handler(maintainHandler::handle);


        httpServer.requestHandler(router::accept).listen(serverPort);

        log.info("AcceptorVerticle is ready, Listening host: {}, port: {}", serverHost, serverPort);
    }
}
