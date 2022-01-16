package com.sample.domain.dto.common;

import java.io.Serializable;
import java.util.Objects;

import org.seasar.doma.Domain;

/**
 * ID값을 내포하는 클래스
 * 
 * @param <T>
 */
@Domain(valueType = Integer.class, factoryMethod = "of")
public class ID<T> implements Serializable {

    private static final long serialVersionUID = -8883289947172519834L;

    private static final ID<Object> NOT_ASSIGNED = new ID<>(null);

    private final Integer value;

    /**
     * 생성자
     * 
     * @param value
     */
    private ID(final Integer value) {
        this.value = value;
    }

    /**
     * 내포하고 있는 값을 반환한다.
     * 
     * @return
     */
    public Integer getValue() {
        return value;
    }

    /**
     * 팩토리 메소드
     * 
     * @param value
     * @param <R>
     * @return
     */
    public static <R> ID<R> of(final Integer value) {
        return new ID<>(value);
    }

    /**
     * 채번되어 있지 않은 상태의 ID를 반환한다.
     *
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <R> ID<R> notAssigned() {
        return (ID<R>) NOT_ASSIGNED;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.equals(this, NOT_ASSIGNED)) {
            // NOT_ASSINED끼리의 비교도 false를 반환한다
            return false;
        }

        if (Objects.equals(this, o)) {
            return true;
        }

        final ID<?> id = (ID<?>) o;
        return value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
