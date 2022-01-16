package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "code")
@Entity
@Getter
@Setter
public class Code extends DomaDtoImpl {

    private static final long serialVersionUID = 8207242972390517957L;

    // 코드ID
    @Id
    @Column(name = "code_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 코드분류키
    String categoryKey;

    // 코드분류명
    String categoryName;

    // 코드키
    String codeKey;

    // 코드값
    String codeValue;

    // 별칭
    String codeAlias;

    // 속성1
    String attribute1;

    // 속성2
    String attribute2;

    // 속성3
    String attribute3;

    // 속성4
    String attribute4;

    // 속성5
    String attribute5;

    // 속성6
    String attribute6;

    // 표시순
    Integer displayOrder;

    // 무효플래그
    Boolean isInvalid;
}
