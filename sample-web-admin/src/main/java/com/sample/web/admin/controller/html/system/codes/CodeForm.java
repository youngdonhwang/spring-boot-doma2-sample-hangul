package com.sample.web.admin.controller.html.system.codes;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeForm extends BaseForm {

    private static final long serialVersionUID = 7555305356779221873L;

    Long id;

    // 코드키
    @NotEmpty
    String codeKey;

    // 코드명
    @NotEmpty
    String codeValue;

    // 코드별칭
    String codeAlias;

    // 표시순
    @NotNull
    Integer displayOrder;

    // 무효플래그
    Boolean isInvalid;
}
