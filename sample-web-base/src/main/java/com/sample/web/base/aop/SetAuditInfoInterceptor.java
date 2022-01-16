package com.sample.web.base.aop;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 유저를 감사 정보 홀더에 설정한다
 */
@Slf4j
public class SetAuditInfoInterceptor extends BaseHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 컨트롤러 동작 전
        val now = LocalDateTime.now();

        // 아직 로그인하지 않은 경우는, 게스트 취급한다
        AuditInfoHolder.set("GUEST", now);

        // 로그인 유저가 존재하는 경우
        getLoginUser().ifPresent(loginUser -> {
            // 監査情報を設定する
            AuditInfoHolder.set(loginUser.getUsername(), now);
        });

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 컨트롤러 동작 후

        // 감사 정보를 클리어한다
        AuditInfoHolder.clear();
    }

    /**
     * 로그인 유저를 취득한다
     *
     * @return
     */
    protected Optional<UserDetails> getLoginUser() {
        val auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                return ofNullable((UserDetails) principal);
            }
        }

        return Optional.empty();
    }
}
