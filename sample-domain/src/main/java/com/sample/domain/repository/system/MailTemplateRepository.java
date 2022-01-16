package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.MailTemplateDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 메일 템플릿 저장소
 */
@Repository
public class MailTemplateRepository extends BaseRepository {

    @Autowired
    MailTemplateDao mailTemplateDao;

    /**
     * 메일 템플릿을 복수 취득한다
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다
        val options = createSelectOptions(pageable).count();
        val data = mailTemplateDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 메일 템플릿을 취득한다
     *
     * @param criteria
     * @return
     */
    public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
        // 1건 취득
        return mailTemplateDao.select(criteria);
    }

    /**
     * 메일 템플릿을 취득한다
     *
     * @return
     */
    public MailTemplate findById(final Long id) {
        // 1건 취득
        return mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " 의 데이터가 없습니다."));
    }

    /**
     * 메일 템플릿을 추가한다
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate create(final MailTemplate inputMailTemplate) {
        // 1건 등록
        mailTemplateDao.insert(inputMailTemplate);

        return inputMailTemplate;
    }

    /**
     * 메일 템플릿을 갱신한다
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate update(final MailTemplate inputMailTemplate) {
        // 1건 갱신
        int updated = mailTemplateDao.update(inputMailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + inputMailTemplate.getId() + " のデータが見つかりません。");
        }

        return inputMailTemplate;
    }

    /**
     * 메일 템플릿을 논리 삭제한다
     *
     * @return
     */
    public MailTemplate delete(final Long id) {
        val mailTemplate = mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " 의 데이터를 찾을 수 없습니다."));

        int updated = mailTemplateDao.delete(mailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + id + " 은 갱신되지 못했습니다.");
        }

        return mailTemplate;
    }
}
