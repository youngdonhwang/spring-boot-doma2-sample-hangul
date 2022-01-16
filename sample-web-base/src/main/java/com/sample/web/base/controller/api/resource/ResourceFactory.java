package com.sample.web.base.controller.api.resource;

/**
 * 리소스 팩토리
 */
public interface ResourceFactory {

    /**
     * 인스턴스를 작성한다.
     *
     * @return
     */
    Resource create();
}
