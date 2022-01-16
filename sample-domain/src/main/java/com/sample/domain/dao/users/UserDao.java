package com.sample.domain.dao.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;

@ConfigAutowireable
@Dao
public interface UserDao {

    /**
     * 사용자를 취득한다
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final UserCriteria criteria, final SelectOptions options, final Collector<User, ?, R> collector);

    /**
     * 사용자를 1건 취득한다
     *
     * @param id
     * @return
     */
    @Select
    Optional<User> selectById(Long id);

    /**
     * 사용자를 1건 취득한다。
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<User> select(UserCriteria criteria);

    /**
     * 사용자를 등록한다
     *
     * @param user
     * @return
     */
    @Insert
    int insert(User user);

    /**
     * 사용자를 갱신한다
     *
     * @param user
     * @return
     */
    @Update
    int update(User user);

    /**
     * 사용자를 논리 삭제한다
     *
     * @param user
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신 대상으로 하지 않는다
    int delete(User user);

    /**
     * 사용자를 일괄 등록한다
     *
     * @param users
     * @return
     */
    @BatchInsert
    int[] insert(List<User> users);

    /**
     * 사용자를 일괄 갱신한다
     *
     * @param users
     * @return
     */
    @BatchUpdate
    int[] update(List<User> users);
}
