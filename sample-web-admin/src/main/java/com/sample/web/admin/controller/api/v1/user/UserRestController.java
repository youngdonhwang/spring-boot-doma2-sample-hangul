package com.sample.web.admin.controller.api.v1.user;

import static com.sample.web.base.WebConst.MESSAGE_SUCCESS;

import java.io.IOException;
import java.util.Arrays;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.exception.ValidationErrorException;
import com.sample.domain.service.users.UserService;
import com.sample.web.base.controller.api.AbstractRestController;
import com.sample.web.base.controller.api.resource.PageableResource;
import com.sample.web.base.controller.api.resource.PageableResourceImpl;
import com.sample.web.base.controller.api.resource.Resource;

import lombok.val;

@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends AbstractRestController {

    @Autowired
    UserService userService;

    @Override
    public String getFunctionName() {
        return "API_USER";
    }

    /**
     * 사용자를 복수 취득한다.
     *
     * @param query
     * @param page
     * @return
     */
    @ApiOperation(value = "사용자 정보 일괄 취득", notes = "사용자를 일괄 취득합니다.",httpMethod = "GET", consumes = "application/json", response= User.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "사용자 정보 리스트", response =User.class)})
    @GetMapping
    public PageableResource index(UserQuery query, @RequestParam(required = false, defaultValue = "1") int page)
            throws IOException {
        // 입력값에서 DTO를 작성한다
        val criteria = modelMapper.map(query, UserCriteria.class);

        // 10건으로 나누어서 취득한다
        Page<User> users = userService.findAll(criteria, Pageable.DEFAULT);

        PageableResource resource = modelMapper.map(users, PageableResourceImpl.class);
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * 사용자를 취득한다.
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "사용자 정보 취득", notes = "사용자를 취득합니다.",httpMethod = "GET", consumes = "application/json", response= User.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = " 지정된 사용자 정보", response =User.class)})
    @GetMapping(value = "/{userId}")
    public Resource show(@PathVariable Long userId) {
        // 1건 취득한다
        User user = userService.findById(userId);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * 사용자를 추가한다.
     *
     * @param
     */
    @ApiOperation(value = "사용자 추가", notes = "사용자를 추가합니다.",httpMethod = "POST", consumes = "*.*", response= Resource.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "추가한 URI", response =Resource.class)})
    @PostMapping
    public Resource create(@Validated @RequestBody User inputUser, Errors errors) {
        // 입력 오류가 있을 경우
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }

        // 1건 추가하기
        User user = userService.create(inputUser);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * 사용자를 갱신한다.
     *
     * @param
     */
    @PutMapping(value = "/{userId}")
    public Resource update(@PathVariable("userId") Long userId, @Validated @RequestBody User inputUser, Errors errors) {
        // 오류가 있는 경우
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }

        // 1건 갱신한다
        inputUser.setId(userId);
        User user = userService.update(inputUser);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param
     */
    @DeleteMapping(value = "/{userId}")
    public Resource delete(@PathVariable("userId") Long userId) {
        // 1건 취득한다
        User user = userService.delete(userId);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }
}
