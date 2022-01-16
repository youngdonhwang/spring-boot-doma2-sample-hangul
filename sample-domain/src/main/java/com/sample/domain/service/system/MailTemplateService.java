package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.repository.system.MailTemplateRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 메일 템플릿 서비스
 */
@Service
public class MailTemplateService extends BaseTransactionalService {

    @Autowired
    MailTemplateRepository mailTemplateRepository;

    /**
     * 메일템플릿을 복수 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 걍우는 지정한다
    public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return mailTemplateRepository.findAll(criteria, pageable);
    }

    /**
     * 메일템플릿을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return mailTemplateRepository.findOne(criteria);
    }

    /**
     * 메일템플릿을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public MailTemplate findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return mailTemplateRepository.findById(id);
    }

    /**
     * 메일템플릿을 추가한다.
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate create(final MailTemplate inputMailTemplate) {
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
        return mailTemplateRepository.create(inputMailTemplate);
    }

    /**
     * 메일템플릿을 갱신한다.
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate update(final MailTemplate inputMailTemplate) {
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
        return mailTemplateRepository.update(inputMailTemplate);
    }

    /**
     * 메일템플릿을 논리 삭제한다.
     *
     * @return
     */
    public MailTemplate delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return mailTemplateRepository.delete(id);
    }
}
