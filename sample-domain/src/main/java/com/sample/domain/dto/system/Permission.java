package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "permissions")
@Entity
@Getter
@Setter
public class Permission extends DomaDtoImpl {

    private static final long serialVersionUID = -258501373358638948L;

    // 권한ID
    @Id
    @Column(name = "permission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 권한카테고리
    String categoryKey;

    // 권한키
    String permissionKey;

    // 권한명
    String permissionName;
}
