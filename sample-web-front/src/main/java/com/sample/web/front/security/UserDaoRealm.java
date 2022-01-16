package com.sample.web.front.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sample.domain.dto.user.UserCriteria;
import lombok.val;
import org.seasar.doma.jdbc.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dao.users.UserRoleDao;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserRole;
import com.sample.web.base.security.BaseRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * 프론트쪽 인증허가
 */
@Component
@Slf4j
public class UserDaoRealm extends BaseRealm {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Override
    protected UserDetails getLoginUser(String loginId) {
        User user = null;
        List<GrantedAuthority> authorityList = null;

        try {
            // login_id를 메일주소로 간주한다
            val criteria = new UserCriteria();
            criteria.setEmail(loginId);

            // 사용자를 취득하여, 세션에 보관한다
            user = userDao.select(criteria)
                    .orElseThrow(() -> new UsernameNotFoundException("no user found. [id=" + loginId + "]"));

            // 담당자권한을 취득한다
            List<UserRole> userRoles = userRoleDao.selectByUserId(user.getId(), toList());

            // 역할키에 프리픽스를 부여하여 정리한다
            Set<String> roleKeys = userRoles.stream().map(UserRole::getRoleKey).collect(toSet());

            // 권한키를 정리한다
            Set<String> permissionKeys = userRoles.stream().map(UserRole::getPermissionKey).collect(toSet());

            // 역할과 권한을 모두 GrantedAuthority로 건넨다
            Set<String> authorities = new HashSet<>();
            authorities.addAll(roleKeys);
            authorities.addAll(permissionKeys);
            authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

        } catch (Exception e) {
            // 0건 예외가 발생했을 경우는 아무것도 하지 않는다
            // 그 외의 예외는, 인증 오류의 예외로 묶는다
            if (!(e instanceof NoResultException)) {
                throw new UsernameNotFoundException("could not select user.", e);
            }
        }

        return new LoginUser(user, authorityList);
    }
}
