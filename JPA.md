# JPA

# Java와 RDBMS의 패러다임 불일치

### 객체 지향인 Java와 관계 지향인 RDBMS는 서로 다른 패러다임을 가지고 있다.

<br>

- 상속 관계의 유무

  Java: 클래스 상속이 존재

  RDBMS: 상속의 개념이 없으며 테이블을 분할 하거나 JOIN 하여 구현해야 함

     SELECT 시 많은 JOIN으로 인한 쿼리문 복잡, INSERT 시 각 테이블에 모두 생성 후 INSERT 등..

<br>

- 연관관계

  Java: 객체 참조를 통한 단방향 관계 (객체 내부에 객체를 참조하여도 역방향으로 참조가 이루어지지 않음)

  RDBMS: 외래키를 통한 양방향 관계 (Key가 연결되어 있으면 어느 테이블을 기준으로 해도 JOIN을 통해 상대방을 참조할 수 있음)

     참조로 연관관계를 맺는 경우 해당 객체를 INSERT 하기 위해 SELECT로 참조하는 값을 찾아야 할 수 있음.

     또한 같은 객체를 조회하는 메서드가 가져오는 값 별로 여러개 생성해야 할 수 있다.

<br>

- 데이터 타입의 차이

  Java: 다양한 객체 타입과 컬렉션 지원

  RDBMS: 제한된 데이터 타입

<br>

# Java Persistence API, ORM

### Object-relational mapping(객체 관계 매핑)

객체는 객체대로 설계하고, RDBMS는 관계형 데이터베이스에 맞게 설계.
ORM 프레임워크가 중간에서 매핑한다.

ex) Java에서 Entity를 받으면 Entity를 분석하고 그에 맞게 INSERT SQL을 생성

## JPA를 왜 사용하는가??

-  SQL 중심 개발에서 객체 중심으로 개발

- 개발자가 직접 관계형 데이터에 맞게 객체를 분해하지 않아 생산성이 증가

- 기존에는 필드 변경시 모든 SQL을 수정해야 했으나 JPA 사용시 필드만 추가하면 SQL을 JPA가 처리하여 유지보수에 용이

- 데이터베이스 방언(Dialect) 설정만으로 다른 DB로 전환이 가능하여 특정 데이터베이스 기술에 종속되지 않음

    ``` yaml
    database-platform: org.hibernate.dialect.MySQLDialect

    # Oracle로 변경 시
    # database-platform: org.hibernate.dialect.OracleDialect
    ```

- 1차 캐시와 쓰기 지연 및 지연 로딩과 즉시 로딩 전략 선택을 통한 성능 향상

<br>

# JPA의 영속성 컨텍스트

### 영속성 컨텍스트란?

- 엔티티를 영구 저장하는 환경

- EntityManager를 통해 Entity 관리

- Entity의 생명주기와 1차 캐시를 제공

<br>

## Entity의 생명주기 관리

- 비영속(new/transient): 영속성 컨텍스트와 전혀 관련 없는 상태

- 영속(managed): 영속성 컨텍스트에 저장된 상태 (entityManager.persist())

- 준영속(detached): 영속성 컨텍스트에 저장되었다가 분리된 상태 (entityManager.detach())

- 삭제(removed): 삭제된 상태 (entityManager.remove())

## Entity 조회와 1차 캐시

entityManager.find() 시 SQL 쿼리를 실행하지 않고 entityManager에 해당 객체가 있는지 확인.

만약 있으면 : 같은 객체를 find 하면 주소값이 같은 완전히 동일한 객체를 반환

만약 없으면 SQL 쿼리 실행 후 해당 객체를 1차 캐시에 저장

## Entity 등록과 INSERT 지연

entityManager에 persist() 로 여러개의 객체 등록 시 entityManagersms 매번 DB에 쿼리를 보내 저장하는 것이 아닌 영속성 컨텍스트에 저장 후 commit시에 한 번에 SQL 쿼리를 전송합니다.

## Entity 수정과 변경 감지

entityManager.find()를 하면 entityManager의 1차 캐시에 객체가 저장됨.
해당 객체의 값을 변경.
commit시에 해당 객체와 이전의 스냅샷을 비교하여 객체의 값이 변경되었다면 UPDATE 쿼리 전송.

## Entity 삭제

entityManager.find() 이후 entityManager.remove() 로 객체 삭제

<br>

# 플러시 (flush)

영속성 컨텍스트의 변경내용을 데이터베이스에 반영하는 방식

1. entityManager.flush() 로 직접 호출

2. JPQL 쿼리 실행 (flush가 쿼리 전에 자동으로 실행)

3. commit시 반영 (트랜잭션 커밋)

``` java
A a = new A();

entityManager.persist(a);  // 영속성 컨텍스트에 저장, DB에는 아직 반영되지 않음
            
// 1. entityManager.flush() 로 직접 호출
entityManager.flush();
            
// 2. JPQL 쿼리 실행 (flush가 쿼리 전에 자동으로 실행)
entityManager.createQuery("select a from A a", A.class).getResultList();
            
// 3. commit시 반영 (트랜잭션 커밋)
entityTransaction.commit();  // 커밋 시 플러시 자동 호출
```

### JPQL 쿼리 실행시 플러시가 자동으로 호출되는 이유

entityManager.persist() 로 등록한 객체를 JPQL 쿼리로 SELECT 하는 경우 트랜잭션 커밋이나 entityManager.flush()로 직접 DB에 등록하지 않았다면 DB에 값이 아직 존재하는 상태가 아님.

그러나 JPQL은 DB에서 직접 조회하므로, 영속성 컨텍스트의 변경사항이 DB에 반영되어 있어야 함.

따라서 JPQL 쿼리 실행 전에 자동으로 flush()를 호출 하도록 되어있습니다. (설정에서 변경 가능하나 권장하지 않음)

### 주의사항

플러시는 영속성 컨텍스트를 비우는 것이 아닌, 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화 하는 것.