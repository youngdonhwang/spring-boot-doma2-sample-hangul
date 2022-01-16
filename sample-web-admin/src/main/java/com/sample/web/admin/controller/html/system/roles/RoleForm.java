package com.sample.web.admin.controller.html.system.roles;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleForm extends BaseForm {

    private static final long serialVersionUID = 7555305356779221873L;

    Long id;

    // 역할키
    @NotEmpty
    String roleKey;

    // 역할명
    @NotEmpty
    String roleName;

    // 권한
    Map<Integer, Boolean> permissions;
}
