package com.sample.web.base.controller.html;

import static com.sample.web.base.WebConst.MAV_ERRORS;

import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.common.FunctionNameAware;
import com.sample.web.base.controller.BaseController;
import com.sample.web.base.security.authorization.Authorizable;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 베이스 HTML컨트롤러
 */
@Slf4j
public abstract class AbstractHtmlController extends BaseController implements FunctionNameAware, Authorizable {

    @Override
    public boolean authorityRequired() {
        return true;
    }

    /**
     * 입력 체크 오류가 있는 경우는 true를 반환한다.
     *
     * @param model
     * @return
     */
    public boolean hasErrors(Model model) {
        val errors = model.asMap().get(MAV_ERRORS);

        if (errors != null && errors instanceof BeanPropertyBindingResult) {
            val br = ((BeanPropertyBindingResult) errors);

            if (br.hasErrors()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 리다이렉트할 곳에 입력 오류를 전달한다.
     *
     * @param attributes
     * @param result
     */
    public void setFlashAttributeErrors(RedirectAttributes attributes, BindingResult result) {
        attributes.addFlashAttribute(MAV_ERRORS, result);
    }
}
