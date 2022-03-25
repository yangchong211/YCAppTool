package com.yc.store.model;

import java.util.Collections;
import java.util.Map;

/**
 * Data and metadata for an entry returned by the cache.
 */
public class Entry {

    /**
     * The data returned from cache.
     */
    public byte[] data;

    /**
     * ETag for cache coherency.
     */
    public String eTag;

    /**
     * Date of this response as reported by the server.
     */
    public long serverDate;

    /**
     * The last modified date for the requested object.
     */
    public long lastModified;

    /**
     * TTL for this record.
     */
    public long ttl;

    /**
     * Soft TTL for this record.
     */
    public long softTtl;

    /**
     * Immutable response headers as received from server; must be non-null.
     */
    public Map<String, String> responseHeaders = Collections.emptyMap();

    /**
     * True if the entry is expired.
     */
    public boolean isExpired() {
        return this.ttl < System.currentTimeMillis();
    }

    /**
     * True if a refresh is needed from the original data source.
     */
    public boolean refreshNeeded() {
        return this.softTtl < System.currentTimeMillis();
    }

}
