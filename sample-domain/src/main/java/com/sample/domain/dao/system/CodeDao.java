package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;

@ConfigAutowireable
@Dao
public interface CodeDao {

    /**
     * 코드를 취득한다.
     * 
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final CodeCriteria criteria, final SelectOptions options, final Collector<Code, ?, R> collector);

    /**
     * 코드를 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Code> selectById(Long id);

    /**
     * 코드를 1건 취득한다.
     *
     * @param codeKey
     * @return
     */
    @Select
    Optional<Code> selectByKey(String codeKey);

    /**
     * 코드를 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Code> select(CodeCriteria criteria);

    /**
     * 코드를 등록한다.
     *
     * @param Code
     * @return
     */
    @Insert
    int insert(Code Code);

    /**
     * 코드를 갱신한다.
     *
     * @param code
     * @return
     */
    @Update(exclude = { "categoryKey", "categoryName", "codeKey", "codeAlias", "attribute1", "attribute2", "attribute3",
            "attribute4", "attribute5", "attribute6", "isInvalid" })
    int update(Code code);

    /**
     * 코드를 논리삭제한다.
     *
     * @param code
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신 대상으로 하지 않는다.
    int delete(Code code);

    /**
     * 코드를 일괄 등록한다.
     *
     * @param codes
     * @return
     */
    @BatchInsert
    int[] insert(List<Code> codes);
}
