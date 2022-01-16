package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.BZip2Data;
import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.MultipartFileConvertible;

import lombok.Getter;
import lombok.Setter;

@Table(name = "upload_files")
@Entity
@Getter
@Setter
public class UploadFile extends DomaDtoImpl implements MultipartFileConvertible {

    private static final long serialVersionUID = 1738092593334285554L;

    @OriginalStates // 차분 UPDATE를 위해 정의
    UploadFile originalStates;

    @Id
    @Column(name = "upload_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 파일명
    @Column(name = "file_name")
    String filename;

    // 원래파일명
    @Column(name = "original_file_name")
    String originalFilename;

    // 컨텐츠타입
    String contentType;

    // 컨텐츠
    BZip2Data content;  //byte[]를 포함하는 도메인 클래스(값 객체)
}
