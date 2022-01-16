package com.sample.web.admin.controller.html.system.codes;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되지 않은 속성을 무시하여 매핑한다
@JsonPropertyOrder({ "코드ID", "코드분류키", "코드분류명", "코드키", "코드값", "코드별칭", "표시순", "무효플래그" }) // CSVのヘッダ順
@Getter
@Setter
public class CodeCsv implements Serializable {

    private static final long serialVersionUID = 1872497612721457509L;

    @JsonProperty("코드ID")
    Long id;

    @JsonProperty("코드분류키")
    String categoryKey;

    @JsonProperty("코드분류명")
    String categoryName;

    @JsonProperty("코드키")
    String codeKey;

    @JsonProperty("코드값")
    String codeValue;

    @JsonProperty("코드별칭")
    String codeAlias;

    @JsonProperty("표시순")
    Integer displayOrder;

    @JsonProperty("무효플래그")
    Boolean isInvalid;
}
