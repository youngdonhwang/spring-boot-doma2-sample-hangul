package com.sample.web.admin.controller.html.system.roles;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되어 있지 않는 속성을 무시하여 맵핑한다
@JsonPropertyOrder({ "역할ID", "역할키", "역할명" }) // CSV의 헤더순
@Getter
@Setter
public class RoleCsv implements Serializable {

    private static final long serialVersionUID = -3895412714445561940L;

    @JsonProperty("역할ID")
    Long id;

    @JsonProperty("역할키")
    String roleKey;

    @JsonProperty("역할명")
    String roleName;
}
