package com.sample.web.admin.controller.html.system.staffs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되어 있지 않은 속성을 무시하여 맵핑한다
@JsonPropertyOrder({ "담당자ID", "성", "이름", "메일주소", "전화번호" }) // CSV의 헤더순
@Getter
@Setter
public class StaffCsv implements Serializable {

    private static final long serialVersionUID = -1883999589975469540L;

    @JsonProperty("담당자ID")
    Long id;

    // 해시화된 패스워드
    @JsonIgnore // CSV에 출력하지 않는다
    String password;

    @JsonProperty("이름")
    String firstName;

    @JsonProperty("성")
    String lastName;

    @JsonProperty("메일주소")
    String email;

    @JsonProperty("전화번호")
    String tel;
}
