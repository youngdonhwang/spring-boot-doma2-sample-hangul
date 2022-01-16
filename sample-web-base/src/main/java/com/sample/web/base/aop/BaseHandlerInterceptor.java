package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.web.base.controller.api.AbstractRestController;

import lombok.val;

/**
 * 베이스 인터셉터
 */
public abstract class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 컨트롤러의 동작 전
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 컨트롤러의 동작 후

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 처리 완료 후
    }

    /**
     * RestController인지 아닌지의 여부를 나타내는 값을 반환한다.
     * 
     * @param handler
     * @return
     */
    protected boolean isRestController(Object handler) {
        val bean = getBean(handler, AbstractRestController.class);

        if (bean instanceof AbstractRestController) {
            return true;
        }

        return false;
    }

    /**
     * 인수인 객체가 지정 클래스인지 아닌지의 여부를 반환한다.
     *
     * @param obj
     * @param clazz
     * @return
     */
    protected boolean isInstanceOf(Object obj, Class<?> clazz) {

        if (clazz.isAssignableFrom(obj.getClass())) {
            return true;
        }

        return false;
    }

    /**
     * Handler의 Bean을 반환한다.
     *
     * @param handler
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T getBean(Object handler, Class<T> clazz) {

        if (handler != null && handler instanceof HandlerMethod) {
            val hm = ((HandlerMethod) handler).getBean();

            if (clazz.isAssignableFrom(hm.getClass())) {
                return (T) hm;
            }

            return null;
        }

        return null;
    }
}
