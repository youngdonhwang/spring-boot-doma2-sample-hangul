package com.sample.domain.dto.common;

import java.util.List;

/**
 * 페이지네이션을 이용하고 있는 경우에, 페이지 수나 현재의 페이지 수를 갖고 있는다.
 * 
 * @param <T>
 */
public interface Page<T> extends Pageable {

    /**
     * 데이터를 반환한다.
     * 
     * @return
     */
    List<T> getData();

    /**
     * 데이터 건수를 반환한다.
     * 
     * @return
     */
    long getCount();

    /**
     * 페이지 수를 반환한다.
     *
     * @return
     */
    int getTotalPages();
}
