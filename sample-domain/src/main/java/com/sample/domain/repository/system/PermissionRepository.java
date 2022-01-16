package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.PermissionDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 권한 저장소
 */
@Repository
public class PermissionRepository extends BaseRepository {

    @Autowired
    PermissionDao permissionDao;

    /**
     * 권한을 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다
        val options = createSelectOptions(pageable).count();
        val data = permissionDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 권한을 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<Permission> findOne(PermissionCriteria criteria) {
        // 1件取得
        return permissionDao.select(criteria);
    }

    /**
     * 권한을 취득한다.
     *
     * @return
     */
    public Permission findById(final Long id) {
        // 1件取得
        return permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " 의 데이터가 없습니다."));
    }

    /**
     * 권한을 추가한다.
     *
     * @param inputPermission
     * @return
     */
    public Permission create(final Permission inputPermission) {
        // 1건 등록
        permissionDao.insert(inputPermission);

        return inputPermission;
    }

    /**
     * 권한을 갱신한다.
     *
     * @param inputPermission
     * @return
     */
    public Permission update(final Permission inputPermission) {
        // 1件更新
        int updated = permissionDao.update(inputPermission);

        if (updated < 1) {
            throw new NoDataFoundException("permission_id=" + inputPermission.getId() + " 의 데이터가 없습니다.");
        }

        return inputPermission;
    }

    /**
     * 권한을 논리삭제한다.
     *
     * @return
     */
    public Permission delete(final Long id) {
        val permission = permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " 의 데이터가 없습니다."));

        int updated = permissionDao.delete(permission);

        if (updated < 1) {
            throw new NoDataFoundException("permission_id=" + id + " 는 갱신되지 못했습니다.");
        }

        return permission;
    }
}
