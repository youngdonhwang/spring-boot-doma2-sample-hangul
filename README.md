# Spring Boot Sample Application

[![Build Status](https://travis-ci.org/miyabayt/spring-boot-doma2-sample.svg?branch=2018_springbootbook)](https://travis-ci.org/miyabayt/spring-boot-doma2-sample)
[![Documentation Status](https://readthedocs.org/projects/spring-boot-doma2-sample/badge/?version=latest)](http://spring-boot-doma2-sample.readthedocs.io/ja/latest/?badge=latest)

## 로컬 환경

소스의 다운로드
```bash
$ git clone https://github.com/miyabayt/spring-boot-doma2-sample.git
```

### 개발 환경（IntelliJ）

#### 필요한 플러그인・설정

- Lombok plugin을 인스톨한다.
  - Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`을 ON으로 한다.
- Eclipse Code Formatter을 인스톨한다.
  - Settings > Other Settings > Eclipse Code Formatter > `Use the Eclipse code formatter`를 ON으로 한다.
    - `Eclipse Java Formatter config file`에 `eclipse-formatter.xml`를 지정한다.
- bootRun을 실행하고 있는 경우에도 빌드되도록 한다.
  - Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`을 ON으로 한다.
- Windows의 경우는, 콘솔 출력이 문자깨짐이 발생하므로、`C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`의 안에 있는 `idea64.exe.vmoptions`파일에 `-Dfile.encoding=UTF-8`을 추기한다.
- 브라우저에 LiveReload기능 확장을 인스톨한다.
  - `http://livereload.com/extensions/`에서 각 브라우저의 기능 확장을 다운로드한다.

### Docker API의 유효화

#### Windows10의 경우
* Settings > General > `Expose daemon on tcp://...`을 ON으로 한다.

#### MacOSX의 경우
* 디폴트로 `unix:///var/run/docker.sock`에 접속할 수 있다.
* TCP로 API를 이용하고 싶은 경우는, 다음을 실시한다.

```bash
$ brew install socat
$ socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock &
```

#### Docker Toolbox의 경우
* 후술할 `Docker의 기동`의 절차를 실시한다.

### Docker의 기동
MySQL등의 서버를 기동한다.

#### Windows10、MacOSX의 경우
```bash
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew composeUp
```

#### Docker Toolbox의 경우
* `application-development.yml`을 편집한다.
  * `spring.datasource.url`의 `127.0.0.1:3306`を`192.168.99.100:3306`으로 변경한다.
* `Docker CLI`로 docker-compose를 실행한다.
```bash
$ cd /path/to/spring-boot-doma2-sample/docker
$ docker-compose up
```

### FakeSMTP의 기동
메일 송신의 테스트를 위해 FakeSMTP을 기동한다.

```bash
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew startFakeSmtpServer
```

### 애플리케이션의 기동

#### 관리쪽
```bash
$ # admin application
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew :sample-web-admin:bootRun
```

#### 프론트쪽
```bash
$ # front application
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew :sample-web-front:bootRun
```

#### 배치
```bash
$ # 담당자 정보 데이터 로드 배치를 기동한다
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew :sample-batch:bootRun -Pargs="--job=importStaffJob"
```

### 접속처 정보
#### 테스트 유저 test@sample.com / passw0rd

| 접속처| URL|
| :-----| :---------------------------------------|
| 관리쪽 화면| http://localhost:18081/admin|
| 관리쪽 API| http://localhost:18081/admin/api/v1/users.json|
| 프론트 쪽| http://localhost:18080/|

#### 데이터베이스 접속처

```bash
# Windows10、MacOSX의 경우
mysql -h 127.0.0.1 -P 3306 -uroot -ppassw0rd sample

# Docker Toolbox의 경우
mysql -h 192.168.99.100 -P 3306 -uroot -ppassw0rd sample
```

### 코드 자동생성（덤）
```bash
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew codegen -PsubSystem=system -Pfunc=client -PfuncStr=取引先 [-Ptarget=dao|dto|repository|service|controller|html]
```

## 참고

| 프로젝트| 개요|
| :---------------------------------------| :-------------------------------|
| [Lombok Project](https://projectlombok.org/)| 정형적인 코드를 작성하지 않아도 된다|
| [Springframework](https://projects.spring.io/spring-framework/)| Spring Framework|
| [Spring Security](https://projects.spring.io/spring-security/)| 시큐리티 대책, 인증・인가의 프레임워크|
| [Doma2](https://doma.readthedocs.io/ja/stable/)| O/R매퍼|
| [spring-boot-doma2](https://github.com/domaframework/doma-spring-boot)| Doma2와 Spring Boot를 연계한다|
| [Flyway](https://flywaydb.org/)| DB마이그레이션 도구|
| [Thymeleaf](http://www.thymeleaf.org/)| 템플릿 엔진|
| [Thymeleaf Layout Dialect](https://ultraq.github.io/thymeleaf-layout-dialect/)| 템플릿을 레이아웃화 한다|
| [WebJars](https://www.webjars.org/)| jQuery등의 클라이언트쪽 라이브러리를 JAR로 로딩|
| [ModelMapper](http://modelmapper.org/)| Bean매핑 라이브러리|
| [Ehcache](http://www.ehcache.org/)| 캐시 라이브러리|
| [Spock](http://spockframework.org/)| 테스트 프레임워크|
| [Mockito](http://site.mockito.org/)| 모킹 프레임워크 |


## Written by hyd 2022.1.10

admin id/pwd : test@sample.com/passw0rd

dbms connection 오류 수정
- url: jdbc:mysql://127.0.0.1:3306/MySQL80?useSSL=false&allowPublickKeyRetrieval=true&characterEncoding=UTF-8

connection url
- web-front : localhost:18080
- web-admin : localhost:18081/admin