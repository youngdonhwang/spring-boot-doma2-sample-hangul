package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;

@ConfigAutowireable
@Dao
public interface MailTemplateDao {

    /**
     * 메일템플릿을 취득한다
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final MailTemplateCriteria criteria, final SelectOptions options,
            final Collector<MailTemplate, ?, R> collector);

    /**
     * 메일템플릿을 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<MailTemplate> selectById(Long id);

    /**
     * 메일템플릿을 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<MailTemplate> select(MailTemplateCriteria criteria);

    /**
     * 메일템플릿을 등록한다.
     *
     * @param mailtemplate
     * @return
     */
    @Insert
    int insert(MailTemplate mailtemplate);

    /**
     * 메일템플릿을 갱신한다.
     *
     * @param mailTemplate
     * @return
     */
    @Update
    int update(MailTemplate mailTemplate);

    /**
     * 메일템플릿을 논리삭제한다.
     *
     * @param mailTemplate
     * @return
     */
    @Update(excludeNull = true)
    // NULL항목은 갱신대상으로 하지 않는다
    int delete(MailTemplate mailTemplate);

    /**
     * 메일템플릿을 일괄 등록한다.
     *
     * @param mailTemplates
     * @return
     */
    @BatchInsert
    int[] insert(List<MailTemplate> mailTemplates);

    /**
     * 메일템플릿을 일괄 갱신한다.
     *
     * @param mailtemplates
     * @return
     */
    @BatchUpdate
    int[] update(List<MailTemplate> mailtemplates);
}
