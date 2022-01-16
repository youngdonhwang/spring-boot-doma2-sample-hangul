# 코드 생성 플러그인（보너스）

## 플러그인의 설정

다음과 같은 설정을 `build.gradle`에 기술한다.
각 속성은, 생성할 소스의 템플릿으로 사용되고 있으므로,
출력하고 싶은 내용에 맞추어서 변경하도록 한다.

```groovy
apply plugin: com.sample.CodeGenPlugin

codegen {
    domainProjectName = "sample-domain" // 도메인의 프로젝트명
    webProjectName = "sample-web-admin" // 메인 프로젝트（SpringBoot의 메인 메소드를 구현하고 있는）

    commonDtoPackageName = "com.sample.domain.dto.common"             // ID등의 공통적인 엔티티의 패키지명
    daoPackageName = "com.sample.domain.dao"                          // Dao를 배치하는 패키지명
    dtoPackageName = "com.sample.domain.dto"                          // Dto를 배치하는 패키지명
    servicePackageName = "com.sample.domain.service"                  // 서비스를 배치하는 패키지명
    commonServicePackageName = "com.sample.domain.service"            // 서비스의 베이스 클래스를 배치하는 패키지명
    exceptionPackageName = "com.sample.domain.exception"              // 예외 클래스를 배치하는 패키지명
    webBasePackageName = "com.sample.web.base"                        // 컨트롤러 관련 베이스 클래스를 배치하는 패키지명
    baseValidatorPackageName = com.sample.domain.validator            // 벨리데이터를 배치하는 패키지명
    baseControllerPackageName = "com.sample.web.base.controller.html" // 컨트롤러의 베이스 클래스를 배치하는 패키지명
    controllerPackageName = "com.sample.web.admin.controller.html"    // 생성할 컨트롤러의 패키지명
}
```

## 플러그인의 실행

아래의 명령을 실행하면, 최소한의 소스 파일이 생성된다.

```bash
$ cd /path/to/sample-web-admin
$ gradlew codegen -PsubSystem=system -Pfunc=employee -PfuncStr=従業員
```

## 생성 파일의 리스트

아래의 계층으로, 각각의 소스 파일이 작성된다.

```
.
├── sample-domain
│   └── src
│       └── main
│            ├── java
│            │   └── com
│            │        └── sample
│            │             └── domain
│            │                  ├── dao
│            │                  │   └── system
│            │                  │        └── EmployeeDao.java
│            │                  ├── dto
│            │                  │   └── system
│            │                  │        └── Employee.java
│            │                  └── service
│            │                       └── system
│            │                            └── EmployeeDervice.java
│            └── resources
│                 └── META-INF
│                     └── com
│                          └── sample
│                               └── domain
│                                    └── dao
│                                         └── EmployeeDao
│                                              ├── select.sql
│                                              ├── selectAll.sql
│                                              └── selectById.sql
└── sample-web-admin
     └── src
         └── main
              ├── java
              │   └── com
              │        └── sample
              │             └── web
              │                  └── admin
              │                       └── controller
              │                            └── html
              │                                 └── system
              │                                      └── employees
              │                                           ├── EmployeeCsv.java
              │                                           ├── EmployeeForm.java
              │                                           ├── EmployeeFormValidator.java
              │                                           ├── EmployeeHtmlController.java
              │                                           └── SearchEmployeeForm.java
              └── resources
                   └── templates
                        └── modules
                             └── system
                                  └── employees
                                       ├── find.html
                                       ├── new.html
                                       └── show.html
```


```eval_rst
.. note::
   최소한의 소스만을 생성하도록 되어 있으므로,
   필요에 따라 생성원의 템플릿을 편집하거나, 플러그인 자체를 수정하거나 한다。
   템플릿은, buildSrc/src/resources/templates에 보관되어 있다.
```
