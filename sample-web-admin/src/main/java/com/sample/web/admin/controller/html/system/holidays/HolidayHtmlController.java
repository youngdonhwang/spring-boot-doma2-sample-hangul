package com.sample.web.admin.controller.html.system.holidays;

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
import com.sample.domain.dto.system.Holiday;
import com.sample.domain.dto.system.HolidayCriteria;
import com.sample.domain.service.system.HolidayService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 공휴일관리
 */
@Controller
@RequestMapping("/system/holidays")
@SessionAttributes(types = { SearchHolidayForm.class, HolidayForm.class })
@Slf4j
public class HolidayHtmlController extends AbstractHtmlController {

    @Autowired
    HolidayFormValidator holidayFormValidator;

    @Autowired
    HolidayService holidayService;

    @ModelAttribute("holidayForm")
    public HolidayForm holidayForm() {
        return new HolidayForm();
    }

    @ModelAttribute("searchHolidayForm")
    public SearchHolidayForm searchHolidayForm() {
        return new SearchHolidayForm();
    }

    @InitBinder("holidayForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(holidayFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_Holiday";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newHoliday(@ModelAttribute("holidayForm") HolidayForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 재생성한다
            model.addAttribute("holidayForm", new HolidayForm());
        }

        return "modules/system/holidays/new";
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
    public String newHoliday(@Validated @ModelAttribute("holidayForm") HolidayForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/holidays/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputHoliday = modelMapper.map(form, Holiday.class);

        // 등록한다
        val createdHoliday = holidayService.create(inputHoliday);

        return "redirect:/system/holidays/show/" + createdHoliday.getId();
    }

    /**
     * 리스트화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findHoliday(@ModelAttribute("searchHolidayForm") SearchHolidayForm form, Model model) {
        // 입력값으로부터 검색조건을 작성한다
        val criteria = modelMapper.map(form, HolidayCriteria.class);

        // 10건씩 취득한다
        val pages = holidayService.findAll(criteria, form);

        // 화면에 검색 결과를 건넨다
        model.addAttribute("pages", pages);

        return "modules/system/holidays/find";
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
    public String findHoliday(@Validated @ModelAttribute("searchHolidayForm") SearchHolidayForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다.
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/holidays/find";
        }

        return "redirect:/system/holidays/find";
    }

    /**
     * 상세화면
     *
     * @param holidayId
     * @param model
     * @return
     */
    @GetMapping("/show/{holidayId}")
    public String showHoliday(@PathVariable Long holidayId, Model model) {
        // 1건 취득한다
        val holiday = holidayService.findById(holidayId);
        model.addAttribute("holiday", holiday);
        return "modules/system/holidays/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param holidayId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{holidayId}")
    public String editHoliday(@PathVariable Long holidayId, @ModelAttribute("holidayForm") HolidayForm form,
            Model model) {
        // 세션으로부터 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val holiday = holidayService.findById(holidayId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(holiday, form);
        }

        return "modules/system/holidays/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param holidayId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{holidayId}")
    public String editHoliday(@Validated @ModelAttribute("holidayForm") HolidayForm form, BindingResult br,
            @PathVariable Long holidayId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/holidays/edit/" + holidayId;
        }

        // 갱신 대상을 취득한다
        val holiday = holidayService.findById(holidayId);

        // 입력값을 채워넣는다
        modelMapper.map(form, holiday);

        // 갱신한다
        val updatedHoliday = holidayService.update(holiday);

        // 세션 holidayForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/holidays/show/" + updatedHoliday.getId();
    }

    /**
     * 삭제처리
     *
     * @param holidayId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{holidayId}")
    public String removeHoliday(@PathVariable Long holidayId, RedirectAttributes attributes) {
        // 논리삭제한다
        holidayService.delete(holidayId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/holidays/find";
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
        val holidays = holidayService.findAll(new HolidayCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<HolidayCsv> csvList = modelMapper.map(holidays.getData(), toListType(HolidayCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new CsvView(HolidayCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
