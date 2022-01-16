package com.sample.web.admin.controller.html.system.roles;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRoleForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = 7979636448439604680L;

    Long id;

    // 역할키
    String roleKey;

    // 역할명
    String roleName;
}
