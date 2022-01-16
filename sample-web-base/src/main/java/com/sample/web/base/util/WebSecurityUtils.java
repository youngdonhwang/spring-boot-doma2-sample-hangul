package com.sample.web.base.util;

import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.val;

public class WebSecurityUtils {

    private static final SpelParserConfiguration config = new SpelParserConfiguration(true, true);

    private static final SpelExpressionParser parser = new SpelExpressionParser(config);

    /**
     * 인증 정보를 취득한다
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPrincipal() {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        return (T) auth.getPrincipal();
    }

    /**
     * 인수로 지정한 권한을 갖고 있는지, 그것을 나타내는 값을 반환한다
     * 
     * @param a
     * @return
     */
    public static boolean hasAuthority(final String a) {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        val authorities = auth.getAuthorities();

        boolean isAllowed = false;
        for (GrantedAuthority ga : authorities) {
            val authority = ga.getAuthority();
            val expressionString = String.format("'%s' matches '%s'", a, authority);
            val expression = parser.parseExpression(expressionString);

            isAllowed = expression.getValue(Boolean.class);
            if (isAllowed) {
                break;
            }
        }

        return isAllowed;
    }

    /**
     * 로그인ID을 취득한다
     * 
     * @return
     */
    public static String getLoginId() {
        String loginId = null;
        val principal = WebSecurityUtils.getPrincipal();

        if (principal instanceof UserDetails) {
            loginId = ((UserDetails) principal).getUsername();
        }

        return loginId;
    }
}
