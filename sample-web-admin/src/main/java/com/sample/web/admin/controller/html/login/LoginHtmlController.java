package com.sample.web.admin.controller.html.login;

import static com.sample.web.base.WebConst.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.web.base.controller.html.AbstractHtmlController;

import lombok.extern.slf4j.Slf4j;

/**
 * 관리측 로그인
 */
@Controller
@Slf4j
public class LoginHtmlController extends AbstractHtmlController {

    @Override
    public String getFunctionName() {
        return "A_LOGIN";
    }

    /**
     * 초기 표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping(LOGIN_URL)
    public String index(@ModelAttribute LoginForm form, Model model) {
        return "modules/login/login";
    }

    /**
     * 입력 체크
     * 
     * @param form
     * @param br
     * @return
     */
    @PostMapping(LOGIN_URL)
    public String index(@Validated @ModelAttribute LoginForm form, BindingResult br) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 되돌아간다
        if (br.hasErrors()) {
            return "modules/login/login";
        }

		// 입력 체크를 통과한 경우는, Spring Security의 인증 처리로 포워드한다
		// POST 메소드여야 하므로, forword를 사용할 필요가 있다
        return "forward:" + LOGIN_PROCESSING_URL;
    }

    /**
     * 로그인 성공
     *
     * @param form
     * @param attributes
     * @return
     */
    @PostMapping(LOGIN_SUCCESS_URL)
    public String loginSuccess(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
    	// Spring Security에 의한 인증 처리가 성공하면, 
    	// 설정한 URL으로 포워드하므로,  POST 메소드로 처리하도록 한다
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("login.success"));
        return "redirect:/";
    }

    /**
     * 로그인 실패
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping(LOGIN_FAILURE_URL)
    public String loginFailure(@ModelAttribute LoginForm form, Model model) {
        model.addAttribute(GLOBAL_MESSAGE, getMessage("login.failed"));
        return "modules/login/login";
    }

    /**
     * 타임 아웃했을 때
     * 
     * @param form
     * @param model
     * @return
     */
    @GetMapping(LOGIN_TIMEOUT_URL)
    public String loginTimeout(@ModelAttribute LoginForm form, Model model) {
    	// 독자적으로 구현한 AuthenticationEntryPoint에서 세션 타임 아웃이라고 
    	// 판별되었을 경우는 이 메소드에서 처리한다
    	
        model.addAttribute(GLOBAL_MESSAGE, getMessage("login.timeout"));
        return "modules/login/login";
    }

    /**
     * 로그 아웃
     *
     * @return
     */
    @GetMapping(LOGOUT_SUCCESS_URL)
    public String logout(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
    	// 로그 아웃 처리가 성공하면, 이 메소드에서 처리한다
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("logout.success"));
        return "redirect:/login";
    }

    @Override
    public boolean authorityRequired() {
        // 권한 체크를 요구하지 않는다
        return false;
    }
}
