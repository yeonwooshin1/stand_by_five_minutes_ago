# Swagger (OpenAPI) & Springdoc-openapi 가이드

Spring Boot 프로젝트에서 API 문서를 자동으로 생성하고, 편리하게 테스트할 수 있는 환경을 구축하는 방법을 안내합니다.

---

### 1. Swagger와 OpenAPI란?

- **OpenAPI Specification (OAS):** RESTful API를 명세하기 위한 표준 사양입니다. API의 엔드포인트, 요청/응답 데이터 모델, 인증 방법 등을 기계가 읽을 수 있는 형식(YAML 또는 JSON)으로 정의하는 규칙입니다.
- **Swagger:** OpenAPI 사양을 기반으로 API를 설계, 빌드, 문서화하고 테스트하는 데 도움이 되는 오픈소스 도구 모음입니다.
    - **Swagger UI:** OpenAPI 명세서를 웹 페이지에서 시각적으로 확인하고 직접 API를 호출하며 테스트할 수 있는 UI 도구입니다.
    - **Swagger Editor:** OpenAPI 명세서를 작성하고 편집하는 웹 기반 에디터입니다.

**Springdoc-openapi**는 Spring Boot 프로젝트에서 이러한 OpenAPI 3.0 기반의 문서를 쉽게 생성하고 Swagger UI를 통합해주는 라이브러리입니다.

### 2. 사용하는 이유

- **자동화된 API 문서:** 코드에 작성된 어노테이션을 기반으로 API 문서를 자동으로 생성해주므로, 별도의 문서 작업 시간을 크게 줄일 수 있습니다.
- **인터랙티브 테스트 환경:** 프론트엔드 개발자나 API 사용자가 Swagger UI를 통해 직접 API를 호출하고 응답을 확인할 수 있어, 커뮤니케이션 비용이 감소하고 개발 효율이 향상됩니다.
- **API 명세의 중앙 관리:** API의 모든 정보가 코드와 함께 관리되므로, 코드와 문서 간의 불일치 문제를 방지할 수 있습니다.

---

### 3. 설정 방법 (Maven/Gradle)

Spring Boot 3.x 버전을 기준으로, `build.gradle` 또는 `pom.xml`에 아래 의존성을 추가하기만 하면 기본 설정이 완료됩니다.

**Maven (`pom.xml`)**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version> <!-- 최신 버전은 Maven Central에서 확인 -->
</dependency>
```

**Gradle (`build.gradle`)**
```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0' // 최신 버전은 Maven Central에서 확인
```

의존성을 추가하고 애플리케이션을 실행한 뒤, 아래 주소로 접속하면 Swagger UI 화면을 볼 수 있습니다.

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI 명세서(JSON):** `http://localhost:8080/v3/api-docs`

---

### 4. 상세 설정 및 커스터마이징

`springdoc-openapi`는 다양한 어노테이션을 제공하여 API 문서를 더 상세하고 풍부하게 만들 수 있습니다.

#### 4.1. 주요 어노테이션

| 어노테이션 | 설명 | 위치 |
| --- | --- | --- |
| **`@Operation`** | 특정 엔드포인트(API)에 대한 설명, 요약 등을 추가합니다. | 메소드 |
| **`@Parameter`** | 파라미터에 대한 설명, 예시 등을 추가합니다. | 파라미터 |
| **`@Schema`** | DTO(모델) 객체나 그 필드에 대한 설명, 제약 조건 등을 추가합니다. | 클래스, 필드 |
| **`@ApiResponses`, `@ApiResponse`** | API의 가능한 응답 상태(200, 404, 500 등)와 그에 대한 설명을 정의합니다. | 메소드 |
| **`@Tag`** | API들을 특정 그룹(태그)으로 묶어서 보여줍니다. (예: "사용자 API", "게시글 API") | 클래스 |

#### 4.2. 적용 예제

`UserController.java`
```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User APIs", description = "사용자 관련 API 명세")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "사용자 정보 조회", description = "사용자 ID를 통해 특정 사용자의 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "조회할 사용자의 ID", required = true, example = "1")
            @PathVariable Long userId) {
        
        // ... 사용자 조회 로직 ...
        UserDto user = new UserDto("John Doe", "john.doe@example.com");
        return ResponseEntity.ok(user);
    }
}

@Schema(description = "사용자 정보 DTO")
class UserDto {
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "사용자 이메일", example = "gildong@example.com")
    private String email;
    
    // 생성자, Getter, Setter
    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
    // ...
}
```

---

### 5. 전역 설정

`application.yml` 파일이나 `@Configuration` Bean을 통해 API 문서의 제목, 버전, 설명 등 전역적인 정보를 설정할 수 있습니다.

#### 5.1. `application.yml`을 이용한 설정
```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    display-request-duration: true
    groups-order: DESC
  info:
    title: My Project API
    description: 내 프로젝트를 위한 API 명세서입니다.
    version: v1.0.0
```

#### 5.2. `@Configuration` Bean을 이용한 설정
```java
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("My Project API")
                .version("v1.0.0")
                .description("내 프로젝트를 위한 API 명세서입니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
```
