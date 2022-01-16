package com.sample.domain.dao;

/**
 * 이중 송신 방지 체크 토큰 홀더
 */
public class DoubleSubmitCheckTokenHolder {

    private static final ThreadLocal<String> EXPECTED_TOKEN = new ThreadLocal<>();

    private static final ThreadLocal<String> ACTUAL_TOKEN = new ThreadLocal<>();

    /**
     * 토큰을 보관한다.
     *
     * @param expected
     * @param actual
     */
    public static void set(String expected, String actual) {
        EXPECTED_TOKEN.set(expected);
        ACTUAL_TOKEN.set(actual);
    }

    /**
     * 세션에 보관되어 있던 토큰을 반환한다.
     *
     * @return
     */
    public static String getExpectedToken() {
        return EXPECTED_TOKEN.get();
    }

    /**
     * 화면으로부터 건네온 토큰을 반환한다.
     *
     * @return
     */
    public static String getActualToken() {
        return ACTUAL_TOKEN.get();
    }

    /**
     * 감사 정보를 클리어한다.
     */
    public static void clear() {
        EXPECTED_TOKEN.remove();
        ACTUAL_TOKEN.remove();
    }
}
