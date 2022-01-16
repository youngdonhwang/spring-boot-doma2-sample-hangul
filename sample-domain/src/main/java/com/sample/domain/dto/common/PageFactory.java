package com.sample.domain.dto.common;

import java.util.List;

/**
 * 페이지 팩토리
 */
public interface PageFactory {

    /**
     * 인스턴스를 생성해서 반환한다.
     * 
     * @param data
     * @param pageable
     * @param count
     * @param <T>
     * @return
     */
    <T> Page<T> create(List<T> data, Pageable pageable, long count);
}
