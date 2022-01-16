package com.sample.web.base.security.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

import lombok.val;

/**
 * 컨트롤러의 메소드 명으로 권한 키를 해결한다
 */
public class DefaultPermissionKeyResolver implements PermissionKeyResolver {

    private static final String TYPE_NAME_REPLACEMENT = "HtmlController";

    @Override
    public String resolve(Object handler) {
        String permissionKey = null;

        if (handler instanceof HandlerMethod) {
            val handlerMethod = (HandlerMethod) handler;
            val bean = handlerMethod.getBean();

            if (bean instanceof Authorizable) {
                val typeName = handlerMethod.getBeanType().getSimpleName();
                val typeNamePrefix = StringUtils.remove(typeName, TYPE_NAME_REPLACEMENT);
                val methodName = handlerMethod.getMethod().getName();

                // 인가 제어가 불필요한 기능은, 권한 키를 해결하지 않는다
                if (((Authorizable) bean).authorityRequired()) {
                    permissionKey = String.format("%s.%s", typeNamePrefix, methodName);
                }
            }
        }

        return permissionKey;
    }
}
