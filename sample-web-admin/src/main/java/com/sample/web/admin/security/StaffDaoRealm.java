package com.sample.web.admin.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dao.system.StaffRoleDao;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.dto.system.StaffRole;
import com.sample.web.base.security.BaseRealm;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리측 인증 인가
 */
@Component
@Slf4j
public class StaffDaoRealm extends BaseRealm {

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    @Override
    protected UserDetails getLoginUser(String email) {
        Staff staff = null;
        List<GrantedAuthority> authorityList = null;

        try {
            // login_id를 메일 주소로 사용한다
            val criteria = new StaffCriteria();
            criteria.setEmail(email);

            // 담당자를 취득하여 세션에 보관한다
            staff = staffDao.select(criteria)
                    .orElseThrow(() -> new UsernameNotFoundException("no staff found [id=" + email + "]"));

            // 담당자 권한을 취득한다
            List<StaffRole> staffRoles = staffRoleDao.selectByStaffId(staff.getId(), toList());

            // 역할 키에 접두사를 붙여서 정리한다
            Set<String> roleKeys = staffRoles.stream().map(StaffRole::getRoleKey).collect(toSet());

            // 권한 키를 정리한다
            Set<String> permissionKeys = staffRoles.stream().map(StaffRole::getPermissionKey).collect(toSet());

            // 역할과 권한을 모두 GrantedAuthority로 건넨다
            Set<String> authorities = new HashSet<>();
            authorities.addAll(roleKeys);
            authorities.addAll(permissionKeys);
            authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

            return new LoginStaff(staff, authorityList);

        } catch (Exception e) {
            if (!(e instanceof UsernameNotFoundException)) {
                // 입력이 틀린 것 이외의 예외는 로그를 출력한다
                log.error("failed to getLoginUser. ", e);
                throw e;
            }

            // 0건 예외가 슬로우된 경우는 아무것도 하지 않는다
            // 그 외의 예외는 인증 오류의 예외에 포함
            throw new UsernameNotFoundException("could not select staff.", e);
        }
    }
}
