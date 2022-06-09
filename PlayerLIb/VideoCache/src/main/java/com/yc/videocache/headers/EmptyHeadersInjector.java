package com.yc.videocache.headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Empty {@link HeaderInjector} implementation.
 */
public class EmptyHeadersInjector implements HeaderInjector {

    @Override
    public Map<String, String> addHeaders(String url) {
        return new HashMap<>();
    }

}
