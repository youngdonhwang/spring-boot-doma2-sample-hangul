package com.sample.web.base.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.sample.web.base.util.RequestUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 화면을 표시할 때에, 유효하지 하지 않은 세션ID가 전달될 경우는, <br>
 * 타임 아웃했을 경우의 URL로 리다이렉트한다. <br>
 * 단, AJAX 통신의 경우는, 상태 코드만 반환한다.
 */
@Slf4j
public class DefaultAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final String loginTimeoutUrl;

    /**
     *
     * @param loginUrl
     * @param loginTimeoutUrl
     */
    public DefaultAuthenticationEntryPoint(String loginUrl, String loginTimeoutUrl) {
        super(loginUrl);
        this.loginTimeoutUrl = loginTimeoutUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        if (RequestUtils.isAjaxRequest(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        super.commence(request, response, authException);
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
        val url = super.determineUrlToUseForThisRequest(request, response, exception);

        if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
            if (log.isDebugEnabled()) {
                log.debug("세션이 타임 아웃하였습니다.");
            }

            return this.loginTimeoutUrl;
        }

        return url;
    }
}
