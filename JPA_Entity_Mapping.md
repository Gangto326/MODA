# JPA Entity 매핑

### Java 객체와 RDBMS 테이블 간의 관계를 정의하는 것.

## 기본 Entity 매핑

``` java
/**
* @Entity: JPA가 관리할 엔티티 클래스임을 명시
* @Table: 매핑할 테이블 정보를 명시
*/
@Entity
@Table(name = "users") // 실제 데이터베이스 테이블 이름을 매핑. 해당 속성을 생략하면 Entity 이름인 User 로 자동 매핑됨.
public class User {
    /**
    * @Id: PK 매핑
    * @GeneratedValue: PK 생성
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * IDENTITY: PK의 생성을 DB에 위임하여 자동으로 생성. (MySQL의 AUTO_INCREMENT)
     * 
     * 설정이 IDENTITY일 경우 commit 시가 아닌 entityManager.persist() 호출 마다 INSERT SQL 쿼리 실행.
     * IDENTITY 설정은 DB에 INSERT한 후에야 ID 값을 알 수 있음. 하지만 영속성 컨텍스트가 Entity를 관리하기 위해선 반드시 ID 값이 필요하기 때문.
     * 
     * SEQUENCE: DB 시퀀스를 사용하여 기본키 할당.
     * 
     * 시퀀스란?
     * 유일한 숫자를 자동으로 생성해주는 DB 객체. (Oracle, PostgreSQL, H2, DB2 등)
     * 
     * MySQL의 AUTO_INCREMENT 는 테이블에 종속적이나 SEQUENCE 는 독립적인 객체로 여러 테이블에서 공유할 수 있음.
     * 대량 INSERT 설정 시 IDENTITY 설정은 매번 쿼리를 전송 (쓰기 지연 활용 불가)
     * But, SEQUENCE 설정 시 시퀀스를 한번에 50확보하여 INSERT 쿼리를 commit시에 한 번에 보낼 수 있음 (한정적인 쓰기 지연 활용 가능)
     * 
     * TABLE: KEY를 생성하는 별도의 테이블을 만들어 시퀀스 설정을 흉내내는 방식. 별도의 테이블을 관리하여 성능이 좋지 않음.
     * 
     * AUTO: DB 언어(방언)에 따른 자동 선택. ex) Oracle == SEQUENCE, MySQL == IDENTITY
     */
    private Long id;
    
    /**
     * @Column: 컬럼 매핑
     * name: 실제 데이터베이스 컬럼명
     * length: 문자열 길이 제한
     * nullable: null 허용 여부 (nullable = false == NOT NULL)
     */
    @Column(
        name = "user_name",
        length = 100,
        nullable = false,
        updatable = true, // 업데이트 가능 여부
        columnDefinition = "VARCHAR(100) DEFAULT 'GUEST'" // 직접 컬럼 정의
    )
    private String userName;
    
    /**
     * @Temporal: 날짜 타입 매핑 (DATE, TIME, TIMESTAMP)
     * 
     * Java 8 부터 도입된 LocalDateTime, LocalDate 등의 새로운 날짜 API 등장으로 현재는 잘 사용하지 않음. (자동 매핑이 됨)
     */
    @Temporal(TemporalType.TIMESTAMP)
    /**
     * JPA는 기본적으로 SpringPhysicalNamingStrategy를 통해 camelCase와 snake_case를 자동 변환 후 매핑.
     * 
     * USER_EMAIL -> user_email (대문자도 스네이크케이스로 변환)
     */
    @Column(name = "created_date")
    private Date createdDate;
    
    // @Transient: 매핑하지 않을 필드. 데이터베이스에 저장하지 않음.
    @Transient
    private String temporaryField;
    
    /**
     * @Enumerated: enum 타입 매핑
     * 
     * STRING: Enum 으로 설정한 String 형태 그대로 저장
     * ORDINAL: Enum 으로 설정한 순서에 맞게 0, 1, 2 등의 숫자로 저장.
     * 
     * ORDINAL을 사용하면 안 되는 이유?
     * Enum 객체의 첫 번째에 값을 추가하게 되면 DB에 저장된 모든 값이 변질될 수 있음.
     */
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
```
<br>

## 연관관계 매핑

``` java
// 일대다(One-to-Many) 관계의 부모 엔티티
@Entity
@Table(name = "teams")
public class Team {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * mappedBy: 양방향 관계에서 주인이 아닌 쪽에서 관계를 명시할 때 사용
     * 
     * 외래 키가 있는 곳을 연관관계의 주인으로 정해야 함. 따라서 @ManyToOne 쪽이 항상 연관관계의 주인이 됨.
     * 잘못된 방법을 사용할 경우 TEAM_ID가 NULL이 되거나, 조회가 되지 않는 등의 문제 발생.
     * 
     * 
     * cascade: 영속성 전이 설정
     * 해당 설정이 있으면 Team만 저장해도 members가 함께 저장됨.
     * 
     * CascadeType 종류
     *  - ALL: 모든 작업을 전파
     *  - PERSIST: 저장할 때만 전파
     *  - REMOVE: 삭제할 때만 전파
     *  - MERGE: 업데이트할 때만 전파
     * 
     * @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})와 같이 여러개를 지정하 수 있음.
     * 
     * cascade 사용 시 유의사항 
     *  - 여러 엔티티에서 참조하는 경우 삭제 전파 시 다른 참조가 있음에도 삭제되는 등의 문제 발생 가능.
     * 
     *  - 독립적인 라이프사이클을 가지는 경우 저장이나 삭제를 전파하여 같이 진행할 시 문제가 발생할 수 있음.
     * 
     *  - 중요한 정보는 cascade 사용을 권장하지 않음.
     * 
     * 
     * orphanRemoval: 고아 객체(부모와의 관계가 끊어진 객체) 제거
     */
    @OneToMany(
        mappedBy = "team",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Member> members = new ArrayList<>();
    
    // 연관관계 편의 메서드
    public void addMember(Member member) {
        this.members.add(member);
        member.setTeam(this);
    }
}

// 다대일(Many-to-One) 관계의 자식 엔티티
@Entity
@Table(name = "members")
public class Member {
    @Id 
    @GeneratedValue
    private Long id;
    
    /**
     * @ManyToOne: 다대일 관계 설정
     * @ManyToOne과 @OneToOne은 기본 설정이 FetchType.EAGER (즉시 로딩) 이므로 명시적으로 FetchType.LAZY (지연 로딩) 설정
     * 
     * @JoinColumn: 외래키 매핑
     * JoinColumn생략 시 {참조하는_테이블명}_{참조되는_테이블의_기본키_컬럼명}로 설정됨.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "team_id", // 외래키 컬럼명
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_member_team") // 외래키 제약조건명
    )
    private Team team;
}
```