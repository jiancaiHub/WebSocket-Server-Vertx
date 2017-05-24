package com.vertx.websocket.server.util;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jiancai.wang on 2017/5/24.
 */
public class PropertiesUtil {

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    public static JsonObject loadProperties(String resourceLocation) {
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
