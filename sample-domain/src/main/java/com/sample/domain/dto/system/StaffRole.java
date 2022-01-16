package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "staff_roles")
@Entity
@Getter
@Setter
public class StaffRole extends DomaDtoImpl {

    private static final long serialVersionUID = 1780669742437422350L;

    // 담당자역할ID
    @Id
    @Column(name = "staff_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 담당자ID
    Long staffId;

    // 역할키
    String roleKey;

    // 역할명
    String roleName;

    // 권한키
    String permissionKey;

    // 권한명
    String permissionName;
}
