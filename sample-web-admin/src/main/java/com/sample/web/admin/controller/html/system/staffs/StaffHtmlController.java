package com.sample.web.admin.controller.html.system.staffs;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.service.system.StaffService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 담당자 관리
 */
@Controller
@RequestMapping("/system/staffs")
@SessionAttributes(types = { SearchStaffForm.class, StaffForm.class })
@Slf4j
public class StaffHtmlController extends AbstractHtmlController {

    @Autowired
    StaffFormValidator staffFormValidator;

    @Autowired
    StaffService staffService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ModelAttribute("staffForm")
    public StaffForm staffForm() {
        return new StaffForm();
    }

    @ModelAttribute("searchStaffForm")
    public SearchStaffForm searchStaffForm() {
        return new SearchStaffForm();
    }

    @InitBinder("staffForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(staffFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_STAFF";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newStaff(@ModelAttribute("staffForm") StaffForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 재생성한다
            model.addAttribute("staffForm", new StaffForm());
        }

        return "modules/system/staffs/new";
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
    public String newStaff(@Validated @ModelAttribute("staffForm") StaffForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/staffs/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputStaff = modelMapper.map(form, Staff.class);
        val password = form.getPassword();

        // 패스워드를 해시화한다
        inputStaff.setPassword(passwordEncoder.encode(password));

        // 등록한다
        val createdStaff = staffService.create(inputStaff);

        return "redirect:/system/staffs/show/" + createdStaff.getId();
    }

    /**
     * 리스트 화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findStaff(@ModelAttribute SearchStaffForm form, Model model) {
        // 입력값을 채워넣는다
        val criteria = modelMapper.map(form, StaffCriteria.class);

        // 10건씩 취득한다
        val pages = staffService.findAll(criteria, form);

        // 화면에 검색결과를 건넨다
        model.addAttribute("pages", pages);

        return "modules/system/staffs/find";
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
    public String findStaff(@Validated @ModelAttribute("searchStaffForm") SearchStaffForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력 체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/staffs/find";
        }

        return "redirect:/system/staffs/find";
    }

    /**
     * 상세화면
     *
     * @param staffId
     * @param model
     * @return
     */
    @GetMapping("/show/{staffId}")
    public String showStaff(@PathVariable Long staffId, Model model) {
        // 1건 취득한다
        val staff = staffService.findById(staffId);
        model.addAttribute("staff", staff);
        return "modules/system/staffs/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param staffId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{staffId}")
    public String editStaff(@PathVariable Long staffId, @ModelAttribute("staffForm") StaffForm form, Model model) {
        // 세션으로부터 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val staff = staffService.findById(staffId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(staff, form);
        }

        return "modules/system/staffs/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param staffId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{staffId}")
    public String editStaff(@Validated @ModelAttribute("staffForm") StaffForm form, BindingResult br,
            @PathVariable Long staffId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/staffs/edit/" + staffId;
        }

        // 갱신 대상을 취득한다
        val staff = staffService.findById(staffId);

        // 입력값을 채워넣는다
        modelMapper.map(form, staff);
        val password = staff.getPassword();

        if (StringUtils.isNotEmpty(password)) {
            // 패스워드를 해시화한다
            staff.setPassword(passwordEncoder.encode(password));
        }

        // 갱신한다
        val updatedStaff = staffService.update(staff);

        // 세션 staffForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/staffs/show/" + updatedStaff.getId();
    }

    /**
     * 삭제처리
     *
     * @param staffId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{staffId}")
    public String removeStaff(@PathVariable Long staffId, RedirectAttributes attributes) {
        // 논리삭제한다
        staffService.delete(staffId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/staffs/find";
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
        val staffs = staffService.findAll(new StaffCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<StaffCsv> csvList = modelMapper.map(staffs.getData(), toListType(StaffCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new CsvView(StaffCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
