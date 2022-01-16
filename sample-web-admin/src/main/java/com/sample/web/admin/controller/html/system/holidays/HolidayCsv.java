package com.sample.web.admin.controller.html.system.holidays;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되어 있지 않는 속성을 무시하여 맵핑한다
@JsonPropertyOrder({ "공휴일ID", "공휴일명", "날짜" }) // CSV의 헤더순
@Getter
@Setter
public class HolidayCsv implements Serializable {

    private static final long serialVersionUID = 6658799113183356993L;

    @JsonProperty("공휴일ID")
    Long id;

    @JsonProperty("공휴일명")
    String holidayName;

    @JsonProperty("날짜")
    @JsonFormat(pattern = "yyyy/MM/dd")
    LocalDate holidayDate;
}
