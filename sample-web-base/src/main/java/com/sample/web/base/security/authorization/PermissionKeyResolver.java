package com.sample.web.base.security.authorization;

public interface PermissionKeyResolver {

    /**
     * 권한 키를 해결한다.
     * 
     * @param handler
     * @return
     */
    String resolve(Object handler);
}
