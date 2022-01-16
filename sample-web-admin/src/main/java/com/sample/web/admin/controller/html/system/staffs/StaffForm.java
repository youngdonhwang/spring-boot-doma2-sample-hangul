package com.sample.web.admin.controller.html.system.staffs;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffForm extends BaseForm {

    private static final long serialVersionUID = -6807767990335584883L;

    Long id;

    // 이름
    @NotEmpty
    String firstName;

    // 성
    @NotEmpty
    String lastName;

    @NotEmpty
    String password;

    @NotEmpty
    String passwordConfirm;

    // 메일 주소
    @NotEmpty
    @Email
    String email;

    // 전화번호
    @Digits(fraction = 0, integer = 10)
    String tel;
}
