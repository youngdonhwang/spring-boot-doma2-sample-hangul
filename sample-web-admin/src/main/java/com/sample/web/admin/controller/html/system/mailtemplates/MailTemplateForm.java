package com.sample.web.admin.controller.html.system.mailtemplates;

import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MailTemplateForm extends BaseForm {

    private static final long serialVersionUID = -5860252006532570164L;

    Long id;

    // 메일템플릿키
    @NotEmpty
    String templateKey;

    // 메일타이틀
    @NotEmpty
    String subject;

    // 메일본문
    @NotEmpty
    String templateBody;
}
