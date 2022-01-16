package com.sample.web.base.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.util.WebUtils;

import lombok.val;

public class SessionUtils {

    /**
     * 지정한 속성명으로 값을 취득한다.
     *
     * @param request
     * @param attributeName
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String attributeName) {
        T ret = null;
        val session = getSession(request);
        val mutex = getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                ret = (T) session.getAttribute(attributeName);
            }
        }
        return ret;
    }

    /**
     * 지정한 속성명으로 값을 설정한다.
     *
     * @param request
     * @param attributeName
     * @param value
     * @return
     */
    public static void setAttribute(HttpServletRequest request, String attributeName, Object value) {
        val session = getSession(request);
        val mutex = getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                session.setAttribute(attributeName, value);
            }
        }
    }

    /**
     * mutex를 취득한다.
     *
     * @param request
     * @return
     */
    public static Object getMutex(HttpServletRequest request) {
        val session = getSession(request);

        if (session != null) {
            val mutex = WebUtils.getSessionMutex(session);
            return mutex;
        }

        return null;
    }

    /**
     * 존재하는 세션을 취득한다.
     *
     * @param request
     * @return
     */
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false);
    }
}
