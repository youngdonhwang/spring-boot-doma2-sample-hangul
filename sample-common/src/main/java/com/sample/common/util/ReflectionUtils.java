package com.sample.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * 리플렉션 관련 유틸리티
 */
@Slf4j
public class ReflectionUtils {

    /**
     * 지정한 어노테이션이 부여되어 있는 필드를 반환한다.
     *
     * @param clazz
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> Stream<Field> findWithAnnotation(Class<?> clazz, Class<A> annotationType) {
        return Optional.ofNullable(clazz.getDeclaredFields()).map(Arrays::stream).orElseGet(Stream::empty)
                .filter(f -> f.isAnnotationPresent(annotationType));
    }

    /**
     * 지정한 필드 값을 반환한다.
     *
     * @param f
     * @param obj
     * @return
     */
    public static Object getFieldValue(Field f, Object obj) {

        try {
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            // ignore
        }

        return null;
    }
}
