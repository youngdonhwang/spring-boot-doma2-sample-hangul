package com.sample.domain.dto.user;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "user_roles")
@Entity
@Getter
@Setter
public class UserRole extends DomaDtoImpl {

    private static final long serialVersionUID = -6750983302974218054L;

    // 담당자역할ID
    @Id
    @Column(name = "user_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 사용자ID
    Long userId;

    // 역할키
    String roleKey;

    // 역할명
    String roleName;

    // 권한키
    String permissionKey;

    // 권한명
    String permissionName;
}
