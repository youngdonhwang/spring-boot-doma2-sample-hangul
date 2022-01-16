package com.sample.web.admin.controller.html.users.users;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.List;

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
import com.sample.domain.dto.system.UploadFile;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.service.users.UserService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.util.MultipartFileUtils;
import com.sample.web.base.view.CsvView;
import com.sample.web.base.view.ExcelView;
import com.sample.web.base.view.PdfView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자관리
 */
@Controller
@RequestMapping("/users/users")
@SessionAttributes(types = { SearchUserForm.class, UserForm.class })
@Slf4j
public class UserHtmlController extends AbstractHtmlController {

    @Autowired
    UserFormValidator userFormValidator;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ModelAttribute("userForm")
    public UserForm userForm() {
        return new UserForm();
    }

    @ModelAttribute("searchUserForm")
    public SearchUserForm searchUserForm() {
        return new SearchUserForm();
    }

    @InitBinder("userForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(userFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_USER";
    }

    /**
     * 등록화면 초기표시
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newUser(@ModelAttribute("userForm") UserForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttribute에 남아있는 경우는 재생성한다
            model.addAttribute("userForm", new UserForm());
        }

        return "modules/users/users/new";
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
    public String newUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
            RedirectAttributes attributes) {

        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/new";
        }

        // 입력값으로부터 DTO를 작성한다
        val inputUser = modelMapper.map(form, User.class);
        val password = form.getPassword();

        // 패스워드를 해시화한다
        inputUser.setPassword(passwordEncoder.encode(password));

        // 등록한다
        val createdUser = userService.create(inputUser);

        return "redirect:/users/users/show/" + createdUser.getId();
    }

    /**
     * 리스트화면 초기표시
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findUser(@ModelAttribute SearchUserForm form, Model model) {
        // 입력값을 채워넣는다
        val criteria = modelMapper.map(form, UserCriteria.class);

        // 10건씩 취득한다
        val pages = userService.findAll(criteria, form);

        // 화면에 검색결과를 건넨다
        model.addAttribute("pages", pages);

        return "modules/users/users/find";
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
    public String findUser(@Validated @ModelAttribute("searchUserForm") SearchUserForm form, BindingResult br,
            RedirectAttributes attributes) {

        // 입력체크 오류가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/find";
        }

        return "redirect:/users/users/find";
    }

    /**
     * 상세화면
     *
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/show/{userId}")
    public String showUser(@PathVariable Long userId, Model model) {
        // 1건 취득한다
        val user = userService.findById(userId);
        model.addAttribute("user", user);

        if (user.getUploadFile() != null) {
            // 첨부파일을 취득한다
            val uploadFile = user.getUploadFile();

            // Base64디코딩하여 압축을 푼다
            val base64data = uploadFile.getContent().toBase64();
            val sb = new StringBuilder().append("data:image/png;base64,").append(base64data);

            model.addAttribute("image", sb.toString());
        }

        return "modules/users/users/show";
    }

    /**
     * 편집화면 초기표시
     *
     * @param userId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable Long userId, @ModelAttribute("userForm") UserForm form, Model model) {

        // 세션으로부터 취득할 수 있는 경우는, 다시 읽어들이지 않는다
        if (!hasErrors(model)) {
            // 1건 취득한다
            val user = userService.findById(userId);

            // 취득한 Dto를 Form에 채워넣는다
            modelMapper.map(user, form);
        }

        return "modules/users/users/new";
    }

    /**
     * 편집화면 갱신처리
     *
     * @param form
     * @param br
     * @param userId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{userId}")
    public String editUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
            @PathVariable Long userId, SessionStatus sessionStatus, RedirectAttributes attributes) {

        // 입력체크 오륲가 있는 경우는, 원래의 화면으로 돌아간다
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/edit/" + userId;
        }

        // 갱신대상을 취득한다
        val user = userService.findById(userId);

        // 입력값을 채워넣는다
        modelMapper.map(form, user);

        val image = form.getUserImage();
        if (image != null && !image.isEmpty()) {
            val uploadFile = new UploadFile();
            MultipartFileUtils.convert(image, uploadFile);
            user.setUploadFile(uploadFile);
        }

        // 갱신한다
        val updatedUser = userService.update(user);

        // 세션 userForm을 클리어한다
        sessionStatus.setComplete();

        return "redirect:/users/users/show/" + updatedUser.getId();
    }

    /**
     * 삭제처리
     *
     * @param userId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{userId}")
    public String removeUser(@PathVariable Long userId, RedirectAttributes attributes) {
        // 논리삭제한다
        userService.delete(userId);

        // 삭제성공 메시지
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/users/users/find";
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
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // 채워넣는다
        List<UserCsv> csvList = modelMapper.map(users.getData(), toListType(UserCsv.class));

        // CSV스키마 클래스, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new CsvView(UserCsv.class, csvList, filename);

        return new ModelAndView(view);
    }

    /**
     * Excel다운로드
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/download/{filename:.+\\.xlsx}")
    public ModelAndView downloadExcel(@PathVariable String filename) {
        // 전건 취득한다
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // Excel 북 생성 콜백, 데이터, 다운로드 시의 파일명을 지정한다
        val view = new ExcelView(new UserExcel(), users.getData(), filename);

        return new ModelAndView(view);
    }

    /**
     * PDF다운로드
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/download/{filename:.+\\.pdf}")
    public ModelAndView downloadPdf(@PathVariable String filename) {
        // 모든 건을 취득한다
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // 서류 레이아웃, 데이터, 다운로드 시의 파일 명을 지정한다
        val view = new PdfView("reports/users.jrxml", users.getData(), filename);

        return new ModelAndView(view);
    }
}
