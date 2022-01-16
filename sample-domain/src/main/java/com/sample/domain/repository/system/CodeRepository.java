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

import com.sample.domain.dao.system.CodeDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 코드 저장소
 */
@Repository
public class CodeRepository extends BaseRepository {

    @Autowired
    CodeDao codeDao;

    @Autowired(required = false)
    CacheManager cacheManager;

    /**
     * 코드를 전부 취득한다.
     *
     * @return
     */
    @Cacheable(cacheNames = "code", key = "#root.method")
    public List<Code> fetchAll() {
        // ページングを指定する
        val pageable = Pageable.NO_LIMIT;
        val options = createSelectOptions(pageable).count();
        return codeDao.selectAll(new CodeCriteria(), options, toList());
    }

    /**
     * 코드를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다.
        val options = createSelectOptions(pageable).count();
        val data = codeDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 코드를 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<Code> findOne(CodeCriteria criteria) {
        // 1건 취득
        return codeDao.select(criteria);
    }

    /**
     * 코드를 취득한다.
     *
     * @param categoryKey
     * @param codeKey
     * @return
     */
    @Cacheable(cacheNames = "code", key = "#categoryKey + '_' + #codeKey")
    public Optional<Code> findByCodeKey(String categoryKey, String codeKey) {
        // 1건 취득
        val criteria = new CodeCriteria();
        criteria.setCategoryKey(categoryKey);
        criteria.setCodeKey(codeKey);
        return codeDao.select(criteria);
    }

    /**
     * 코드를 취득한다.
     *
     * @return
     */
    @Cacheable(cacheNames = "code", key = "#id")
    public Code findById(final Long id) {
        // 1건 취득
        return codeDao.selectById(id).orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * 코드를 추가한다.
     *
     * @param inputCode
     * @return
     */
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCode.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCode.categoryKey + '_' + #inputCode.codeKey") //
    })
    public Code create(final Code inputCode) {
        // 1건 등록
        codeDao.insert(inputCode);

        return inputCode;
    }

    /**
     * 코드를 갱신한다.
     *
     * @param inputCode
     * @return
     */
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCode.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCode.categoryKey + '_' + #inputCode.codeKey") //
    })
    public Code update(final Code inputCode) {
        // 1건 갱신
        int updated = codeDao.update(inputCode);

        if (updated < 1) {
            throw new NoDataFoundException("code_id=" + inputCode.getId() + " 의 데이터가 없습니다.");
        }

        return inputCode;
    }

    /**
     * 코드를 논리 삭제한다.
     *
     * @return
     */
    @Caching(evict = { //
            @CacheEvict(cacheNames = "code_category", key = "#id"),
            @CacheEvict(cacheNames = "code_category", key = "#categoryKey + '_' + #codeKey") //
    })
    public Code delete(final Long id) {
        val code = codeDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " 의 데이터가 없습니다."));

        int updated = codeDao.delete(code);

        if (updated < 1) {
            throw new NoDataFoundException("code_id=" + id + " 는 갱신할 수 없었습니다.");
        }

        return code;
    }

    /**
     * 캐시로 로드한다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadCache() {
        // 캐시한다
        if (cacheManager != null) {
            val cache = cacheManager.getCache("code");
            fetchAll().forEach(c -> {
                cache.put(c.getCategoryKey() + "_" + c.getCodeKey(), c);
                cache.put(c.getId(), c);
            });
        }
    }
}
