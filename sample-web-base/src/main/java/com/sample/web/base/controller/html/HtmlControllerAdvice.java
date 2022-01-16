package com.sample.web.base.controller.html;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.sample.domain.dto.common.ID;
import com.sample.web.base.controller.IDTypeEditor;

import lombok.extern.slf4j.Slf4j;

/**
 * HTML 컨트롤러 어드바이스
 */
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
@Slf4j
public class HtmlControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        // 문자열 필드가 미입력인 경우에, 빈문자열이 아닌 NULL을 바인드한다
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        // 문자열을 ID로 변환한다.
        binder.registerCustomEditor(ID.class, new IDTypeEditor());

        // id컬럼을 입력값으로 덮어쓰지 않는다
        binder.setDisallowedFields("*.id");

        // version컬럼을 입력값으로 덮어쓰지 않는다
        binder.setDisallowedFields("*.version");
    }
}
