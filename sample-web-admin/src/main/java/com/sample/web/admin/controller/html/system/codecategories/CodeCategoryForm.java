package com.sample.web.admin.controller.html.system.codecategories;

import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeCategoryForm extends BaseForm {

    private static final long serialVersionUID = -7942742528754164062L;

    Long id;

    // 코드분류키
    @NotEmpty
    String categoryKey;

    // 코드분류명
    @NotEmpty
    String categoryName;
}
