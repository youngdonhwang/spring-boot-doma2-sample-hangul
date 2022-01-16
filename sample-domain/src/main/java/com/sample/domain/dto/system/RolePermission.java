package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "role_permissions")
@Entity
@Getter
@Setter
public class RolePermission extends DomaDtoImpl {

    private static final long serialVersionUID = 4915898548766398327L;

    @OriginalStates // 차분 UPDATE를 위해 정의한다
    RolePermission originalStates;

    @Id
    @Column(name = "role_permission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 역할키
    String roleKey;

    // 권한ID
    Integer permissionId;
}
