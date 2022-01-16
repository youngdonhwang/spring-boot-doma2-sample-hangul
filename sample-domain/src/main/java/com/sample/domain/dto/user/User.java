package com.sample.domain.dto.user;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import org.seasar.doma.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.system.UploadFile;

import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User extends DomaDtoImpl {

    private static final long serialVersionUID = 4512633005852272922L;

    @OriginalStates // 차분UPDATE를 위해 정의한다
    @JsonIgnore // API의 응답에 포함하지 않는다
    User originalStates;

    @Id //주 키
    @Column(name = "user_id")  // id <-> user_id를 매핑한다
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // MySQL의 AUTO_INCREMENT를 이용한다
    Long id;

    // 해시된 패스워드
    @JsonIgnore
    String password;

    // 이름
    @ApiModelProperty(value = "이름")
    String firstName;

    // 성
    @ApiModelProperty(value = "성")
    String lastName;

    // 메일 주소
    @Email
    @ApiModelProperty(value = "메일주소")
    String email;

    // 전화번호
    @ApiModelProperty(value = "전화번호", allowableValues = "range[0, 10]")
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 우편번호
    @NotEmpty
    @ApiModelProperty(value = "우편번호")
    String zip;

    // 주소
    @NotEmpty
    @ApiModelProperty(value = "주소")
    String address;

    // 첨부파일ID
    @JsonIgnore
    Long uploadFileId;

    // 첨부파일
    @Transient // Doma로 영속화하지 않는다(users테이블에 upload_filed이라는 컬럼이 없기 때문)
    @JsonIgnore
    UploadFile uploadFile;
}
