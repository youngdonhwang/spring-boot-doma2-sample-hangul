package com.sample.domain.dto.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

@Domain(valueType = String.class, factoryMethod = "of")
@NoArgsConstructor
public class CommaSeparatedString implements Serializable {

    private static final long serialVersionUID = -6864852815920199569L;

    @Getter
    String data;

    /**
     * 팩토리 메소드
     *
     * @param data
     * @return
     */
    public static CommaSeparatedString of(String data) {
        val css = new CommaSeparatedString();
        css.data = StringUtils.join(data, ",");
        return css;
    }

    // ResultSet.getBytes(int)에서 취득된 값이 이 생성자에서 설정된다
    CommaSeparatedString(String data) {
        this.data = StringUtils.join(data, ",");
    }

    // PreparedStatement.setBytes(int, bytes)에 설정할 값이 이 메소드로부터 취득된다
    String getValue() {
        return this.data;
    }
}
