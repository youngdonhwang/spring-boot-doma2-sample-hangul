
package com.sample.web.base.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import lombok.Setter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 요청 파라미터의 문자수가 예상외로 많은 경우는 오류 화면으로 천이시킨다
 */
@Slf4j
public class CheckOverflowFilter extends OncePerRequestFilter {

    @Setter
    private int maxLength = 20000;

    private static final String ERROR_URL = "/error";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            val parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                val parameterName = parameterNames.nextElement();
                val value = request.getParameter(parameterName);

                if (value != null && value.length() > maxLength) {
                    redirectToErrorPage(request, response);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("cloud not parse request parameters", e);
            redirectToErrorPage(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        val contextPath = request.getContextPath();
        response.sendRedirect(contextPath + ERROR_URL);
    }
}
