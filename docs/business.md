# 업무 로직

## 선언적 트랜젝션 관리

예제 구현에서는, `BaseTransactionalService`을 계승함으로써,
선언적 트랜젝션 관리가 적용된다. 트랜젝션 관리가 불필요한 경우는,
`BaseService`를 계승한다.


```java
@Service
public class UserService extends BaseTransactionalService { // ★親クラスで@Transactionalを宣言済み

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    // ...

    // ★AOP에서 이 메소드를 둘러싸도록 DB트랜젝션의 시작・종료가 실시된다
    // ★예외가 발생한 경우는 롤백된다
    public User create(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");

        // 1건 등록
        userDao.insert(inputUser); // ★1번째 테이블

        // 역할 권한 관련 테이블에 등록한다
        val userRole = new UserRole();
        userRole.setUserId(inputUser.getId());
        userRole.setRoleKey("user");
        userRoleDao.insert(userRole); // ★2번째 테이블

        return inputUser;
    }
}

```

`BaseTransactionalService`를 계승함으로써, 반드시 `@Transactional`의 지정이 실시된다.
단, 읽기 전용의 메소드에는, `@Transactional(readOnly = true)`를 지정할 필요가 있다.

### 페이징 처리

```java
    @Transactional(readOnly = true) // Read Only인 경우는 지정한다
    public Page<User> findAll(User where, Pageable pageable) { // ★몇 건씩 취득할지, 몇 페이지째를 취득할지를 Pageable에 설정하여 인수에 전달한다.
        Assert.notNull(where, "where must not be null");

        // 페이징을 지정한다
        val options = createSearchOptions(pageable).count(); // ★Pageableを元にDoma2のSelectOptionsを作成する
        val users = userDao.selectAll(where, options, toList());

        // ★SelectOptions의 count메소드를 호출하면, 건수 취득과 레코드 취득이 하나의 SQL로 이루어질 수 있다
        return PageFactory.create(users, pageable, options.getCount()); // 팩토리 메소드에 리스트를 건네어 Page객체로 포장해서 건넨다
    }
```

```eval_rst
.. note::
   화면쪽의 프로젝트가 Doma2라이브러리를 의존관계로 갖지 않아도 괜찮도록, Doma2의 SelectOptions을 직접 받지 않도록 한다.
   Page 인터페이스의 구현을 나중에 변경하고 싶을 경우는 일괄 수정하지 않아도 괜찮도록 팩토리 메소드를 이용한다.
```
