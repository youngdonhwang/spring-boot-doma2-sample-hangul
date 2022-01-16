package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 코드분류 서비스
 */
@Service
public class CodeCategoryService extends BaseTransactionalService {

    @Autowired
    CodeCategoryRepository codeCategoryRepository;

    /**
     * 코드분류를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용의 경우는 지정한다
    public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return codeCategoryRepository.findAll(criteria, pageable);
    }

    /**
     * 코드분류를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return codeCategoryRepository.findOne(criteria);
    }

    /**
     * 코드분류를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public CodeCategory findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return codeCategoryRepository.findById(id);
    }

    /**
     * 코드분류를 취득한다.
     *
     * @param categoryKey
     * @return
     */
    @Transactional(readOnly = true)
    public CodeCategory findByKey(final String categoryKey) {
        Assert.notNull(categoryKey, "categoryKey must not be null");

        val criteria = new CodeCategoryCriteria();
        criteria.setCategoryKey(categoryKey);

        // 1件取得
        return codeCategoryRepository.findOne(criteria)
                .orElseThrow(() -> new NoDataFoundException("category_key=" + categoryKey + " のデータが見つかりません。"));
    }

    /**
     * 코드분류를 추가한다.
     *
     * @param inputCodeCategory
     * @return
     */
    public CodeCategory create(final CodeCategory inputCodeCategory) {
        Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
        return codeCategoryRepository.create(inputCodeCategory);
    }

    /**
     * 코드분류를 갱신한다.
     *
     * @param inputCodeCategory
     * @return
     */
    public CodeCategory update(final CodeCategory inputCodeCategory) {
        Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
        return codeCategoryRepository.update(inputCodeCategory);
    }

    /**
     * 코드분류를 논리 삭제한다.
     *
     * @return
     */
    public CodeCategory delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return codeCategoryRepository.delete(id);
    }
}
