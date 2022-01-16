# 밸리게이션(유효성)

## 클라이언트 사이드

jQuery Validation Plugin을 사용하여,
Form의 Submit을 Hook해서 입력 오류가 있는 경우는, 정적으로 오류 메시지를 표시한다.
오류가 없는 경우는, Submit가 실행된다.

```javascript
$(function() {
    // 메시지를 덮어쓴다.
    $.extend($.validator.messages, {
        minlength: $.validator.format("{0}문자이상의 문자를 입력해주세요"),
        ...
    });

    $.validator.setDefaults({
        errorPlacement: function(error, element) {
            // 오류가 발생한 항목의 색을 변화시킨다
        },
        success: function(error, element) {
            // 오류가 없는 상태로 변했을 때, 색을 되돌린다
        },
        onkeyup: function(element, event ) {
            // 키에서 손을 떼었을 때에 벨리데이션한다
        },
        onfocusout: function(element) {
            // 포커스를 벗어났을 때에 벨리데이션한다
        },
        submitHandler: function(form){
            form.submit();
        }
    });

    // 입력 체크의 종류를 독자적으로 늘리거나 또는 동작을 덮어쓴다
    $.validator.methods.email = function(value, element) {
        // 입력 체크하여, 오류가 있는 경우는 false를 반환한다
    };

    var options = {
        rules: {
            firstName: {
                required: true,
                maxlength: 50
            }
        }
    };

    // 초기화
    $("#form1").submit(function(e) {
        // validation plugin에서 submit하기 위해 막아둔다
        e.preventDefault();
    }).validate(options);
});
```

## 서버 사이드

### Bean Validation（단일 항목 체크）

입력 폼의 필드에 어노테이션을 지정한다.

```java
@Getter
@Setter
public class UserForm {

    @NotEmpty // ★비어있지 않음을 체크
    @Size(min=2, max=30) // ★문자열 길이의 하한과 상한 체크
    public String name;

    @Email // ★메일주소 서식 체크
    public String email;
}
```

컨트롤러의 인수에 @Validated어노테이션을 지정한다.

```java
public class UserController {

    @PostMapping("/new")
    public String newUser(@Validated UserForm form, BindingResult result, RedirectAttributes attributes) { // ★@Validatedを指定する
        if (result.hasErrors()) { // ★벨리데이션 오류가 있는 경우는 True가 반환된다
            setFlashAttributeErrors(attributes, result);
            return "redirect:/user/users/new";
        }
    }
}
```

화면에 벨리테이션 오류의 내용을 표시한다.

```html
<form th:object="${userForm}" method="post">
    <div th:with="valid=${!#fields.hasErrors('name')}">
        <input type="text" th:field="*{'name'}" />
        <span th:if="${!valid}" th:errors="*{'name'}">Error</span><!-- 오류가 있는 경우는 메시지가 표시된다 -->
    </div>
</form>
```

### Spring Validator（상호 관련 체크）

예제의 구현에서는, `org.springframework.validation.Validator`를 구현한 베이스 클래스를 준비하고 있으므로,
다음과 같은 벨리테이션을 구현하면, 상호 관련 체크를 할 수 있다.

```java
@Component // ★Autowire하기 위한 @Component를 지정한다
public class UserFormValidator extends AbstractValidator<UserForm> { // ★제네릭스로 Form형을 지정한다

    @Override
    protected void doValidate(UserForm form, Errors errors) { // ★제네릭스의 형으로 인수를 받는다

        // 확인용 패스워드와 동일한지 비교한다
        if (!equals(form.getPassword(), form.getPasswordConfirm())) { // ★임의로 체크한다
            errors.rejectValue("password", "users.unmatchPassword"); // ★오류가 있으면, Errors객체에 추가한다
            errors.rejectValue("passwordConfirm", "users.unmatchPassword");
        }
    }
}
```

```eval_rst
.. note::
    오류 메시지는, ValidationMessages.properties에 정의한다.
```

컨트롤러에 Form객체별로 벨리데이터를 설정한다.
단일 항목 체크와 동일하게, `@Validated` 어노테이션을 지정하고 있으면 벨리데이터가 동작한다.

```java
@Controller
public class UserController {
    // ...
    @Autowired
    UserFormValidator userFormValidator; // ★Spring Validator의 구현

    @InitBinder("userForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(userFormValidator); // ★userForm에 바인드한다
    }
    // ...
}
```

### 어노테이션을 직접 만들기

공통화하고 싶은 경우는 아래와 같이 어노테이션 클래스를 작성해서 이용할 수 있다.

```java
@Documented
@Constraint(validatedBy = { ZipCodeValidator.class }) // ★벨리데이터를 지정한다
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
public @interface ZipCode {

    String message() default "{validator.annotation.ZipCode.message}"; // ★벨리데이션에서 오류가 된 경우의 메시지 키

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ZipCode[] value();
    }
}
```

어노테이션에 대한 벨리데이터를 작성한다。

```java
public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> { // ★어떤 어노테이션을 대상으로 할지 지정한다

    static final Pattern p = Pattern.compile("^[0-9]{7}$");

    @Override
    public void initialize(ZipCode ZipCode) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        if (StringUtils.isEmpty(value)) {
            isValid = true;
        } else {
            Matcher m = p.matcher(value);

            if (m.matches()) {
                isValid = true;
            }
        }

        return isValid;
    }
}
```
