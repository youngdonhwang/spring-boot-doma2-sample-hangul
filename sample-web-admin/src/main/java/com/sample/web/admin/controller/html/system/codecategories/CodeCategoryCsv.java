package com.sample.web.admin.controller.html.system.codecategories;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되어 있지 않은 속성을 무시하여 매핑한다
@JsonPropertyOrder({ "코드ID", "코드분류키", "코드분류명" }) // CSV의 헤더 순서
@Getter
@Setter
public class CodeCategoryCsv implements Serializable {

    private static final long serialVersionUID = -1235021910126027275L;

    @JsonProperty("코드분류ID")
    Long id;

    @JsonProperty("코드분류키")
    String categoryKey;

    @JsonProperty("코드분류명")
    String categoryName;
}
