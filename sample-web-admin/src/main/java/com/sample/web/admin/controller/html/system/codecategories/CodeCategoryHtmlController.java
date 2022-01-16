package com.sample.web.admin.controller.html.system.codecategories;

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
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.helper.CodeHelper;
import com.sample.domain.service.system.CodeCategoryService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드분류관리
 */
@Controller
@RequestMapping("/system/code_categories")
@SessionAttributes(types = { SearchCodeCategoryForm.class, CodeCategoryForm.class })
@Slf4j
public class CodeCategoryHtmlController extends AbstractHtmlController {

    @Autowired
    CodeCategoryFormValidator codeCategoryFormValidator;

    @Autowired
    CodeCategoryService codeCategoryService;

    @Autowired
    CodeHelper codeHelper;

    @ModelAttribute("codeCategoryForm")
    public CodeCategoryForm codeCategoryForm() {
        return new CodeCategoryForm();
    }

    @ModelAttribute("searchCodeCategoryForm")
    public SearchCodeCategoryForm searchcodeCategoryForm() {
        return new SearchCodeCategoryForm();
    }

    @InitBinder("codeCategoryForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(codeCategoryFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_CODE_CATEGORY";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newCode(@ModelAttribute("codeCategoryForm") CodeCategoryForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 다시 생성한다
            model.addAttribute("codeCategoryForm", new CodeCategoryForm());
        }

        return "modules/system/code_categories/new";
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
    public String newCode(@Validated @ModelAttribute("codeCategoryForm") CodeCategoryForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/code_categories/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputCodeCategory = modelMapper.map(form, CodeCategory.class);

        // 등록한다
        val createdCodeCategory = codeCategoryService.create(inputCodeCategory);

        return "redirect:/system/code_categories/show/" + createdCodeCategory.getId();
    }

    /**
     * 리스트 화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findCodeCategory(@ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form, Model model) {
        // 입력값으로부터 검색조건을 작성한다
        val criteria = modelMapper.map(form, CodeCategoryCriteria.class);

        // 10건씩 구분하여 취득한다
        val pages = codeCategoryService.findAll(criteria, form);

        // 화면에 검색 결과를 전달한다
        model.addAttribute("pages", pages);

        // 카테고리 분류 리스트
        val codeCategories = codeHelper.getCodeCategories();
        model.addAttribute("codeCategories", codeCategories);

        return "modules/system/code_categories/find";
    }

    /**
     * 검색 결과
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/find")
    public String findCodeCategory(@Validated @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form,
            BindingResult br, RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/code_categories/find";
        }

        return "redirect:/system/code_categories/find";
    }

    /**
     * 상세 화면
     *
     * @param codeCategoryId
     * @param model
     * @return
     */
    @GetMapping("/show/{codeCategoryId}")
    public String showCodeCategory(@PathVariable Long codeCategoryId, Model model) {
        // 1건 취득한다
        val codeCategory = codeCategoryService.findById(codeCategoryId);
        model.addAttribute("codeCategory", codeCategory);
        return "modules/system/code_categories/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param codeCategoryId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{codeCategoryId}")
    public String editCodeCategory(@PathVariable Long codeCategoryId,
            @ModelAttribute("codeCategoryForm") CodeCategoryForm form, Model model) {
        // 세션에서 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val codeCategory = codeCategoryService.findById(codeCategoryId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(codeCategory, form);
        }

        return "modules/system/code_categories/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param codeCategoryId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{codeCategoryId}")
    public String editCodeCategory(@Validated @ModelAttribute("codeCategoryForm") CodeCategoryForm form,
            BindingResult br, @PathVariable Long codeCategoryId, SessionStatus sessionStatus,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 되돌아간다.
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/code_categories/edit/" + codeCategoryId;
        }

        // 갱신 대상을 취득한다
        val codeCategory = codeCategoryService.findById(codeCategoryId);

        // 입력값을 채워넣는다
        modelMapper.map(form, codeCategory);

        // 갱신한다
        val updatedCode = codeCategoryService.update(codeCategory);

        // 세션의 codeCategoryForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/code_categories/show/" + updatedCode.getId();
    }

    /**
     * 삭제 처리
     *
     * @param codeCategoryId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{codeCategoryId}")
    public String removeCodeCategory(@PathVariable Long codeCategoryId, RedirectAttributes attributes) {
        // 논리 삭제한다
        codeCategoryService.delete(codeCategoryId);

        // 삭제 성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/code_categories/find";
    }

    /**
     * CSV 다운로드
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+\\.csv}")
    public ModelAndView downloadCsv(@PathVariable String filename) {
        // 전건 취득한다
        val codes = codeCategoryService.findAll(new CodeCategoryCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<CodeCategoryCsv> csvList = modelMapper.map(codes.getData(), toListType(CodeCategoryCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다.
        val view = new CsvView(CodeCategoryCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
