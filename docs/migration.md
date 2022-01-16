# DB마이그레이션

## flyway의 버전을 변경하기

`Spring Boot 1.5.6`에서는, `Flyway v3.2.1`이 의존관계에 들어가 있는데,
아래의 설정을 `build.gradle`으로 지정함으로써 버젼을 업그레이드할 수 있다.
（`Flyway v4`에서는, 리피터블 마이그레이션을 이용할 수 있다）

```groovy
ext["flyway.version"] = "4.2.0"
```

## 마이그레이션 파일

아래와 같은 내용의 마이그레이션 파일 `R__1_create_tables.sql`을 `src/main/resources/db/migration`에 배치한다.
`R`로 시작하는 파일은, 리피터블 마이그레이션이 실시된다.

```sql
CREATE TABLE IF NOT EXISTS users(
  user_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '사용자ID'
  , first_name VARCHAR(40) NOT NULL COMMENT '이름'
  , last_name VARCHAR(40) NOT NULL COMMENT '성'
  , email VARCHAR(100) DEFAULT NULL COMMENT '메일주소'
  , password VARCHAR(100) DEFAULT NULL COMMENT '패스워드'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '전화번호'
  , zip VARCHAR(20) DEFAULT NULL COMMENT '우편번호'
  , address VARCHAR(100) DEFAULT NULL COMMENT '주소'
  , upload_file_id INT(11) unsigned DEFAULT NULL COMMENT '첨부파일'
  , password_reset_token VARCHAR(50) DEFAULT NULL COMMENT '패스워드리셋토큰'
  , token_expires_at DATETIME DEFAULT NULL COMMENT '토큰실효일'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (user_id)
  , KEY idx_users (email, deleted_at)
) COMMENT='사용자';
```

## flyway의 설정

예제에서는 개발환경에서만의 이용을 가정한다.
하기의 설정을 지정하면, 애플리케이션의 기동시에 Flyway의 마이그레이션이 실시된다.

```properties
spring.flyway.enable=true
```
