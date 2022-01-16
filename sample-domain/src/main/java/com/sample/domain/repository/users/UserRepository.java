package com.sample.domain.repository.users;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.UploadFileDao;
import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dao.users.UserRoleDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.dto.user.UserRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

@Repository
public class UserRepository extends BaseRepository {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    UploadFileDao uploadFileDao;

    /**
     * 사용자를 취득한다.
     * 
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다
        val options = createSelectOptions(pageable).count();
        val data = userDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 사용자를 취득한다.
     * 
     * @param criteria
     * @return
     */
    public Optional<User> findOne(UserCriteria criteria) {
        // 1건 취득
        val user = userDao.select(criteria);

        // 첨부 파일을 취득한다.
        user.ifPresent(u -> {
            val uploadFileId = u.getUploadFileId();
            val uploadFile = ofNullable(uploadFileId).map(uploadFileDao::selectById);
            uploadFile.ifPresent(u::setUploadFile);
        });

        return user;
    }

    /**
     * 사용자를 취득한다.
     *
     * @return
     */
    public User findById(final Long id) {
        return userDao.selectById(id).orElseThrow(() -> new NoDataFoundException("user_id=" + id + " 의 데이터가 없습니다."));
    }

    /**
     * 사용자를 추가한다.
     *
     * @param inputUser
     * @return
     */
    public User create(final User inputUser) {

        // 1건 등록
        userDao.insert(inputUser);

        // 역할 권한 관계를 등록한다
        val userRole = new UserRole();
        userRole.setUserId(inputUser.getId());
        userRole.setRoleKey("users");
        userRoleDao.insert(userRole);

        return inputUser;
    }

    /**
     * 사용자를 갱신한다.
     *
     * @param inputUser
     * @return
     */
    public User update(final User inputUser) {

        val uploadFile = inputUser.getUploadFile();
        if (uploadFile != null) {
            // 첨부파일이 있는 경우는, 등록・갱신한다
            val uploadFileId = inputUser.getUploadFileId();
            if (uploadFileId == null) {
                uploadFileDao.insert(uploadFile);
            } else {
                uploadFileDao.update(uploadFile);
            }

            inputUser.setUploadFileId(uploadFile.getId());
        }

        // 1건 갱신
        int updated = userDao.update(inputUser);

        if (updated < 1) {
            throw new NoDataFoundException("user_id=" + inputUser.getId() + " 의 데이터가 없습니다.");
        }

        return inputUser;
    }

    /**
     * 사용ㅈ를 논리 삭제한다.
     *
     * @return
     */
    public User delete(final Long id) {
        val user = userDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " 의 데이터가 없습니다."));

        int updated = userDao.delete(user);

        if (updated < 1) {
            throw new NoDataFoundException("user_id=" + id + " 는 갱신할 수 없었습니다.");
        }

        return user;
    }
}
