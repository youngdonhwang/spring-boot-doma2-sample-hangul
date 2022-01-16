# 배타제어

## 낙관적 배타제어

예제의 구현에서는, 낙관적 배타제어는 하기의 흐름으로 동작하도록 되어 있다.

1. 화면 표시 처리에서, 개정번호를 포함하여 SELECT문을 발행한다.
2. SessionAttribute에 지정된 Form객체를 사용하여, 개정번호를 세션에 보관한다.
3. 보관 버튼이 눌린 후, 보관 처리에서 세션에 보관된 개정번호를 WHERE구에 지정해 UPDATE문을 발행한다.
4. 갱신 건수가 0인 경우는, 배타 오류로 한다.

공통의 엔티티에, 배타제어용의 필드를 정의하고 있다.
복수의 테이블에 대해 동시 배타제어는, 여기서는 취급하지 않는다.

낙관적 배타제어는, Doma2의 기능을 이용하고 있으므로 아래의 문서를 참조하길 바란다
[Doma 2.0 SQL의 자동 생성에 의한 갱신](http://doma.readthedocs.io/ja/stable/query/update/#sql)

```java
public abstract class DomaDtoImpl implements DomaDto, Serializable {
    // ..

    // 낙관적 배타제어에서 사용하는 개정번호
    @Version // ★낙관적 배타제어에서 사용하는 항목임을 나타낸다
    @Column(name = "version") // ★DB의 컬럼명
    @JsonIgnore // 응답할 JSON에 포함되지 않는 항목
    Integer version;
}
```

## 비관적 배타제어

비관적 배타제어에 대해서도, Doma2의 기능을 이용하고 있으므로, 아래의 문서를 참고하길 바란다.
[Doma 2.0 검색 - 비관적 배타제어](http://doma.readthedocs.io/ja/stable/query/select/#id13)

페이징 처리와 마찬가지로, SelectOptions을 작성하여, `forUpdate`메소드를 호출한 다음 dao의 인수로 건냄으로써,
SELECT ...FOR UPDATE문이 발행된다.

```java
// 비관적 배타제어
val options = createSearchOptions(pageable).forUpdate(); // ★Pageable를 바탕으로 Doma2의 SelectOptions을 작성한다
val users = userDao.selectAll(where, options, toList()); // ★SELECT ...FOR UPDATE
```
