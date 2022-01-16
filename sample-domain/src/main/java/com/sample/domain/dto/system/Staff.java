package com.sample.domain.dto.system;

import java.time.LocalDateTime;

import javax.validation.constraints.Digits;

import javax.validation.constraints.Email;
import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "staffs")
@Entity
@Getter
@Setter
public class Staff extends DomaDtoImpl {

    private static final long serialVersionUID = -3762941082070995608L;

    @OriginalStates // 차분 UPDATE를 위해 정의한다
    Staff originalStates;

    @Id
    @Column(name = "staff_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String password;

    // 이름
    String firstName;

    // 성
    String lastName;

    // 메일주소
    @Email
    String email;

    // 전화번호
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 패스워드리셋토큰
    String passwordResetToken;

    // 토큰 실효일
    LocalDateTime tokenExpiresAt;
}
