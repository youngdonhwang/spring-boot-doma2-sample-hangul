package com.sample.web.admin.controller.html.system.mailtemplates;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되어 있지 않는 속성을 무시하여 매핑한다
@JsonPropertyOrder({ "메일템플릿ID", "메일템플릿키" }) // CSV의 헤더순
@Getter
@Setter
public class MailTemplateCsv implements Serializable {

    private static final long serialVersionUID = 3277131881879633731L;

    @JsonProperty("메일템플릿ID")
    Long id;

    @JsonProperty("메일템플릿키")
    String templateKey;

    @JsonProperty("메일타이틀")
    String subject;

    @JsonProperty("메일본문")
    String templateBody;
}
