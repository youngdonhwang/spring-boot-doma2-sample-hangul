package com.sample.domain.dto.common;

/**
 * MultipartFile 인터페이스가 web모듈에 의존하고 있으므로, <br/>
 * 이 인터페이스를 경유시킴으로써 순환 참조되지 않도록 한다.
 */
public interface MultipartFileConvertible {

    void setFilename(String filename);

    void setOriginalFilename(String originalFilename);

    void setContentType(String contentType);

    void setContent(BZip2Data data);
}
