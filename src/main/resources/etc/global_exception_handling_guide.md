# 전역 예외 처리 가이드 (@RestControllerAdvice)

Spring Boot 애플리케이션에서 발생하는 예외를 중앙에서 효율적으로 처리하는 방법을 안내합니다. 이 방법을 사용하면 각 Controller에 중복된 예외 처리 코드를 작성할 필요가 없어지고, 클라이언트에게 일관된 형식의 오류 응답을 보낼 수 있습니다.

---

### 1. `@RestControllerAdvice`란?

`@RestControllerAdvice`는 `@ControllerAdvice`와 `@ResponseBody`를 합친 어노테이션입니다.

- **`@ControllerAdvice`**: 여러 컨트롤러에 걸쳐 공통으로 적용되는 기능을 정의할 때 사용합니다. (AOP 기반)
- **`@ResponseBody`**: 메서드의 반환 값을 HTTP 응답 본문(Body)에 직접 쓰도록 지시합니다.

따라서 `@RestControllerAdvice`를 클래스에 붙이면, 모든 `@RestController`에서 발생하는 예외를 가로채서 처리하고, 그 결과를 JSON과 같은 형태의 응답 본문으로 반환하는 전역 예외 처리기를 만들 수 있습니다.

---

### 2. 사용하는 이유

- **코드 중복 제거**: 컨트롤러마다 `try-catch` 블록이나 `BindingResult`를 처리하는 코드를 작성할 필요가 없습니다.
- **일관된 오류 응답**: 모든 API 오류 응답을 정해진 형식(예: `ErrorResponse` 객체)으로 통일하여 프론트엔드 개발자와의 협업을 원활하게 합니다.
- **관심사 분리(SoC)**: 비즈니스 로직을 처리하는 컨트롤러와 예외를 처리하는 로직을 명확하게 분리할 수 있습니다.

---

### 3. `@ExceptionHandler`를 이용한 예외 처리

`@ExceptionHandler(Exception.class)` 어노테이션을 사용하면 특정 예외가 발생했을 때 호출될 메서드를 지정할 수 있습니다.

#### 3.1. 공통 오류 응답 DTO 정의

일관된 응답을 위해 오류의 상세 정보를 담을 DTO(Data Transfer Object)를 정의합니다.

`ErrorResponse.java`
```java
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    
    // 유효성 검증 실패 시, 어떤 필드에서 오류가 났는지 상세 정보를 담을 수 있음
    private final Map<String, String> validationErrors; 
}
```

#### 3.2. 전역 예외 처리기 구현

`@RestControllerAdvice` 클래스를 만들고, 처리할 예외 종류별로 `@ExceptionHandler` 메서드를 추가합니다.

**가장 흔한 유효성 검증 실패 예외(`MethodArgumentNotValidException`) 처리 예제:**

`GlobalExceptionHandler.java`
```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 어노테이션을 통한 유효성 검증 실패 시 발생하는 예외를 처리합니다.
     * HTTP Status Code: 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // 실패한 필드와 오류 메시지를 Map에 담음
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("입력 값에 대한 유효성 검증에 실패했습니다.")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 직접 정의한 비즈니스 예외를 처리합니다.
     * 예: 중복된 데이터, 존재하지 않는 리소스 등
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getHttpStatus().value())
                .error(ex.getHttpStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * 위에서 처리하지 못한 모든 예외를 처리합니다. (최후의 보루)
     * HTTP Status Code: 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("서버 내부에서 처리되지 않은 예외가 발생했습니다.")
                .path(request.getRequestURI())
                .build();
        
        // 서버 로깅은 필수
        // log.error("Unhandled Exception: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
```

이제 컨트롤러에서는 `@Valid` 어노테이션만 붙이면 되고, 유효성 검증이 실패했을 때의 처리는 `GlobalExceptionHandler`가 모두 담당하게 됩니다.
