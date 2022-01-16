package com.sample.domain.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.repository.system.CodeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 코드 헬퍼
 */
@Component
@Slf4j
public class CodeHelper {

    @Autowired
    CodeCategoryRepository codeCategoryRepository;

    @Autowired
    CodeRepository codeRepository;

    /**
     * 코드분류를 전부 취득한다.
     *
     * @return
     */
    public List<CodeCategory> getCodeCategories() {
        return codeCategoryRepository.fetchAll();
    }

    /**
     * 코드키를 지정하여 코드값을 취득한다.
     *
     * @param categoryKey
     * @param codeKey
     * @return
     */
    public Code getCode(String categoryKey, String codeKey) {
        return codeRepository.findByCodeKey(categoryKey, codeKey)
                .orElseThrow(() -> new NoDataFoundException("codeKey=" + codeKey + "のデータが見つかりません。"));
    }
}
