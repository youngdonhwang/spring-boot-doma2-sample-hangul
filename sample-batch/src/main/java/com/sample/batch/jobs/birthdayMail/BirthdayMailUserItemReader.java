package com.sample.batch.jobs.birthdayMail;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.batch.jobs.BasePageableItemReader;
import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;

import lombok.val;

/**
 * 생일 메일의 대상을 검색한다
 */
public class BirthdayMailUserItemReader extends BasePageableItemReader<User> {

    @Autowired
    UserDao userDao;

    @Override
    protected List<User> getList() {
        val criteria = new UserCriteria();
        val options = getSelectOptions();
        return userDao.selectAll(criteria, options, toList());
    }
}
