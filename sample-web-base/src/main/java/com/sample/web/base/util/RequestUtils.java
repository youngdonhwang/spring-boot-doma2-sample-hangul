package com.sample.web.base.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.val;

public class RequestUtils {

    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public static final String XMLHTTP_REQUEST = "XMLHttpRequest";

    /**
     * User-Agent을 취득한다.
     * 
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return StringUtils.trimToEmpty(request.getHeader(HttpHeaders.USER_AGENT));
    }

    /**
     * Ajax 통신인지를 나타내는 값을 반환한다.
     * 
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        val header = request.getHeader(X_REQUESTED_WITH);
        val isAjax = StringUtils.equalsIgnoreCase(XMLHTTP_REQUEST, header);
        return isAjax;
    }

    /**
     * HttpServletRequest을 반환한다.
     * 
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        val attributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    /**
     * 사이트의 URL을 반환한다.
     * 
     * @return
     */
    public static String getSiteUrl() {
        val servletRequest = getHttpServletRequest();

        String scheme = servletRequest.getScheme();
        String host = servletRequest.getRemoteHost();
        int port = servletRequest.getServerPort();
        String path = servletRequest.getContextPath();

        String siteUrl = null;
        if (port == 80 || port == 443) {
            siteUrl = StringUtils.join(scheme, "://", host, path);
        } else {
            siteUrl = StringUtils.join(scheme, "://", host, ":", String.valueOf(port), path);
        }

        return siteUrl;
    }
}
