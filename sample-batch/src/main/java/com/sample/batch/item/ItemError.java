package com.sample.batch.item;

import lombok.Getter;
import lombok.Setter;

/**
 * 처리 대상의 오류 메시지
 */
@Setter
@Getter
public class ItemError {

    String sourceName;

    int position;

    String errorMessage;
}
