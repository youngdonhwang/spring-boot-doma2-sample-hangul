package com.sample.batch.jobs.birthdayMail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemProcessor;
import com.sample.domain.dao.system.MailTemplateDao;
import com.sample.domain.dto.common.CommaSeparatedString;
import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.dto.system.SendMailQueue;
import com.sample.domain.dto.user.User;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.helper.SendMailHelper;

import lombok.val;

public class BirthdayMailProcessor extends BaseItemProcessor<User, SendMailQueue> {

    @Value("${spring.mail.properties.mail.from}")
    String fromAddress;

    @Autowired
    MailTemplateDao mailTemplateDao;

    @Autowired
    SendMailHelper sendMailHelper;

    @Override
    protected void onValidationError(BatchContext context, BindingResult result, User item) {
    }

    @Override
    protected SendMailQueue doProcess(BatchContext context, User user) {
        val mailTemplate = getMailTemplate("birthdayMail");

        val subject = mailTemplate.getSubject();
        val templateBody = mailTemplate.getTemplateBody();

        Map<String, Object> objects = new HashMap<>();
        objects.put("user", user);

        val body = sendMailHelper.getMailBody(templateBody, objects);
        val to = CommaSeparatedString.of(user.getEmail());

        val transform = new SendMailQueue();
        transform.setFrom(fromAddress);
        transform.setSubject(subject);
        transform.setBody(body);
        transform.setTo(to);

        return transform;
    }

    /**
     * 메일 템플릿을 취득한다.
     *
     * @return
     */
    protected MailTemplate getMailTemplate(String templateKey) {
        val criteria = new MailTemplateCriteria();
        criteria.setTemplateKey(templateKey);
        val mailTemplate = mailTemplateDao.select(criteria).orElseThrow(
                () -> new NoDataFoundException("templateKey=" + criteria.getTemplateKey() + " 의 데이터가 없습니다."));

        return mailTemplate;
    }

    @Override
    protected Validator getValidator() {
        return null;
    }
}
