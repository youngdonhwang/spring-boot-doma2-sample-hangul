package com.sample.web.base.aop;

import static com.sample.web.base.WebConst.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.sample.common.util.MessageUtils;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.repository.system.CodeCategoryRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetModelAndViewInterceptor extends BaseHandlerInterceptor {

    @Autowired
    CodeCategoryRepository codeCategoryRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 컨트롤러 동작 후
        if (isRestController(handler)) {
            // API의 경우는 스킵한다
            return;
        }

        if (modelAndView == null) {
            return;
        }

        val locale = LocaleContextHolder.getLocale();
        val pulldownOption = MessageUtils.getMessage(MAV_PULLDOWN_OPTION, locale);

        // 상수 정의를 화면에 건넨다
        Map<String, Object> constants = new HashMap<>();
        constants.put(MAV_PULLDOWN_OPTION, pulldownOption);
        modelAndView.addObject(MAV_CONST, constants);

        // 정형 리스트
        val codeCategories = getCodeCategories();
        modelAndView.addObject(MAV_CODE_CATEGORIES, codeCategories);

        // 입력 오류를 화면 객체에 설정한다
        retainValidateErrors(modelAndView);
    }

    /**
     * 코드 분류 리스트를 화면에 설정한다
     * 
     * @return
     */
    protected List<CodeCategory> getCodeCategories() {
        // 코드 분류를 모두 취득한다
        return codeCategoryRepository.fetchAll();
    }

    /**
     * 입력 오류를 화면 객체에 설정한다
     * 
     * @param modelAndView
     */
    protected void retainValidateErrors(ModelAndView modelAndView) {
        val model = modelAndView.getModelMap();

        if (model != null && model.containsAttribute(MAV_ERRORS)) {
            val errors = model.get(MAV_ERRORS);

            if (errors != null && errors instanceof BeanPropertyBindingResult) {
                val br = ((BeanPropertyBindingResult) errors);

                if (br.hasErrors()) {
                    val formName = br.getObjectName();
                    val key = BindingResult.MODEL_KEY_PREFIX + formName;
                    model.addAttribute(key, errors);
                    model.addAttribute(GLOBAL_MESSAGE, MessageUtils.getMessage(VALIDATION_ERROR));
                }
            }
        }
    }
}
