package com.sample.web.base.security;

import static com.sample.web.base.WebConst.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sample.web.base.security.rememberme.MultiDeviceRememberMeServices;
import com.sample.web.base.security.rememberme.MultiDeviceTokenRepository;
import com.sample.web.base.security.rememberme.PurgePersistentLoginTask;
import com.sample.web.base.util.RequestUtils;

import lombok.val;

/**
 * 기저 보안 설정
 */
public abstract class BaseSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.security.secureCookie:false}")
    Boolean secureCookie;

    @Value("${application.security.rememberMe.cookieName:rememberMe}")
    String rememberMeCookieName;

    @Value("${application.security.tokenValiditySeconds:86400}")
    Integer tokenValiditySeconds;

    @Value("${application.security.tokenPurgeSeconds:2592000}") // 30 days
    Integer tokenPurgeSeconds;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    private static final String REMEMBER_ME_KEY = "sampleRememberMeKey";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MultiDeviceTokenRepository multiDeviceTokenRepository() {
        val tokenRepository = new MultiDeviceTokenRepository();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public MultiDeviceRememberMeServices multiDeviceRememberMeServices() {
        val rememberMeService = new MultiDeviceRememberMeServices(REMEMBER_ME_KEY, userDetailsService(),
                multiDeviceTokenRepository());
        rememberMeService.setParameter("rememberMe");
        rememberMeService.setCookieName(rememberMeCookieName);
        rememberMeService.setUseSecureCookie(secureCookie);
        rememberMeService.setTokenValiditySeconds(tokenValiditySeconds);
        return rememberMeService;
    }

    @Bean
    public PurgePersistentLoginTask purgePersistentLoginTask() {
        val task = new PurgePersistentLoginTask();
        task.setTokenPurgeSeconds(tokenPurgeSeconds);
        task.setDataSource(dataSource);
        return task;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적 파일로의 액세스는 인증을 걸지 않는다
        web.ignoring()//
                .antMatchers(WEBJARS_URL, STATIC_RESOURCES_URL);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)//
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Cookie에 CSRF토큰을 보관한다
        http.csrf()//
                .csrfTokenRepository(new CookieCsrfTokenRepository());

        String[] permittedUrls = { LOGIN_TIMEOUT_URL, FORBIDDEN_URL, ERROR_URL, NOTFOUND_URL, RESET_PASSWORD_URL,
                CHANGE_PASSWORD_URL };

        http.authorizeRequests()
                // 오류 화면은 인증을 걸지 않는다
                .antMatchers(permittedUrls).permitAll()
                // 오류 화면 이외는 인증을 건다
                .anyRequest().authenticated()//
                .and()//
                .exceptionHandling()//
                .authenticationEntryPoint(authenticationEntryPoint())//
                .accessDeniedHandler(accessDeniedHandler());

        http.formLogin()
                // 로그인 화면의 URL
                .loginPage(LOGIN_URL)
                // 인가를 처리하는 URL
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                // 로그인 성공시의 전이할 곳
                .successForwardUrl(LOGIN_SUCCESS_URL)
                // 로그인 실패시의 전이할 곳
                .failureUrl(LOGIN_FAILURE_URL)
                // 로그인 ID의 파라미터 명
                .usernameParameter("loginId")
                // 패스워드의 파라미터 명
                .passwordParameter("password").permitAll();

        // 로그 아웃 설정
        http.logout()//
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                // Cookie를 파기한다
                .deleteCookies("SESSION", "JSESSIONID", rememberMeCookieName)
                // 로그 아웃 화면의 URL
                .logoutUrl(LOGOUT_URL)
                // 로그 아웃 후의 전이할 곳
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                // ajax인 경우는, HTTP 스테이터스를 반환한다
                .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(),
                        RequestUtils::isAjaxRequest)
                // 세션을 파기한다
                .invalidateHttpSession(true).permitAll();

        // RememberMe
        http.rememberMe().key(REMEMBER_ME_KEY)//
                .rememberMeServices(multiDeviceRememberMeServices());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new DefaultAuthenticationEntryPoint(LOGIN_URL, LOGIN_TIMEOUT_URL);
    }
}
