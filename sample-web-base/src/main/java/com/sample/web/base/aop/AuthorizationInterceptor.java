package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.sample.web.base.security.authorization.PermissionKeyResolver;
import com.sample.web.base.util.WebSecurityUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationInterceptor extends BaseHandlerInterceptor {

    @Autowired
    PermissionKeyResolver permissionKeyResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 컨트롤러의 동작전
        if (isRestController(handler)) {
            // API의 경우는 스킵한다
            return true;
        }

        val permissionKey = permissionKeyResolver.resolve(handler);

        // 권한 키를 SpEL로 평가한다
        if (permissionKey != null && !WebSecurityUtils.hasAuthority(permissionKey)) {
            String loginId = WebSecurityUtils.getLoginId();
            throw new AccessDeniedException(
                    "permission denied. [loginId=" + loginId + ", permissionKey=" + permissionKey + "]");
        }

        return true;
    }
}
