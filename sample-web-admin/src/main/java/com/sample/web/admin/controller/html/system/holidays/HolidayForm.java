package com.sample.web.admin.controller.html.system.holidays;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HolidayForm extends BaseForm {

    private static final long serialVersionUID = 6646321876052100374L;

    Long id;

    // 공휴일명
    @NotEmpty
    String holidayName;

    // 날짜
    LocalDate holidayDate;
}
