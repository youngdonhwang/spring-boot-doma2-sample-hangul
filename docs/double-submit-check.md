# 이중송신방지

## 이중송신방지란

Web애플리케이션에서는, 다음의 조작을 실시하면, 동일한 처리가 2번 실행되어,
데이터가 중복되는 일이 있다.

1. 등록 버튼을 연속해서 눌렀을 경우.
2. 등록 처리가 끝난 후, 브라우저의 새로 고침 버튼을 누름으로써, 앞의 처리가 다시 실행되는 경우.
3. 등록 종료 화면으로 천이한 다음, 브라우저의 되돌아가기 버튼을 누름으로써, 등록 처리가 재실행된다.

## 대책 방법

하기의 대책을 하면, 이중송신이 어느 정도 방지 가능하다.

* Javascript를 사용해 버튼을 연속으로 눌리지 않도록 한다.
* Post-Redirect-Get패턴을 이용하여, 폼의 재송신을 할 수 없도록 한다.
* 토큰에 의한 제어
    1. 화면 표시의 타이밍에 토큰을 발행한다.
    2. 등록 버튼을 누른다.（화면에 심어둔 토큰도 송신）
    3. 서버에서 발행한 토큰과, 화면에서 건낸 토큰을 비교하여 일치하지 않으면 부정으로 한다,
    4. 정상으로 처리한 경우는, 토큰을 파기한다.

## 예제의 구현 예

예제의 구현에서는, 다음의 흐름으로
이중송신방지 체크를 실시하고 있다.

* `DoubleSubmitCheckingRequestDataValueProcessor`가 Form태그에 토큰을 심어넣는다.

```java
public class DoubleSubmitCheckingRequestDataValueProcessor implements RequestDataValueProcessor {
    // ...

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        val map = PROCESSOR.getExtraHiddenFields(request);
        String token = DoubleSubmitCheckToken.getExpectedToken(request);
        if (token == null) {
            token = DoubleSubmitCheckToken.renewToken(request);
        }

        if (!map.isEmpty()) {
            // ★토큰을 심어둔다
            map.put(DoubleSubmitCheckToken.DOUBLE_SUBMIT_CHECK_PARAMETER, token);
        }
        return map;
    }

    // ...
}
```

* `SetDoubleSubmitCheckTokenInterceptor`에서, POST메소드의 요청을 인터셉트하고, 토큰의 비교를 실시한다.
    - 화면에서 토큰이 건네지 않은 경우는, 새로운 토큰을 생성하여 내부적으로 보관해둔다.
    - 토큰의 비교에서 동일한 경우는, 그대로 처리를 계속한다.

```java
public class SetDoubleSubmitCheckTokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 컨트롤러의 동작 전
        val expected = DoubleSubmitCheckToken.getExpectedToken(request);
        val actual = DoubleSubmitCheckToken.getActualToken(request);
        DoubleSubmitCheckTokenHolder.set(expected, actual);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 컨트롤러의 동작 후
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            // POST된 때에 토큰이 일치하고 있으면, 새로운 토큰을 발행한다.
            val expected = DoubleSubmitCheckToken.getExpectedToken(request);
            val actual = DoubleSubmitCheckToken.getActualToken(request);

            if (expected != null && actual != null && Objects.equals(expected, actual)) {
                DoubleSubmitCheckToken.renewToken(request);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 처리 완료후
        DoubleSubmitCheckTokenHolder.clear();
    }
}
```

* Doma2의 리스너`DefaultEntityListener`에서, 토큰의 비교를 실시한다.
    - INSERT문을 발행하기 전의 타이밍（preInsert）에, 토큰의 비교를 실시하고, 일치하지 않은 경우는, 예외를 던진다.

```java
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

    @Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
        // 이중송신방지 체크
        val expected = DoubleSubmitCheckTokenHolder.getExpectedToken();
        val actual = DoubleSubmitCheckTokenHolder.getActualToken();

        if (expected != null && actual != null && !Objects.equals(expected, actual)) {
            throw new DoubleSubmitErrorException(); // ★일치하지 않는 경우는, 예외를 던진다.
        }

        // ...
    }

    // ...
}
```

* `HtmlExceptionHandler`で`DoubleSubmitErrorException`를 핸들링한다.
    - 이중송신을 감지했다는 메시지를 FlashMap에 설정하여, 원래의 화면으로 리다이렉트한다.

```java
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestController에서는 동작시키지 않는다
public class HtmlExceptionHandler {
    // ...

    @ExceptionHandler({ DoubleSubmitErrorException.class })
    public RedirectView handleDoubleSubmitErrorException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {

        // 공통 메시지를 취득한다
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = DOUBLE_SUBMIT_ERROR; // ★이중 송신 오류의 메시지
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }
    // ...
}
```
