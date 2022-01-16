package com.sample.domain.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.repository.users.UserRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 사용자 서비스
 */
@Service
public class UserService extends BaseTransactionalService {

    @Autowired
    UserRepository userRepository;

    /**
     * 사용자를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용의 경우는 지정한다
    public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findAll(criteria, pageable);
    }

    /**
     * 사용자를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> findOne(UserCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findOne(criteria);
    }

    /**
     * 사용자를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(UserCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findOne(criteria);
    }

    /**
     * 사용자를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return userRepository.findById(id);
    }

    /**
     * 사용자를 추가한다.
     *
     * @param inputUser
     * @return
     */
    public User create(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");
        return userRepository.create(inputUser);
    }

    /**
     * 사용자를 갱신한다.
     *
     * @param inputUser
     * @return
     */
    public User update(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");
        return userRepository.update(inputUser);
    }

    /**
     * 사용자를 논리삭제한다.
     *
     * @return
     */
    public User delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return userRepository.delete(id);
    }
}
