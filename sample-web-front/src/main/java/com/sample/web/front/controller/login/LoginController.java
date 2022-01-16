package com.sample.web.front.controller.login;

import static com.sample.web.base.WebConst.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.web.base.controller.html.AbstractHtmlController;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그인
 */
@Controller
@Slf4j
public class LoginController extends AbstractHtmlController {

    @Override
    public String getFunctionName() {
        return "F_Login";
    }

    /**
     * 초기표시
     *
     * @return
     */
    @GetMapping(LOGIN_URL)
    public String index(@ModelAttribute LoginForm form) {
        return "login/login";
    }

    /**
     * 입력체크
     *
     * @param form
     * @param result
     * @return
     */
    @PostMapping(LOGIN_URL)
    public String index(@Validated @ModelAttribute LoginForm form, BindingResult result) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 되돌아간다
        if (result.hasErrors()) {
            return "login/login";
        }

        return "forward:" + LOGIN_PROCESSING_URL;
    }

    /**
     * 로그인성공
     *
     * @param form
     * @param attributes
     * @return
     */
    @RequestMapping(LOGIN_SUCCESS_URL)
    public String loginSuccess(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("login.success"));
        return "redirect:/";
    }

    /**
     * 로그인실패
     *
     * @param form
     * @param model
     * @return
     */
    @RequestMapping(LOGIN_FAILURE_URL)
    public String loginFailure(@ModelAttribute LoginForm form, Model model) {
        model.addAttribute(GLOBAL_MESSAGE, getMessage("login.failed"));
        return "login/login";
    }

    /**
     * 로그아웃
     *
     * @return
     */
    @RequestMapping(LOGOUT_URL)
    public String logout(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("logout.success"));
        return "redirect:/login";
    }

    @Override
    public boolean authorityRequired() {
        return false;
    }
}
