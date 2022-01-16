package com.sample.domain.dao.system;

import java.util.List;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.UploadFile;
import com.sample.domain.dto.system.UploadFileCriteria;

@ConfigAutowireable
@Dao
public interface UploadFileDao {

    /**
     * 업로드 파일을 취득한다
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select
    List<UploadFile> selectAll(UploadFileCriteria criteria, SelectOptions options);

    /**
     * 업로드 파일을 1건 취득한다
     *
     * @param id
     * @return
     */
    @Select
    UploadFile selectById(Long id);

    /**
     * 업로드 파일을 1건 취득한다
     *
     * @param criteria
     * @return
     */
    @Select
    UploadFile select(UploadFileCriteria criteria);

    /**
     * 업로드 파일을 등록한다
     *
     * @param uploadFile
     * @return
     */
    @Insert
    int insert(UploadFile uploadFile);

    /**
     * 업로드 파일을 갱신한다
     *
     * @param uploadFile
     * @return
     */
    @Update
    int update(UploadFile uploadFile);

    /**
     * 업로드 파일을 물리 삭제한다
     *
     * @param uploadFile
     * @return
     */
    @Update(excludeNull = true)
    int delete(UploadFile uploadFile);

    /**
     * 업로드 파일을 일괄 등록한다
     *
     * @param uploadFiles
     * @return
     */
    @BatchInsert
    int[] insert(List<UploadFile> uploadFiles);
}
