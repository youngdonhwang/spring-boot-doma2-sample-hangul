package com.sample.batch.item;

/**
 * 작업 위치
 */
public interface ItemPosition {

    String getSourceName();

    void setSourceName(String sourceName);

    int getPosition();

    void setPosition(int position);

    default boolean isFirst() {
        return getPosition() == 1;
    }
}
