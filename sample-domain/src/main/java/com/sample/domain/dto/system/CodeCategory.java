package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "code_category")
@Entity
@Getter
@Setter
public class CodeCategory extends DomaDtoImpl {

    private static final long serialVersionUID = 2229749282619203935L;

    // 코드분류ID
    @Id
    @Column(name = "code_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 카테고리키
    String categoryKey;

    // 카테고리명
    String categoryName;
}
