package com.sample.web.base.controller.api.resource;

/**
 * 리소스 팩토리의 디폴트 구현
 */
public class DefaultResourceFactoryImpl implements ResourceFactory {

    /**
     * 인스턴스를 작성한다.
     *
     * @return
     */
    @Override
    public Resource create() {
        return new ResourceImpl();
    }
}
