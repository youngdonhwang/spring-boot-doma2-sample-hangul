# 메일 송신

## JavaMailSender를 이용하기

예제의 구현에서는, `JavaMailSender`를 사용하여 메일을 송신하는 헬퍼 클래스를 구현하고 있다.
메일 본문의 템플릿은, 데이터베이스에 갖고 있음을 가정하고 있으므로,
우선 본문을 템플릿 엔진(Thymeleaf）에 대응한 후,
메일 송신 메소드의 인수에 건네는 흐름으로 처리한다.

```java
@Component
@Slf4j
public class SendMailHelper {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 메일을 송신한다.
     *
     * @param fromAddress
     * @param toAddress
     * @param subject
     * @param body // 템플릿 엔진에서 플레이스홀더를 메꾼 문자열
     */
    public void sendMail(String fromAddress, String[] toAddress, String subject, String body) {
        // ...
    }

    /**
     * 지정한 템플릿의 메일 본문을 반환한다.
     *
     * @param template // Thymeleaf의 TEXT형의 문법으로 작성된 템플릿 문
     * @param objects // 템플릿의 플레이스홀더를 메꾸기 위한 변수
     * @return
     */
    public String getMailBody(String template, Map<String, Object> objects) {
        // ...
    }
}
```
