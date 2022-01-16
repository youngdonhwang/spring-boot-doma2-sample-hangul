package com.sample.domain.dto.common;

/**
 * 페이징 가능한 것을 나타낸다
 */
public interface Pageable {

    Pageable DEFAULT = new DefaultPageable(1, 10);

    Pageable NO_LIMIT = new DefaultPageable(1, Integer.MAX_VALUE);

    /**
     * 
     * @return
     */
    int getPage();

    /**
     * 
     * @return
     */
    int getPerpage();
}
