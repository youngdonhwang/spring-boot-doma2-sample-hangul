package com.sample.domain.dto.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.domain.dao.DefaultEntityListener;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity(listener = DefaultEntityListener.class) // 자동적으로 시스템 제어 항목을 갱신하기 위해 리스터를 지정한다
@Setter
@Getter
public abstract class DomaDtoImpl implements DomaDto, Serializable {

    // 작성자
    @JsonIgnore
    String createdBy;

    // 작성 일시
    @JsonIgnore
    LocalDateTime createdAt;

    // 갱신자
    @JsonIgnore
    String updatedBy;

    // 갱신 일시
    @JsonIgnore
    LocalDateTime updatedAt;

    // 삭제자
    @JsonIgnore
    String deletedBy;

    // 삭제 일시
    @JsonIgnore
    LocalDateTime deletedAt;

    // 낙관적 동시 제어(Optimistic Concurrency control)에서 사용할 개정 번호
    @Version
    @Column(name = "version")
    Integer version;

    // 작성・갱신자로 사용할 값
    @Transient
    @JsonIgnore
    String auditUser;

    // 작성・갱신일으로 사용할 값
    @Transient
    @JsonIgnore
    LocalDateTime auditDateTime;
}
