package com.sample.web.base.controller.html;

import static com.sample.web.base.WebConst.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.sample.common.util.MessageUtils;
import com.sample.domain.exception.DoubleSubmitErrorException;
import com.sample.domain.exception.FileNotFoundException;
import com.sample.domain.exception.NoDataFoundException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 화면 기능용 예외 핸들러
 */
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
@Slf4j
public class HtmlExceptionHandler {

    private static final String VIEW_ATTR_STACKTRACE = "trace";

    /**
     * 파일、데이터 부재시의 예외를 핸들링한다
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ FileNotFoundException.class, NoDataFoundException.class })
    public String handleNotFoundException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("not found.", e);
        }

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + NOTFOUND_URL;
    }

    /**
     * 권한 부족 오류를 핸들링한다
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ AccessDeniedException.class })
    public String handleAccessDeniedException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("forbidden.", e);
        }

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + FORBIDDEN_URL;
    }

    /**
     * 낙관적 배타제어에 의해 발생하는 예외를 핸들링한다
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ OptimisticLockingFailureException.class })
    public RedirectView handleOptimisticLockingFailureException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("optimistic locking failure.", e);
        }

        // 공통 메시지를 취득한다
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = OPTIMISTIC_LOCKING_FAILURE_ERROR;
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }

    /**
     * 이중 송신 방지 체크에 의해 발생하는 예외를 핸들링한다
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ DoubleSubmitErrorException.class })
    public RedirectView handleDoubleSubmitErrorException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("double submit error.");
        }

        // 공통 메시지를 취득한다
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = DOUBLE_SUBMIT_ERROR;
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }

    /**
     * 예기치못한 예외를 핸들링한다
     * 
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public String handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        // TODO
        // 핸들할 예외가 있는 경우는 조건 분기한다
        log.error("unhandled runtime exception.", e);

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + ERROR_URL;
    }

    /**
     * 리다이렉트할 곳에서 글로벌 메시지를 표시한다
     *
     * @param request
     * @param response
     * @param locale
     * @param messageCode
     * @return
     */
    protected RedirectView getRedirectView(HttpServletRequest request, HttpServletResponse response, Locale locale,
            String messageCode) {
        // 메시지를 천이할 곳에 표시한다
        val message = MessageUtils.getMessage(messageCode, locale);
        outputFlashMap(request, response, GLOBAL_MESSAGE, message);

        val requestURI = request.getRequestURI();
        val view = new RedirectView(requestURI);

        return view;
    }

    /**
     * 스택 추적을 문자열로 반환한다
     * 
     * @param e
     * @return
     */
    protected String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * FlashMap에 값을 기록한다.
     * 
     * @param request
     * @param response
     * @param attr
     * @param value
     */
    protected void outputFlashMap(HttpServletRequest request, HttpServletResponse response, String attr, String value) {
        val flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(attr, value);

        // flashMap을 기록한다
        val flashManager = RequestContextUtils.getFlashMapManager(request);
        flashManager.saveOutputFlashMap(flashMap, request, response);
    }
}
