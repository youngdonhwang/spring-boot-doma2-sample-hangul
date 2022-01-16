package com.sample.domain.exception;

/**
 * 데이터 부존재 오류
 */
public class NoDataFoundException extends RuntimeException {

    private static final long serialVersionUID = -3553226048751584224L;

    /**
     * 생성자
     */
    public NoDataFoundException(String message) {
        super(message);
    }

    /**
     * 생성자
     */
    public NoDataFoundException(Exception e) {
        super(e);
    }
}
