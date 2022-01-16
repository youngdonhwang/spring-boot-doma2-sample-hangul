package com.sample.web.base.aop;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.sample.domain.dao.DoubleSubmitCheckTokenHolder;
import com.sample.web.base.security.DoubleSubmitCheckToken;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 이중 송신 방지 체크를 위한 토큰을 세션에 설정한다
 */
@Slf4j
public class SetDoubleSubmitCheckTokenInterceptor extends BaseHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 컨트롤러의 동작 전
        val expected = DoubleSubmitCheckToken.getExpectedToken(request);
        val actual = DoubleSubmitCheckToken.getActualToken(request);
        DoubleSubmitCheckTokenHolder.set(expected, actual);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 컨트롤러의 동작 후
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            // POST되었을 때에 토큰이 일치한다면 새로운 토큰을 발생한다
            val expected = DoubleSubmitCheckToken.getExpectedToken(request);
            val actual = DoubleSubmitCheckToken.getActualToken(request);

            if (expected != null && actual != null && Objects.equals(expected, actual)) {
                DoubleSubmitCheckToken.renewToken(request);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 처리 완료 후
        DoubleSubmitCheckTokenHolder.clear();
    }
}
