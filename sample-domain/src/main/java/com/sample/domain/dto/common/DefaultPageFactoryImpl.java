package com.sample.domain.dto.common;

import java.util.List;

/**
 * 페이지 팩토리의 디폴트 구현
 */
public class DefaultPageFactoryImpl implements PageFactory {

    /**
     * 인스턴스를 생성해서 반환한다.
     * 
     * @param data
     * @param pageable
     * @param count
     * @param <T>
     * @return
     */
    public <T> Page<T> create(List<T> data, Pageable pageable, long count) {
        return new PageImpl<>(data, pageable, count);
    }
}
