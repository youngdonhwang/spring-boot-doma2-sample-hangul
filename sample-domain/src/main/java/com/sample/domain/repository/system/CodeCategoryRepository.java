package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.CodeCategoryDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 코드분류 저장소
 */
@Repository
public class CodeCategoryRepository extends BaseRepository {

    @Autowired
    CodeCategoryDao codeCategoryDao;

    @Autowired(required = false)
    CacheManager cacheManager;

    /**
     * 코드분류를 전부 취득한다.
     *
     * @return
     */
    public List<CodeCategory> fetchAll() {
        // 페이징을 지정한다.
        val pageable = Pageable.NO_LIMIT;
        val options = createSelectOptions(pageable).count();
        return codeCategoryDao.selectAll(new CodeCategoryCriteria(), options, toList());
    }

    /**
     * 코드분류를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다
        val options = createSelectOptions(pageable).count();
        val data = codeCategoryDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 코드분류를 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
        // 1건 취득
        return codeCategoryDao.select(criteria);
    }

    /**
     * 코드분류를 취득한다.
     *
     * @return
     */
    @Cacheable(cacheNames = "code_category", key = "#id")
    public CodeCategory findById(final Long id) {
        // 1건 취득
        return codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * 코드분류를 추가한다.
     *
     * @param inputCodeCategory
     * @return
     */
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
    })
    public CodeCategory create(final CodeCategory inputCodeCategory) {
        // 1건 등록
        codeCategoryDao.insert(inputCodeCategory);
        return inputCodeCategory;
    }

    /**
     * 코드분류를 갱신한다.
     *
     * @param inputCodeCategory
     * @return
     */
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
    })
    public CodeCategory update(final CodeCategory inputCodeCategory) {
        // 1건 갱신
        int updated = codeCategoryDao.update(inputCodeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("codeCategory_id=" + inputCodeCategory.getId() + " のデータが見つかりません。");
        }

        return inputCodeCategory;
    }

    /**
     * 코드분류를 논리삭제한다.
     *
     * @return
     */
    @Caching(evict = { //
            @CacheEvict(cacheNames = "code_category", key = "#inputCodeCategory.id"),
            @CacheEvict(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
    })
    public CodeCategory delete(final Long id) {
        val codeCategory = codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " 의 데이터가 발견되지 않았습니다."));

        int updated = codeCategoryDao.delete(codeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("codeCategory_id=" + id + " 는 갱신할 수 없었습니다.");
        }

        return codeCategory;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadCache() {
        // 캐시한다
        if (cacheManager != null) {
            val cache = cacheManager.getCache("code_category");
            fetchAll().forEach(c -> {
                cache.put(c.getCategoryKey(), c);
                cache.put(c.getId(), c);
            });
        }
    }
}
