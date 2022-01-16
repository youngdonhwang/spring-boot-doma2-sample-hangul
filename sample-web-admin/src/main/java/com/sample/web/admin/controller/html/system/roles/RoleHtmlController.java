package com.sample.web.admin.controller.html.system.roles;

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

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;
import com.sample.domain.service.system.PermissionService;
import com.sample.domain.service.system.RoleService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 역할관리
 */
@Controller
@RequestMapping("/system/roles")
@SessionAttributes(types = { RoleForm.class })
@Slf4j
public class RoleHtmlController extends AbstractHtmlController {

    @Autowired
    RoleFormValidator roleFormValidator;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @ModelAttribute("roleForm")
    public RoleForm roleForm() {
        return new RoleForm();
    }

    @ModelAttribute("searchRoleForm")
    public SearchRoleForm searchRoleForm() {
        return new SearchRoleForm();
    }

    @InitBinder("roleForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(roleFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_ROLE";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newRole(@ModelAttribute("roleForm") RoleForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아 있는 경우는 재생성한다
            model.addAttribute("roleForm", new RoleForm());
        }

        // 권한리스트를 취득한다
        Page<Permission> permissions = permissionService.findAll(new PermissionCriteria(), Pageable.NO_LIMIT);
        model.addAttribute("permissions", permissions);

        return "modules/system/roles/new";
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
    public String newRole(@Validated @ModelAttribute("roleForm") RoleForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/roles/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputRole = modelMapper.map(form, Role.class);

        // 등록한다
        val createdRole = roleService.create(inputRole);

        return "redirect:/system/roles/show/" + createdRole.getId();
    }

    /**
     * 리스트화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findRole(@ModelAttribute SearchRoleForm form, Model model) {
        // 입력값을 채워넣는다.
        val criteria = modelMapper.map(form, RoleCriteria.class);

        // 10건별로 취득한다.
        val pages = roleService.findAll(criteria, Pageable.DEFAULT);

        // 화면에 검색결과를 건넨다.
        model.addAttribute("pages", pages);

        return "modules/system/roles/find";
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
    public String findRole(@Validated @ModelAttribute("searchRoleForm") SearchRoleForm form, BindingResult br,
            RedirectAttributes attributes) {
        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/roles/find";
        }

        return "redirect:/system/roles/find";
    }

    /**
     * 상세화면
     *
     * @param roleId
     * @param model
     * @return
     */
    @GetMapping("/show/{roleId}")
    public String showRole(@PathVariable Long roleId, Model model) {
        // 1건 취득한다
        val role = roleService.findById(roleId);
        model.addAttribute("role", role);

        // 권한 리스트를 취득한다
        Page<Permission> permissions = permissionService.findAll(new PermissionCriteria(), Pageable.NO_LIMIT);
        model.addAttribute("permissions", permissions);

        return "modules/system/roles/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param roleId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{roleId}")
    public String editRole(@PathVariable Long roleId, @ModelAttribute("roleForm") RoleForm form, Model model) {
        // 세션으로부터 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val role = roleService.findById(roleId);

            // 취득한 Dto를 Formに에 채워넣는다
            modelMapper.map(role, form);
        }

        // 권한 리스트를 취득한다
        Page<Permission> permissions = permissionService.findAll(new PermissionCriteria(), Pageable.NO_LIMIT);
        model.addAttribute("permissions", permissions);

        return "modules/system/roles/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param roleId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{roleId}")
    public String editRole(@Validated @ModelAttribute("roleForm") RoleForm form, BindingResult br,
            @PathVariable Long roleId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 입력 체크 오륲가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/system/roles/edit/" + roleId;
        }

        // 갱신 대상을 취득한다
        val role = roleService.findById(roleId);

        // 입력값을 채워넣는다
        modelMapper.map(form, role);

        // 갱신한다
        val updatedRole = roleService.update(role);

        // 세션 roleForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/system/roles/show/" + updatedRole.getId();
    }

    /**
     * 삭제처리
     *
     * @param roleId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{roleId}")
    public String removeRole(@PathVariable Long roleId, RedirectAttributes attributes) {
        // 논리삭제한다
        roleService.delete(roleId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/roles/find";
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
        val roles = roleService.findAll(new RoleCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<RoleCsv> csvList = modelMapper.map(roles.getData(), toListType(RoleCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new CsvView(RoleCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
