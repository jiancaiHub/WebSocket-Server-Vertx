package com.vertx.websocket.server.common;

/**
 * Created by jiancai.wang on 2017/4/19.
 */
public abstract class Constants {

    public static final String DEFAULT_TOPIC_KEY = "topic";
    public static final String DEFAULT_TOPIC = "default";
    public static final String[] DEFAULT_TOPIC_LIST = {"default"};
    public static final long MAINTAIN_METRICS_PERIOD = 10 * 1000L;
    public static final long HEARTBEAT_PERIOD = 10 * 1000L;
    public static final String HEARTBEAT_REST_URL = "iov/rest/common/server/ws/heartbeat";

}
