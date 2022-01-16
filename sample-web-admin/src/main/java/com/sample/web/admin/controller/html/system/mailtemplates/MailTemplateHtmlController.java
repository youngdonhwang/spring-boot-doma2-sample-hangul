package com.sample.web.admin.controller.html.system.mailtemplates;

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
import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.service.system.MailTemplateService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 메일템플릿 관리
 */
@Controller
@RequestMapping("/system/mailtemplates")
@SessionAttributes(types = { SearchMailTemplateForm.class, MailTemplateForm.class })
@Slf4j
public class MailTemplateHtmlController extends AbstractHtmlController {

    @Autowired
    MailTemplateFormValidator mailTemplateFormValidator;

    @Autowired
    MailTemplateService mailTemplateService;

    @ModelAttribute("mailTemplateForm")
    public MailTemplateForm mailTemplateForm() {
        return new MailTemplateForm();
    }

    @ModelAttribute("searchMailTemplateForm")
    public SearchMailTemplateForm searchMailTemplateForm() {
        return new SearchMailTemplateForm();
    }

    @InitBinder("mailTemplateForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(mailTemplateFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_MAIL_TEMPLATE";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newMailtemplate(@ModelAttribute("mailTemplateForm") MailTemplateForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 재생성한다
            model.addAttribute("mailTemplateForm", new MailTemplateForm());
        }

        return "modules/system/mailtemplates/new";
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
    public String newMailtemplate(@Validated @ModelAttribute("mailTemplateForm") MailTemplateForm form,
            BindingResult br, RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/mailtemplates/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputMailtemplate = modelMapper.map(form, MailTemplate.class);

        // 등록한다
        val createdMailtemplate = mailTemplateService.create(inputMailtemplate);

        return "redirect:/system/mailtemplates/show/" + createdMailtemplate.getId();
    }

    /**
     * 리스트화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findMailtemplate(@ModelAttribute SearchMailTemplateForm form, Model model) {
        // 입력값을 채워넣는다
        val criteria = modelMapper.map(form, MailTemplateCriteria.class);

        // 10건별로 취득한다
        val pages = mailTemplateService.findAll(criteria, form);

        // 화면에 검색결과를 건넨다
        model.addAttribute("pages", pages);

        return "modules/system/mailtemplates/find";
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
    public String findMailtemplate(@Validated @ModelAttribute("searchMailTemplateForm") SearchMailTemplateForm form,
            BindingResult br, RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/mailtemplates/find";
        }

        return "redirect:/system/mailtemplates/find";
    }

    /**
     * 상세화면
     *
     * @param mailTemplateId
     * @param model
     * @return
     */
    @GetMapping("/show/{mailTemplateId}")
    public String showMailtemplate(@PathVariable Long mailTemplateId, Model model) {
        // 1건 취득한다
        val mailTemplate = mailTemplateService.findById(mailTemplateId);
        model.addAttribute("mailTemplate", mailTemplate);
        return "modules/system/mailtemplates/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param mailTemplateId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{mailTemplateId}")
    public String editMailtemplate(@PathVariable Long mailTemplateId,
            @ModelAttribute("mailTemplateForm") MailTemplateForm form, Model model) {
        // 세션으로부터 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득하기
            val mailTemplate = mailTemplateService.findById(mailTemplateId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(mailTemplate, form);
        }

        return "modules/system/mailtemplates/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param mailTemplateId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{mailTemplateId}")
    public String editMailtemplate(@Validated @ModelAttribute("mailTemplateForm") MailTemplateForm form,
            BindingResult br, @PathVariable Long mailTemplateId, SessionStatus sessionStatus,
            RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다.
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/mailtemplates/edit/" + mailTemplateId;
        }

        // 갱신대상을 취득한다
        val mailTemplate = mailTemplateService.findById(mailTemplateId);

        // 입력값을 채워넣는다
        modelMapper.map(form, mailTemplate);

        // 갱신한다
        val updatedMailTemplate = mailTemplateService.update(mailTemplate);

        // 세션 mailTemplateForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/mailtemplates/show/" + updatedMailTemplate.getId();
    }

    /**
     * 삭제처리
     * 
     * @param mailTemplateId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{mailTemplateId}")
    public String removeMailTemplate(@PathVariable Long mailTemplateId, RedirectAttributes attributes) {
        // 논리삭제한다
        mailTemplateService.delete(mailTemplateId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/mailtemplates/find";
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
        val mailTemplates = mailTemplateService.findAll(new MailTemplateCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<MailTemplateCsv> csvList = modelMapper.map(mailTemplates.getData(), toListType(MailTemplateCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new CsvView(MailTemplateCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
