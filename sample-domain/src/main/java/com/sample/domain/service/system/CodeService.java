package com.sample.domain.service.system;

import static com.sample.common.util.ValidateUtils.isEquals;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 코드 서비스
 */
@Service
public class CodeService extends BaseTransactionalService {

    @Autowired
    CodeRepository codeRepository;

    /**
     * 코드를 복수 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 경우는 지정한다
    public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return codeRepository.findAll(criteria, pageable);
    }

    /**
     * 코드를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Code> findOne(CodeCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return codeRepository.findOne(criteria);
    }

    /**
     * 코드를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Code findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return codeRepository.fetchAll().stream().filter(c -> c.getId() == id.longValue()).findFirst()
                .orElseThrow(() -> new NoDataFoundException("id=" + id + " 의 데이터가 없습니다."));
    }

    /**
     * 코드를 취득한다.
     * 
     * @param codeKey
     * @return
     */
    @Transactional(readOnly = true)
    public Code findByKey(final String codeKey) {
        Assert.notNull(codeKey, "codeKey must not be null");
        return codeRepository.fetchAll().stream().filter(c -> isEquals(codeKey, c.getCodeKey())).findFirst()
                .orElseThrow(() -> new NoDataFoundException("code_key=" + codeKey + " 의 데이터가 없습니다."));
    }

    /**
     * 코드를 추가한다.
     *
     * @param inputCode
     * @return
     */
    public Code create(final Code inputCode) {
        Assert.notNull(inputCode, "inputCode must not be null");
        return codeRepository.create(inputCode);
    }

    /**
     * 코드를 갱신한다.
     *
     * @param inputCode
     * @return
     */
    public Code update(final Code inputCode) {
        Assert.notNull(inputCode, "inputCode must not be null");
        return codeRepository.update(inputCode);
    }

    /**
     * 코드를 논리 삭제한다.
     *
     * @return
     */
    public Code delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return codeRepository.delete(id);
    }
}
