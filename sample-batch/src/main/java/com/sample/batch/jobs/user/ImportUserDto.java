package com.sample.batch.jobs.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.sample.batch.item.ItemPosition;
import com.sample.domain.validator.annotation.PhoneNumber;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportUserDto implements ItemPosition {

    // 이름
    @NotEmpty
    String firstName;

    // 성
    @NotEmpty
    String lastName;

    // 메일주소
    @Email
    String email;

    // 전화번호
    @PhoneNumber
    String tel;

    // 우편번호
    @NotEmpty
    String zip;

    // 주소
    @NotEmpty
    String address;

    // 읽어들일 원 파일명
    String sourceName;

    int position;
}
