package com.sample.domain.dto.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.seasar.doma.Entity;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Setter
@Getter
public abstract class DomaHistoryDtoImpl extends DomaDtoImpl implements DomaHistoryDto, Serializable {

    // 갱신항목명
    String changedPropertyNames;

    // 갱신자ID
    Integer changedBy;

    // 갱신일시
    LocalDateTime changedAt;
}
