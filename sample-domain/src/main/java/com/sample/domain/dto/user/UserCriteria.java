package com.sample.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteria extends User {

    private static final long serialVersionUID = -1;

    // 주소가 NULL인 데이터를 필터링
    Boolean onlyNullAddress;
}
