# Jakarta Bean Validation & Hibernate Validator 가이드

Java 애플리케이션에서 데이터 유효성 검증을 체계적이고 선언적으로 처리하는 방법을 안내합니다.

---

### 1. Jakarta Bean Validation이란?

Jakarta Bean Validation(과거 Java Bean Validation)은 Java 객체의 데이터 유효성 검증을 위한 표준 사양(Specification)입니다. 실제 코드가 아닌 **어노테이션(Annotation)**을 사용하여 객체의 필드에 제약 조건을 선언하고, 이를 검증하는 프레임워크입니다.

**Hibernate Validator**는 이 Jakarta Bean Validation 사양을 구현한 가장 널리 사용되는 구현체(Implementation)입니다.

### 2. 사용하는 이유

- **비즈니스 로직 분리:** 유효성 검증 로직을 비즈니스 로직과 분리하여 코드가 깔끔해지고 유지보수가 쉬워집니다.
- **재사용성:** 모델 객체에 직접 제약 조건을 정의하므로, 해당 객체가 사용되는 모든 곳에서 동일한 검증 규칙을 재사용할 수 있습니다.
- **표준화 및 통합:** Spring Boot와 같은 현대적인 프레임워크와 완벽하게 통합되어, 몇 가지 설정만으로 강력한 유효성 검증 기능을 자동화할 수 있습니다.
- **가독성:** `@NotNull`, `@Size(max=10)` 등 직관적인 어노테이션을 사용하므로 어떤 제약 조건이 있는지 한눈에 파악하기 쉽습니다.

---

### 3. 설정 방법 (Maven/Gradle)

Spring Boot 프로젝트에서는 `spring-boot-starter-web` 의존성에 기본적으로 포함되어 있어 별도 추가 없이 바로 사용할 수 있습니다. 만약 포함되어 있지 않다면 아래와 같이 의존성을 추가합니다.

**Maven (`pom.xml`)**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Gradle (`build.gradle`)**
```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

---

### 4. 주요 어노테이션

| 어노테이션 | 설명 |
| --- | --- |
| **`@NotNull`** | `null` 값을 허용하지 않습니다. |
| **`@NotEmpty`** | `null`과 빈 문자열(`""`), 빈 컬렉션을 허용하지 않습니다. (공백만 있는 문자열은 허용) |
| **`@NotBlank`** | `null`과 빈 문자열, 공백만 있는 문자열(`" "`)을 허용하지 않습니다. (문자열 전용) |
| **`@Size(min, max)`** | 문자열, 배열, 컬렉션의 크기가 지정된 범위 안에 있어야 합니다. |
| **`@Min(value)`** | 숫자 값이 지정된 최솟값 이상이어야 합니다. |
| **`@Max(value)`** | 숫자 값이 지정된 최댓값 이하이어야 합니다. |
| **`@Email`** | 올바른 이메일 형식이어야 합니다. |
| **`@Pattern(regexp)`** | 값이 지정된 정규 표현식과 일치해야 합니다. |
| **`@Positive` / `@PositiveOrZero`** | 양수 / 0 또는 양수여야 합니다. |
| **`@Future` / `@Past`** | 날짜/시간 값이 미래 / 과거여야 합니다. |

---

### 5. 사용 방법

#### 5.1. DTO(Data Transfer Object)에 적용하기

회원가입 요청을 처리하는 DTO 객체에 유효성 검증 어노테이션을 적용하는 예제입니다.

`UserSignUpDto.java`
```java
import jakarta.validation.constraints.*;

public class UserSignUpDto {

    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    @Size(min = 2, max = 10, message = "사용자 이름은 2자 이상 10자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$", message = "비밀번호는 8자 이상이며, 영문과 숫자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "나이는 필수 입력 항목입니다.")
    @Min(value = 19, message = "19세 이상만 가입할 수 있습니다.")
    private Integer age;

    // Getters and Setters
}
```

#### 5.2. Spring Boot Controller에서 검증하기

Spring Boot의 Controller에서 요청 본문(`@RequestBody`)을 받을 때 `@Valid` 어노테이션을 붙이면, DTO에 정의된 제약 조건에 따라 유효성 검증이 자동으로 수행됩니다.

- **검증 성공 시:** 비즈니스 로직이 정상적으로 실행됩니다.
- **검증 실패 시:** `MethodArgumentNotValidException` 예외가 발생하며, 이 예외를 처리하여 클라이언트에게 적절한 오류 메시지를 반환해야 합니다.

`UserController.java`
```java
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpDto userDto, BindingResult bindingResult) {
        // @Valid 어노테이션으로 검증 실패 시, bindingResult에 오류 내용이 담김
        if (bindingResult.hasErrors()) {
            // 오류 메시지를 가공하여 클라이언트에게 반환
            String errorMessages = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // 유효성 검증 통과 시, 회원가입 로직 수행
        // ... (예: userService.register(userDto)) ...

        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }
}
```

**💡 Tip:** 실무에서는 `@RestControllerAdvice`와 `@ExceptionHandler(MethodArgumentNotValidException.class)`를 사용하여 전역적으로 유효성 검증 예외를 처리하는 것이 더 효율적입니다.
