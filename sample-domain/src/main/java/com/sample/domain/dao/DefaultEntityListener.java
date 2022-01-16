package com.sample.domain.dao;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

import com.sample.common.util.ReflectionUtils;
import com.sample.domain.dto.common.DomaDto;
import com.sample.domain.dto.common.Dto;
import com.sample.domain.exception.DoubleSubmitErrorException;

import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor // 생성자가 필수이기 때문에 선언
@Slf4j
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

    @Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
        // 이중 송신 방지 체크
        val expected = DoubleSubmitCheckTokenHolder.getExpectedToken();
        val actual = DoubleSubmitCheckTokenHolder.getActualToken();

        if (expected != null && actual != null && !Objects.equals(expected, actual)) {
            throw new DoubleSubmitErrorException();
        }

        if (entity instanceof DomaDto) {
            val domaDto = (DomaDto) entity;
            val createdAt = AuditInfoHolder.getAuditDateTime();
            val createdBy = AuditInfoHolder.getAuditUser();

            domaDto.setCreatedAt(createdAt); // 작성일
            domaDto.setCreatedBy(createdBy); // 작성자
        }
    }

    @Override
    public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {

        if (entity instanceof DomaDto) {
            val domaDto = (DomaDto) entity;
            val updatedAt = AuditInfoHolder.getAuditDateTime();
            val updatedBy = AuditInfoHolder.getAuditUser();

            val methodName = context.getMethod().getName();
            if (StringUtils.startsWith("delete", methodName)) {
                domaDto.setDeletedAt(updatedAt); // 삭제일
                domaDto.setDeletedBy(updatedBy); // 삭제자
            } else {
                domaDto.setUpdatedAt(updatedAt); // 갱신일
                domaDto.setUpdatedBy(updatedBy); // 갱신자
            }
        }
    }

    @Override
    public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {

        if (entity instanceof DomaDto) {
            val domaDto = (DomaDto) entity;
            val deletedAt = AuditInfoHolder.getAuditDateTime();
            val deletedBy = AuditInfoHolder.getAuditUser();
            val name = domaDto.getClass().getName();
            val ids = getIds(domaDto);

            // 물리 삭제한 경우는 로그 출력한다
            log.info("데이터를 물리 삭제하였습니다. entity={}, id={}, deletedBy={}, deletedAt={}", name, ids, deletedBy, deletedAt);
        }
    }

    /**
     * Id 어노테이션이 부여된 필드의 리스트 값을 반환한다
     *
     * @param dto
     * @return
     */
    protected List<Object> getIds(Dto dto) {
        return ReflectionUtils.findWithAnnotation(dto.getClass(), Id.class)
                .map(f -> ReflectionUtils.getFieldValue(f, dto)).collect(toList());
    }
}
