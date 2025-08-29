# 체크리스트 템플릿(CTem) 생성 기능 개선 제안

안녕하세요! `CTem` 생성 관련 코드를 검토해 보았습니다.

현재 코드는 `CTemController` -> `CTemService` -> `CTemDao`로 이어지는 흐름을 가지고 있습니다. 하지만 Spring Boot 프레임워크의 장점을 제대로 활용하지 못하고 있어 여러 가지 잠재적인 문제점을 안고 있습니다.

**결론부터 말씀드리면, 현재 코드는 동작하지 않거나, 동작하더라도 매우 비효율적이고 보안에 취약합니다.** 아래에 주요 문제점과 개선 방안을 정리했습니다.

---

### 주요 문제점 및 개선 방안

#### 1. 데이터베이스 연결 방식 (가장 시급한 문제)

*   **문제점:**
    *   `Dao` 클래스에서 `DriverManager.getConnection()`을 사용하여 매번 새로운 DB 커넥션을 생성하고 있습니다. Spring Boot 애플리케이션에서는 커넥션 풀(Connection Pool)을 사용하는 것이 표준입니다. 매번 커넥션을 생성하고 닫는 것은 매우 비효율적이며 시스템에 큰 부하를 줍니다.
    *   DB 연결 정보(URL, 사용자, 비밀번호)가 코드에 하드코딩되어 있습니다. 이는 보안상 매우 취약하며, 연결 정보가 변경될 때마다 코드를 수정하고 다시 빌드해야 합니다.
    *   `CTemDao`와 같은 각 DAO가 `Dao`를 상속받아 자신만의 커넥션을 갖게 됩니다. 이는 트랜잭션 관리를 불가능하게 만듭니다.

*   **개선 방안:**
    1.  **`Dao.java` 클래스를 삭제합니다.**
    2.  `src/main/resources/application.properties` 파일에 DB 연결 정보를 추가하여 Spring Boot가 자동으로 커넥션 풀(HikariCP)을 관리하도록 설정합니다.

    ```properties
    # src/main/resources/application.properties

    # Database Settings
    spring.datasource.url=jdbc:mysql://localhost:3306/project
    spring.datasource.username=root
    spring.datasource.password=1234
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # JPA Settings (Option B - Recommended)
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

#### 2. DAO 계층 구현 방식

*   **문제점:**
    *   `PreparedStatement`, `ResultSet` 등을 직접 사용하는 전통적인 JDBC 방식은 코드가 길어지고, 리소스(Connection, PreparedStatement, ResultSet)를 수동으로 닫아주어야 하는 번거로움이 있습니다. 누락 시 리소스 누수로 이어질 수 있습니다.
    *   `try-catch` 블록에서 `Exception`을 잡아서 단순히 `System.out.println(e)`로 처리하고 `0`을 반환합니다. 이는 실제 에러 원인을 파악하기 어렵게 만들고, 호출한 쪽(서비스, 컨트롤러)에서는 성공했는지 실패했는지 정확히 알 수 없습니다.

*   **개선 방안 (A: `JdbcTemplate` 사용):**
    *   Spring Boot가 제공하는 `JdbcTemplate`을 사용하여 반복적인 JDBC 코드를 대폭 줄일 수 있습니다. `JdbcTemplate`은 리소스 관리와 예외 처리를 자동으로 해줍니다.

    **`CTemDao.java` 수정 예시:**
    ```java
    package five_minutes.model.dao;

    import five_minutes.model.dto.CTemDto;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.support.GeneratedKeyHolder;
    import org.springframework.jdbc.support.KeyHolder;
    import org.springframework.stereotype.Repository;

    import java.sql.PreparedStatement;
    import java.sql.Statement;

    @Repository
    public class CTemDao { // extends Dao 삭제

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public CTemDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public int createCTem(CTemDto cTemDto) {
            String sql = "insert into checktemplate (ctName , ctDescription , bnNo) values (? , ? , ?) ";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, cTemDto.getCtName());
                ps.setString(2, cTemDto.getCtDescription());
                ps.setString(3, cTemDto.getBnNo());
                return ps;
            }, keyHolder);

            // 생성된 PK(ctNo)를 반환
            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().intValue();
            } else {
                // 실패 시 예외를 발생시키거나 0을 반환하여 서비스 계층에서 처리
                // throw new RuntimeException("Failed to create CTem and retrieve key.");
                return 0;
            }
        }
    }
    ```

*   **개선 방안 (B: Spring Data JPA 사용 - 강력 추천):**
    *   Spring Boot에서 가장 권장되는 데이터 접근 방식입니다. `interface`를 선언하는 것만으로 기본적인 CRUD 메소드가 자동으로 구현됩니다.
    1.  `CTemDto`를 `@Entity` 어노테이션을 사용한 `CTem` 엔티티 클래스로 변경합니다.
    2.  `JpaRepository`를 상속받는 `CTemRepository` 인터페이스를 생성합니다.
    3.  `CTemDao` 클래스는 더 이상 필요 없으므로 삭제합니다.

    **`CTem.java` (Entity):**
    ```java
    package five_minutes.model.entity; // 패키지 변경

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    // ... 다른 lombok 어노테이션

    @Entity
    @Table(name = "checktemplate")
    @Getter @Setter
    public class CTem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int ctNo;
        private String ctName;
        private String ctDescription;
        private int ctStatus;
        // ... createDate, updateDate 등
        private String bnNo;
    }
    ```

    **`CTemRepository.java` (Repository):**
    ```java
    package five_minutes.model.repository; // 패키지 변경

    import five_minutes.model.entity.CTem;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface CTemRepository extends JpaRepository<CTem, Integer> {
    }
    ```

    **`CTemService.java` (JPA 사용 시):**
    ```java
    package five_minutes.service;

    import five_minutes.model.dto.CTemDto;
    import five_minutes.model.entity.CTem;
    import five_minutes.model.repository.CTemRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class CTemService {
        private final CTemRepository cTemRepository; // Dao 대신 Repository 주입

        public int createCTem(CTemDto cTemDto) {
            // DTO를 Entity로 변환
            CTem cTem = new CTem();
            cTem.setCtName(cTemDto.getCtName());
            cTem.setCtDescription(cTemDto.getCtDescription());
            cTem.setBnNo(cTemDto.getBnNo());

            // save 메소드는 저장된 엔티티를 반환. ID가 포함되어 있음.
            CTem savedCTem = cTemRepository.save(cTem);
            return savedCTem.getCtNo();
        }
    }
    ```

---

### 요약

1.  **`Dao.java`를 삭제**하고 `application.properties`에 DB 설정을 추가하여 **Spring Boot가 DB 커넥션을 관리**하게 하세요.
2.  기존 DAO 클래스들을 **Spring Data JPA Repository** 방식으로 변경하는 것을 **강력히 추천**합니다. 이렇게 하면 코드가 매우 간결해지고 안정성이 높아집니다.
3.  위와 같이 수정하면 `CTem` 생성 기능이 Spring Boot 표준에 맞게 안정적으로 동작할 것입니다.

현재 프로젝트의 다른 부분(`CTItem` 등)도 동일한 방식으로 개선해야 합니다.
