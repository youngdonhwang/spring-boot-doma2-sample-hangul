package com.sample.domain.dto.system;

import java.util.Map;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "roles")
@Entity
@Getter
@Setter
public class Role extends DomaDtoImpl {

    private static final long serialVersionUID = 4825745231712286767L;

    @OriginalStates // 차분 UPDATE를 위해 정의한다
    Role originalStates;

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 역할키
    String roleKey;

    // 역할명
    String roleName;

    // 권한
    @Transient
    Map<Integer, Boolean> permissions;
}
