package com.sample.web.base.security.authorization;

public interface Authorizable {

    /**
     * 인가를 필요로 하는 기능의 경우는 true를 반환한다.
     * 
     * @return
     */
    boolean authorityRequired();
}
