# 메시지

## 메시지 정의 파일의 지정

```properties
spring.messages.basename=messages,ValidationMessages,PropertyNames # 콤마구분으로 복수 파일을 지정할 수 있다
spring.messages.cache-duration=-1 # -1로 캐시가 무효가 된다. 프로덕션 환경에서는 어느 정도 캐시한다
spring.messages.encoding=UTF-8
```

예제의 구현에서는 다음의 역할로 파일을 분할하고 있다.
1. messages.properties
    - 정적 문장 등의 메시지를 정의한다.
1. ValidationMessages.properties
    - 벨리데이션 오류의 메시지를 정의한다.
1. PropertyNames.properties
    - 벨리데이션 오류가 발생했을 때에 항목명을 표시하기 위해 항목명을 정의한다.

## 로케일에 의한 메시지 전환

아래의 Bean을 정의함으로써, i18n대응이 가능하게 된다.
로케일이 「ja」인 경우는, `messages_ja.properties`나 `ValidationMessages_ja.properties`로 정의한 파일이 사용된다.

```java
@Bean
public LocaleResolver localeResolver() {
    // Cookie에 언어를 보관한다.
    val resolver = new CookieLocaleResolver();
    resolver.setCookieName("lang"); // cookie의 lang속성에 설정된 값을 이용하여 로케일을 전환한다.
    return resolver;
}

@Bean
public LocaleChangeInterceptor localeChangeInterceptor() {
    // lang파라미터로 로케일을 전환한다
    val interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang"); // 로케일 전환에 사용하는 파라미터명을 지정한다.
    return interceptor;
}
```
