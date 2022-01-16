package com.sample.domain.service.login;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.helper.SendMailHelper;
import com.sample.domain.repository.system.MailTemplateRepository;
import com.sample.domain.repository.system.StaffRepository;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 로그인 서비스
 */
@Service
public class LoginService extends BaseTransactionalService {

    @Value("${spring.mail.properties.mail.from}")
    String fromAddress;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    MailTemplateRepository mailTemplateRepository;

    @Autowired
    SendMailHelper sendMailHelper;

    /**
     * 패스워드 리셋 메일을 송신한다.
     * 
     * @param email
     * @param url
     */
    public void sendResetPasswordMail(String email, String url) {
        Assert.notNull(fromAddress, "fromAddress must be set.");

        val criteria = new StaffCriteria();
        criteria.setEmail(email);
        val staff = staffRepository.findOne(criteria);

        staff.ifPresent(s -> {
            // 토큰을 발행한다
            val token = UUID.randomUUID().toString();
            s.setPasswordResetToken(token);
            s.setTokenExpiresAt(LocalDateTime.now().plusHours(2)); // 2시간 후에 실효시킨다
            staffRepository.update(s);

            // 메일을 작성한다
            val mailTemplate = getMailTemplate("passwordReset");
            val subject = mailTemplate.getSubject();
            val templateBody = mailTemplate.getTemplateBody();

            Map<String, Object> objects = new HashMap<>();
            objects.put("staff", s);
            objects.put("url", StringUtils.join(url, "?token=", token));

            // 템플릿 엔진을 이용해 내용을 가공한다
            val body = sendMailHelper.getMailBody(templateBody, objects);

            // 메일을 송신한다
            sendMailHelper.sendMail(fromAddress, new String[] { s.getEmail() }, subject, body);
        });
    }

    /**
     * 토큰의 유효성을 체크한다.
     * 
     * @param token
     * @return
     */
    public boolean isValidPasswordResetToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        // 토큰의 일치와 유효 기간을 체크한다
        val criteria = new StaffCriteria();
        criteria.setPasswordResetToken(token);
        val staff = staffRepository.findOne(criteria);

        if (!staff.isPresent()) {
            return false;
        }

        return true;
    }

    /**
     * 패스워드를 갱신한다.
     * 
     * @param token
     * @param password
     * @return
     */
    public boolean updatePassword(String token, String password) {
        // 토큰의 일치와 유효 기간을 체크한다
        val criteria = new StaffCriteria();
        criteria.setPasswordResetToken(token);
        val staff = staffRepository.findOne(criteria);

        if (!staff.isPresent()) {
            return false;
        }

        staff.ifPresent(s -> {
            // 패스워드를 리셋한다
            s.setPasswordResetToken(null);
            s.setTokenExpiresAt(null);
            s.setPassword(password);
            staffRepository.update(s);
        });

        return true;
    }

    /**
     * 메일 템플릿을 취득한다.
     *
     * @return
     */
    protected MailTemplate getMailTemplate(String templateKey) {
        val criteria = new MailTemplateCriteria();
        criteria.setTemplateKey(templateKey);
        return mailTemplateRepository.findOne(criteria).orElseThrow(
                () -> new NoDataFoundException("templateKey=" + criteria.getTemplateKey() + " 의 데이터가 없습니다."));
    }
}
