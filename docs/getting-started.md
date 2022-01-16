# Getting started

## JDK의 설치

- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)를 다운로드하여 설치한다.
- 환경변수 `JAVA_HOME`을 설정한다.

## IDE의 설치

- Eclipse의 경우
    1. Eclipse를 다운로드한다.（[STS](https://spring.io/tools/sts)、[Pleiades](http://mergedoc.osdn.jp/)등）
    2. Gradle 플러그인이 없을 경우는 설치한다.
    3. [Lombok](https://projectlombok.org/download)를 다운로드한다.
        * 다운로드한 `lombok.jar`를 더블 클릭하여 기동한다.
        * Eclipse가 검출한 것을 확인하여 「Install」버튼을 누른다.
    4. `eclipse.ini`파일에,「-javaagent:lombok.jar」가 추기되어 있으므로, Eclipse를 재기동한다.

- IntelliJ의 경우
    1. Lombok plugin를 인스톨한다.
        * Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`를 ON으로 한다.
    2. Eclipse Code Formatter를 인스톨한다.
        * Settings > Other Settings > Eclipse Code Formatter > `Use the Eclipse code formatter`를 ON으로 한다.
        * `Eclipse Java Formatter config file`에 `eclipse-formatter.xml`를 지정한다.
    3. bootRun를 실행하고 있는 경우에도 빌드되도록 한다.
        * Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`를 ON으로 한다.
    4. Windows의 경우는, 콘솔 출력이 문자깨짐 현상이 발생하므로, `C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`의 안에 있는 `idea64.exe.vmoptions`파일에 `-Dfile.encoding=UTF-8`을 추기한다.

- 공통
    1. 브라우저에 LiveReload 기능 확장을 설치한다.
        * `http://livereload.com/extensions/`로부터 각 브라우저의 기능 확장을 다운로드한다.
