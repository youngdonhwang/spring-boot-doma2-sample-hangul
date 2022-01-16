package com.sample.common.util;

import org.slf4j.MDC;

public class MDCUtils {

    /**
     * MDC의 값을 설정한다.
     *
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 미설정인 경우에만 MDCの의 값을 설정한다.
     * 
     * @param key
     * @param value
     */
    public static void putIfAbsent(String key, String value) {
        if (MDC.get(key) == null)
            MDC.put(key, value);
    }

    /**
     * MDC의 값을 삭제한다.
     * 
     * @param key
     */
    public static void remove(String key) {
        MDC.remove(key);
    }
}
