package com.sample.web.base.controller.html;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseForm implements Serializable {

    private static final long serialVersionUID = 893506941860422885L;

    // 작성・갱신자에 사용할 값
    String auditUser;

    // 작성・갱신일에 사용할 값
    LocalDateTime auditDateTime;

    // 개정번호
    Integer version;

    /**
     * 기존 레코드가 없는 데이터인가?
     *
     * @return
     */
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * Id컬럼의 Getter
     *
     * @return
     */
    public abstract Long getId();
}
