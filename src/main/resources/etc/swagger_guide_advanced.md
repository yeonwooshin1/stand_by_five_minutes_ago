# Swagger (OpenAPI) 심화 가이드

이 문서는 기본적인 Swagger 설정을 넘어, 실제 프로덕션 환경과 포트폴리오에서 API 문서의 완성도를 높일 수 있는 심화 기능들을 안내합니다.

---


### 1. API 보안(Security) 설정 (JWT 예시)

인증이 필요한 API를 문서화하고 Swagger UI에서 직접 인증 토큰을 사용하여 테스트할 수 있도록 설정하는 것은 매우 중요합니다. 여기서는 JWT Bearer 토큰 방식을 예시로 설명합니다.

#### 설정 방법 (`@Configuration` Bean)

`OpenApiConfig`와 같은 설정 클래스에 `OpenAPI` Bean을 커스터마이징하여 보안 설정을 추가합니다.

**위치**: `*Config.java` (예: `com.example.project.config.OpenApiConfig.java`)

```java
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

        // 1. SecurityScheme 설정
        // API 문서 상단의 "Authorize" 버튼에서 사용할 인증 스키마를 정의합니다.
        String jwtSchemeName = "jwtAuth";
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP) // HTTP 기반 인증
                .scheme("bearer") // Bearer 토큰 방식
                .bearerFormat("JWT"); // 토큰 형식 지정

        // 2. SecurityRequirement 설정
        // API 요청 시 전역적으로 Authorization 헤더에 토큰을 포함하도록 설정합니다.
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes(jwtSchemeName, jwtSecurityScheme))
                .addSecurityItem(securityRequirement);
    }
}
```

#### 적용 결과

- Swagger UI 우측 상단에 **"Authorize"** 버튼이 생성됩니다.
- 이 버튼을 클릭하여 `Bearer {토큰값}` 형식으로 토큰을 입력하면, 이후 모든 API 요청의 헤더에 `Authorization`이 자동으로 포함되어 인증된 API를 테스트할 수 있습니다.
- 특정 API만 인증이 필요하거나, 전역 설정을 사용하고 싶지 않다면 `@SecurityRequirement(name = "jwtAuth")` 어노테이션을 해당 컨트롤러나 메소드에 직접 붙여 적용할 수도 있습니다.

---


### 2. 상세한 데이터 모델(@Schema) 정의

`@Schema` 어노테이션의 다양한 속성을 활용하면 DTO(데이터 모델)의 제약 조건과 규칙을 명확하게 표현할 수 있습니다.

| `@Schema` 속성 | 설명 | 예시 |
| --- | --- | --- |
| `description` | 필드에 대한 상세 설명 | `@Schema(description = "사용자 이메일 주소")` |
| `defaultValue` | 필드의 기본값 | `@Schema(defaultValue = "1")` |
| `example` | 필드의 예시 데이터 | `@Schema(example = "gildong@example.com")` |
| `requiredMode` | 필드의 필수 여부 (`REQUIRED`, `NOT_REQUIRED`, `READ_ONLY`) | `@Schema(requiredMode = Schema.RequiredMode.REQUIRED)` |
| `minLength`, `maxLength` | 문자열의 최소/최대 길이 | `@Schema(minLength = 2, maxLength = 10)` |
| `minimum`, `maximum` | 숫자의 최소/최대값 | `@Schema(minimum = "0", maximum = "150")` |
| `pattern` | 필드가 만족해야 하는 정규식 | `@Schema(pattern = "^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$`)" |
| `allowableValues` | 허용되는 값의 목록 (Enum처럼 사용) | `@Schema(allowableValues = {"USER", "ADMIN"})` |

#### 적용 예제 (DTO 클래스)

**위치**: `*.dto.*.java` (예: `com.example.project.dto.UserCreateRequest.java`)

```java
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*; // Bean Validation 어노테이션과 함께 사용하면 더욱 강력합니다.

@Schema(description = "사용자 생성을 위한 요청 DTO")
public class UserCreateRequest {

    @Schema(description = "사용자 이름", minLength = 2, maxLength = 20, example = "홍길동")
    @NotBlank
    private String username;

    @Schema(description = "사용자 이메일", pattern = "^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    private String email;

    @Schema(description = "사용자 나이", minimum = "19", maximum = "100", example = "25")
    @Min(19)
    private int age;

    @Schema(description = "사용자 역할", defaultValue = "USER", allowableValues = {"USER", "ADMIN"})
    private String role;
}
```

---


### 3. 공통 응답 및 에러 처리 명세

여러 API에서 공통적으로 사용되는 응답, 특히 에러 응답을 중앙에서 관리하면 문서의 일관성과 재사용성이 높아집니다.

#### 1. 공통 에러 응답 DTO 생성

**위치**: `*.dto.response.java`
```java
@Schema(description = "API 에러 응답 DTO")
public class ErrorResponse {
    @Schema(description = "HTTP 상태 코드", example = "404")
    private int code;
    @Schema(description = "에러 메시지", example = "사용자를 찾을 수 없습니다.")
    private String message;
}
```

#### 2. `@ApiResponse`에서 공통 응답 참조

`@ApiResponses` 내에서 `@Content`의 `schema` 속성을 사용하여 공통 DTO를 참조합니다.

**위치**: `*Controller.java`
```java
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

// ... Controller 내부 ...
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "조회 성공"),
    @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
@GetMapping("/{userId}")
// ... 메소드 선언 ...
```

---


### 4. 기타 유용한 기능

- **`@ParameterObject`**: 여러 개의 요청 파라미터를 받는 GET 요청의 경우, 각 파라미터를 DTO 객체로 묶어 `@ParameterObject`를 사용하면 문서가 훨씬 깔끔해집니다.
- **`@Operation(deprecated = true)`**: 더 이상 사용되지 않는 API임을 명시합니다. Swagger UI에서 해당 API에 취소선이 그어집니다.
- **`@Hidden`**: 특정 API나 컨트롤러 전체를 Swagger 문서에서 숨깁니다. 내부용 API 등에 사용하면 유용합니다.
- **파일 업로드/다운로드**: 컨트롤러 메소드의 파라미터로 `MultipartFile`을 사용하면, `springdoc`이 자동으로 파일 업로드를 위한 UI를 생성해 줍니다. 응답의 `produces` 타입을 `application/octet-stream` 등으로 지정하면 파일 다운로드로 명세할 수 있습니다.

이러한 심화 기능들을 활용하여 API 문서를 작성하면, 단순히 기능을 나열하는 것을 넘어 API의 사용 규칙과 제약 조건을 명확히 전달하는 전문적인 명세가 완성됩니다. 이는 협업 효율을 높이고 포트폴리오의 기술적 깊이를 더해줄 것입니다.
