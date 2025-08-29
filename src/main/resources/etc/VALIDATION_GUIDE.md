# Spring Boot 일괄 유효성 검사(Validation) 가이드

다른 클래스의 메소드를 호출하여 유효성 검사를 일괄적으로 처리하는 것은 매우 좋은 방법입니다. 이를 통해 코드가 중복되는 것을 막고, 검증 로직을 한 곳에서 관리하여 유지보수성을 높일 수 있습니다.

Spring Boot에서는 이 기능을 위해 `Validation` 라이브러리를 사용하는 표준적인 방법을 제공합니다.

## 방법: DTO와 Annotation을 활용한 유효성 검사

### 1. `spring-boot-starter-validation` 의존성 추가

먼저 `build.gradle` 파일에 의존성을 추가합니다. (대부분의 웹 프로젝트에는 이미 포함되어 있습니다.)

```groovy
// build.gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    // ... other dependencies
}
```

### 2. DTO 필드에 유효성 검사 어노테이션 추가

유효성을 검사하고 싶은 DTO(`CTemDto`)의 필드 위에 어떤 규칙을 적용할지 어노테이션으로 지정합니다.

**`CTemDto.java` 수정 예시:**
```java
package five_minutes.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
// ...

@Data
public class CTemDto {
    // ...
    
    @NotBlank(message = "템플릿 이름은 필수입니다.")
    @Size(max = 50, message = "템플릿 이름은 50자를 초과할 수 없습니다.")
    private String ctName;

    @Size(max = 200, message = "템플릿 설명은 200자를 초과할 수 없습니다.")
    private String ctDescription;
    
    // ...
    private String bnNo;
}
```
-   `@NotBlank`: `null`이 아니어야 하며, 공백만 있는 문자열도 허용하지 않습니다.
-   `@NotEmpty`: `null`이 아니어야 하며, 빈 문자열(`""`)도 허용하지 않습니다.
-   `@NotNull`: `null`만 아니면 됩니다.
-   `@Size(min=, max=)`: 문자열의 길이나 컬렉션의 크기를 제한합니다.
-   `@Email`: 이메일 형식인지 검사합니다.
-   `@Pattern(regexp=)`: 정규식으로 형식을 검사합니다.
-   `@Min`, `@Max`: 숫자 값의 최소/최대 값을 제한합니다.

### 3. Controller에서 `@Valid` 어노테이션 사용

Controller 메소드에서 `@RequestBody`로 받는 DTO 앞에 `@Valid` 어노테이션을 붙여줍니다. 이렇게 하면 해당 DTO에 대해 설정된 유효성 검사가 자동으로 실행됩니다.

**`CTemController.java` 수정 예시:**
```java
import jakarta.validation.Valid; // import 추가
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// ...

@RestController
@RequestMapping("/checktem")
public class CTemController {
    // ...

    @PostMapping("")
    public int createCTem(@Valid @RequestBody CTemDto cTemDto, HttpSession session) {
        // ...
        // 만약 유효성 검사에 실패하면, 이 메소드가 실행되기 전에
        // Spring이 자동으로 오류를 발생시키고 400 Bad Request 응답을 보냅니다.
        // ...
        return cTemService.createCTem(cTemDto);
    }
}
```

### 4. (선택) 유효성 검사 실패 시 에러 메시지 커스터마이징

`@Valid` 검증에 실패하면 `MethodArgumentNotValidException`이 발생합니다. 기본적으로는 꽤 복잡한 JSON 오류 메시지를 반환하는데, 이를 보기 좋게 가공하기 위해 `@RestControllerAdvice`와 `@ExceptionHandler`를 사용할 수 있습니다.

```java
// GlobalExceptionHandler.java (새로운 파일)
package five_minutes.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
```
이렇게 하면 클라이언트는 `{"ctName": "템플릿 이름은 필수입니다."}` 와 같이 어떤 필드가 왜 잘못되었는지 명확하게 알 수 있는 응답을 받게 됩니다.

---

이 방법을 사용하면 컨트롤러나 서비스 로직에 `if (cTemDto.getCtName() == null) { ... }` 와 같은 지저분한 검증 코드를 넣을 필요 없이, DTO 자체에 검증 책임을 위임하여 코드를 매우 깔끔하고 일관성 있게 유지할 수 있습니다.