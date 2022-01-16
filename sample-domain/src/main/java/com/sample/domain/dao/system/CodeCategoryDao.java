package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;

@ConfigAutowireable
@Dao
public interface CodeCategoryDao {

    /**
     * 코드분류정의를 취득한다.
     * 
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final CodeCategoryCriteria criteria, final SelectOptions options,
            final Collector<CodeCategory, ?, R> collector);

    /**
     * 코드분류정의를 전부 취득한다.
     *
     * @return
     */
    @Select
    List<CodeCategory> fetchAll();

    /**
     * 코드분류를 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<CodeCategory> selectById(Long id);

    /**
     * 코드분류를 1건 취득한다.
     *
     * @param categoryKey
     * @return
     */
    @Select
    Optional<CodeCategory> selectByKey(String categoryKey);

    /**
     * 코드분류를 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<CodeCategory> select(CodeCategoryCriteria criteria);

    /**
     * 코드분류를 등록한다.
     *
     * @param CodeCategory
     * @return
     */
    @Insert
    int insert(CodeCategory CodeCategory);

    /**
     * 코드분류를 갱신한다.
     *
     * @param codeCategory
     * @return
     */
    @Update(exclude = { "categoryKey" })
    int update(CodeCategory codeCategory);

    /**
     * 코드분류를 논리삭제한다.
     *
     * @param codeCategory
     * @return
     */
    @Update(excludeNull = true) // NULL의 항목은 갱신대상으로 하지 않는다
    int delete(CodeCategory codeCategory);

    /**
     * 코드분류를 일괄 등록한다.
     *
     * @param codes
     * @return
     */
    @BatchInsert
    int[] insert(List<CodeCategory> codes);
}
