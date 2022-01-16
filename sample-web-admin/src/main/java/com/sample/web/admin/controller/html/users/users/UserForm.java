package com.sample.web.admin.controller.html.users.users;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.sample.web.base.validator.annotation.ContentType;
import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm extends BaseForm {

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

    // 메일주소
    @NotEmpty
    @Email
    String email;

    // 전화번호
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 우편번호
    String zip;

    // 주소
    String address;

    // 첨부파일
    @ContentType(allowed = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE })
    transient MultipartFile userImage; // serializableではないのでtransientにする
}
