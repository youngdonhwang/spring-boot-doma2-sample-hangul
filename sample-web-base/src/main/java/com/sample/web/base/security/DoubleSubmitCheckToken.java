package com.sample.web.base.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.LRUMap;

import com.sample.common.XORShiftRandom;
import com.sample.web.base.util.SessionUtils;

import lombok.val;

public class DoubleSubmitCheckToken {

    public static final String DOUBLE_SUBMIT_CHECK_PARAMETER = "_double";

    private static final String DOUBLE_SUBMIT_CHECK_CONTEXT = DoubleSubmitCheckToken.class.getName() + ".CONTEXT";

    // 난수 생성기
    private static final XORShiftRandom random = new XORShiftRandom();

    /**
     * 화면에서 건네진 토큰을 반환한다
     *
     * @param request
     * @return actual token
     */
    public static String getActualToken(HttpServletRequest request) {
        return request.getParameter(DOUBLE_SUBMIT_CHECK_PARAMETER);
    }

    /**
     * 지정 키를 토대로 세션에 보관되어 있는 토큰을 반환한다
     *
     * @param request
     * @return expected token
     */
    @SuppressWarnings("unchecked")
    public static String getExpectedToken(HttpServletRequest request) {
        String token = null;
        val key = request.getRequestURI();

        Object mutex = SessionUtils.getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                token = getToken(request, key);
            }
        }

        return token;
    }

    /**
     * 세션에 토큰을 설정한다
     *
     * @param request
     * @return token
     */
    @SuppressWarnings("unchecked")
    public static String renewToken(HttpServletRequest request) {
        val key = request.getRequestURI();
        val token = generateToken();

        Object mutex = SessionUtils.getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                setToken(request, key, token);
            }
        }

        return token;
    }

    /**
     * 토큰을 생성한다
     *
     * @return token
     */
    public static String generateToken() {
        return String.valueOf(random.nextInt(Integer.MAX_VALUE));
    }

    /**
     * 세션에 보관된 LRUMap을 추출한다. 존재하지 않는 경우는 작성해서 반환한다.
     *
     * @param request
     * @return
     */
    protected static LRUMap getLRUMap(HttpServletRequest request) {
        LRUMap map = SessionUtils.getAttribute(request, DOUBLE_SUBMIT_CHECK_CONTEXT);

        if (map == null) {
            map = new LRUMap(10);
        }

        return map;
    }

    /**
     * 토큰을 취득한다
     *
     * @param request
     * @param key
     * @return
     */
    protected static String getToken(HttpServletRequest request, String key) {
        LRUMap map = getLRUMap(request);
        val token = (String) map.get(key);
        return token;
    }

    /**
     * 토큰을 보관한다
     *
     * @param request
     * @param key
     * @param token
     */
    protected static void setToken(HttpServletRequest request, String key, String token) {
        LRUMap map = getLRUMap(request);
        map.put(key, token);
        SessionUtils.setAttribute(request, DOUBLE_SUBMIT_CHECK_CONTEXT, map);
    }
}
