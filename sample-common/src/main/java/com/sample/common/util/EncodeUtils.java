package com.sample.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * 인코딩 유틸리티
 */
@Slf4j
public class EncodeUtils {

    /**
     * UTF-8로 인코딩한 문자열을 반환한다.
     * 
     * @param filename
     * @return
     */
    public static String encodeUtf8(String filename) {
        String encoded = null;

        try {
            encoded = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            // should never happens
        }

        return encoded;
    }
}
