package com.vertx.websocket.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiancai.wang on 2017/6/27.
 */
public class UrlPathUtil {

    public static Map<String, String> paresParams(String query) {
        Map<String, String> params = new HashMap<>();
        String[] paths = query.split("&");
        for (String path : paths) {
            String[] pars = path.split("=");
            params.put(pars[0], pars[1]);
        }
        return params;
    }

}
