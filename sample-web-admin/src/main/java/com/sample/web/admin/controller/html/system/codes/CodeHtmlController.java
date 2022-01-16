package com.sample.web.admin.controller.html.system.codes;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.helper.CodeHelper;
import com.sample.domain.service.system.CodeService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드관리
 */
@Controller
@RequestMapping("/system/codes")
@SessionAttributes(types = { SearchCodeForm.class, CodeForm.class })
@Slf4j
public class CodeHtmlController extends AbstractHtmlController {

    @Autowired
    CodeFormValidator codeFormValidator;

    @Autowired
    CodeService codeService;

    @Autowired
    CodeHelper codeHelper;

    @ModelAttribute("codeForm")
    public CodeForm codeForm() {
        return new CodeForm();
    }

    @ModelAttribute("searchCodeForm")
    public SearchCodeForm searchCodeForm() {
        return new SearchCodeForm();
    }

    @InitBinder("codeForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(codeFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_CODE";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newCode(@ModelAttribute("codeForm") CodeForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 재생성한다
            model.addAttribute("codeForm", new CodeForm());
        }

        return "modules/system/codes/new";
    }

    /**
     * 등록처리
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/new")
    public String newCode(@Validated @ModelAttribute("codeForm") CodeForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력체크오류가 있는 ある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/codes/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputCode = modelMapper.map(form, Code.class);

        // 등록한다
        val createdCode = codeService.create(inputCode);

        return "redirect:/system/codes/show/" + createdCode.getId();
    }

    /**
     * 리스트화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findCode(@ModelAttribute("searchCodeForm") SearchCodeForm form, Model model) {
        // 입력값으로부터 검색조건을 작성한다
        val criteria = modelMapper.map(form, CodeCriteria.class);

        // 10건씩 취득한다
        val pages = codeService.findAll(criteria, form);

        // 화면에 검색 결과를 반환한다
        model.addAttribute("pages", pages);

        // 카테고리 분류 리스트
        val codeCategories = codeHelper.getCodeCategories();
        model.addAttribute("codeCategories", codeCategories);

        return "modules/system/codes/find";
    }

    /**
     * 검색결과
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/find")
    public String findCode(@Validated @ModelAttribute("searchCodeForm") SearchCodeForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/codes/find";
        }

        return "redirect:/system/codes/find";
    }

    /**
     * 상세화면
     *
     * @param codeId
     * @param model
     * @return
     */
    @GetMapping("/show/{codeId}")
    public String showCode(@PathVariable Long codeId, Model model) {
        // 1件取得する
        val code = codeService.findById(codeId);
        model.addAttribute("code", code);
        return "modules/system/codes/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param codeId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{codeId}")
    public String editCode(@PathVariable Long codeId, @ModelAttribute("codeForm") CodeForm form, Model model) {
        // 세션에서 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val code = codeService.findById(codeId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(code, form);
        }

        return "modules/system/codes/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param codeId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{codeId}")
    public String editCode(@Validated @ModelAttribute("codeForm") CodeForm form, BindingResult br,
            @PathVariable Long codeId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/codes/edit/" + codeId;
        }

        // 갱신 대상을 취득한다
        val code = codeService.findById(codeId);

        // 입력값을 채워넣는다
        modelMapper.map(form, code);

        // 갱신한다
        val updatedCode = codeService.update(code);

        // 세션의 codeForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/codes/show/" + updatedCode.getId();
    }

    /**
     * 삭제처리
     *
     * @param codeId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{codeId}")
    public String removeCode(@PathVariable Long codeId, RedirectAttributes attributes) {
        // 논리삭제한다
        codeService.delete(codeId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/codes/find";
    }

    /**
     * CSV다운로드
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+\\.csv}")
    public ModelAndView downloadCsv(@PathVariable String filename) {
        // 전건 취득한다
        val codes = codeService.findAll(new CodeCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<CodeCsv> csvList = modelMapper.map(codes.getData(), toListType(CodeCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드시의 파일명을 지정한다
        val view = new CsvView(CodeCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
