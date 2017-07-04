package com.vertx.websocket.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiancai.wang on 2017/6/27.
 */
public class UrlPathUtil {

    public static Map<String, String> paresParams(String uri) {
        Map<String, String> params = new HashMap<>();
        if (uri.indexOf("&") > -1) {
            String[] paths = uri.substring(uri.indexOf("?") + 1, uri.length()).split("&");
            for (String path : paths) {
                String[] pars = path.split("=");
                params.put(pars[0], pars[1]);
            }
        }
        return params;

    }
}
