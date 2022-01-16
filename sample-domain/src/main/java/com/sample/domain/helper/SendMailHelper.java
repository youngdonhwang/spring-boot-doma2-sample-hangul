package com.sample.domain.helper;

import static com.sample.common.util.ValidateUtils.isNotEmpty;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import static com.sample.common.util.ValidateUtils.isNotEmpty;

/**
 * 메일 송신 헬퍼
 */
@Component
@Slf4j
public class SendMailHelper {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 메일을 송신한다
     * 
     * @param fromAddress
     * @param toAddress
     * @param subject
     * @param body
     */
    public void sendMail(String fromAddress, String[] toAddress, String subject, String body) {
        val message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("failed to send mail.", e);
            throw e;
        }
    }

    /**
     * 지정한 템플릿의 메일 본문을 반환한다
     *
     * @param template
     * @param objects
     * @return
     */
    public String getMailBody(String template, Map<String, Object> objects) {
        val templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        val context = new Context();
        if (isNotEmpty(objects)) {
            objects.forEach(context::setVariable);
        }

        return templateEngine.process(template, context);
    }

    protected ITemplateResolver templateResolver() {
        val resolver = new StringTemplateResolver();
        resolver.setTemplateMode("TEXT");
        resolver.setCacheable(false); // 안전을 위해 캐시하지 않는다
        return resolver;
    }
}
