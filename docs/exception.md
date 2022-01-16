# 예외의 핸들링

## 공통처리에서 예외를 핸들링하기

예외의 구현에서는, `@ControllerAdvice`어노테이션을 지정한 예외 핸들러에서,
공통적으로 예외를 핸들링하고 있다.
새롭게 예외 클래스를 작성할 경우는, 공통적으로 핸들링해야 할 것이라면,
이 클래스에 핸들러 메소드를 추가한다.

```java
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestController에서는 동작시키지 않는다
public class HtmlExceptionHandler {
    // ...
}
```

```eval_rst
.. note::
   업무 로직에서 발생하는 기능 고유의 예외는, 업무 로직의 안에서 핸들링하여,
   적절한 메시지를 화면에 표시하는 것이 바람직하다.
```
