# 정적 컨텐츠

## 정적 컨텐츠를 둘 장소

정적 컨텐츠를 둘 장소를 설정 파일로 지정한다.
디폴트 값은 아래의 설정이 되어 있으므로, `src/main/resources/static`、`src/main/resources/public`등에 배치된 파일은 정적 컨텐츠로 공개된다.

```properties
spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
```

## 캐시 컨트롤

캐시할 시간을 설정한다. 24시간 캐시할 경우는, 아래의 설정을 한다.

```properties
spring.resources.cache-period=86400
```

정적 컨텐츠 파일을 버져닝한다. 파일명을 변경하지 않고 파일 내용을 변경했을 경우에, 캐시가 유효하지 않도록 한다.
정적 컨텐츠의 내용으로부터 MD5해시값을 계산하여 파일명으로 한다.

아래의 설정을 하면, 자동적으로 파일명에 해시값이 포함되게 된다.

```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```

- 출력예

    ```html
    <!-- 템플렛은 그냥 파일을 지정한다 -->
    <link rel="shortcut th:href="@{/static/images/favicon.png}" />

    <!-- 아래와 같이 해시값이 자동적으로 출력된다 -->
    <link rel="shortcut href="/admin/static/images/favicon-ca31b78daf0dd9a106bbf3c6d87d4ec7.png" />
    ```

## WebJars

WebJars를 사용하여 Javascript、CSS라이브러리를 관리한다.
build.gradle에 라이브러리를 추가한다.

```groovy
compile "org.webjars:webjars-locator"
compile "org.webjars:jquery:2.2.4"
```

webjars-locator를 사용할 수 있도록 한다.

```java
public class WebAppConfig extends WebMvcConfigurerAdapter {
    // ...
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // webjars를 ResourceHandler에 등록한다
        registry.addResourceHandler("/webjars/**")
                // JAR의 내용을 리소스 로케이션으로 한다
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                // webjars-locator를 사용하기 위해 리소스 체인 안의 캐시를 무효화한다
                .resourceChain(false);
    }
}
```

webjars-locator를 사용하면 버젼 지정이 불필요하게 된다.

```html
<!-- 템플릿에는 버전을 쓰지 않는다 -->
<link th:src="@{/webjars/jquery/jquery.min.js}" />

<!-- 아래와 같이 교환된다（2.2.4를 의존관계에 지정하고 있기 때문） -->
<link src="/admin/webjars/jquery/2.2.4/jquery.min.js" />
```

Jar파일에 내포된 GZip완료의 리스소를 사용하도록 한다.

```properties
spring.resources.chain.gzipped=true
```

## 시큐리티

Spring Security의 설정으로 인증을 걸지 않도록 한다.

```java
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // ...
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적파일로의 액세스는 인증을 걸지 않는다
        web.ignoring()//
                .antMatchers("/webjars/**", "/static/**");
    }
}
```
