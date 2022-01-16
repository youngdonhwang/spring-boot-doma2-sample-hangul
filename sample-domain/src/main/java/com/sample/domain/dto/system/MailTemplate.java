package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "mail_templates")
@Entity
@Getter
@Setter
public class MailTemplate extends DomaDtoImpl {

    private static final long serialVersionUID = -2997823123579780864L;

    @OriginalStates // 차분 UPDATE를 위해 정의한다
    MailTemplate originalStates;

    @Id
    @Column(name = "mail_template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 카테고리키
    String categoryKey;

    // 메일템플릿키
    String templateKey;

    // 메일타이틀
    String subject;

    // 메일본문
    String templateBody;
}
