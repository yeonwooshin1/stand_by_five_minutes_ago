# 심화 유효성 검증(Validation) 가이드

기본적인 Jakarta Bean Validation 기능 외에, 복잡한 시나리오에 대응하기 위한 고급 기능들을 소개합니다.

---

### 1. 유효성 검사 그룹 (Validation Groups)

객체의 상태(예: 생성 시, 수정 시)에 따라 다른 유효성 검증 규칙을 적용하고 싶을 때 사용합니다. `@Validated` 어노테이션과 함께 사용하면 특정 그룹에 속한 제약 조건만 검증할 수 있습니다.

#### 1.1. 그룹 인터페이스 정의

검증 그룹을 식별하기 위한 빈 인터페이스를 생성합니다.

```java
public interface OnCreate {}
public interface OnUpdate {}
```

#### 1.2. DTO에 그룹 적용

각 필드의 제약 조건에 `groups` 속성을 추가하여 어떤 그룹에 속하는지 지정합니다.

```java
import jakarta.validation.constraints.*;

public class UserDto {

    @NotNull(message = "ID는 필수입니다.", groups = OnUpdate.class) // 수정 시에만 NotNull 검증
    private Long id;

    @NotBlank(message = "사용자 이름은 필수입니다.", groups = {OnCreate.class, OnUpdate.class}) // 생성과 수정 모두 검증
    @Size(min = 2, max = 10, groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.", groups = OnCreate.class) // 생성 시에만 검증
    private String password;
}
```

#### 1.3. Controller에서 그룹 지정

`@Validated` 어노테이션을 사용하여 검증할 그룹을 지정합니다. `@Valid`는 그룹 기능이 없습니다.

```java
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // 생성 API: OnCreate 그룹에 속한 제약 조건만 검증
    @PostMapping
    public ResponseEntity<String> createUser(@Validated(OnCreate.class) @RequestBody UserDto user) {
        // 비즈니스 로직
        return ResponseEntity.ok("User created");
    }

    // 수정 API: OnUpdate 그룹에 속한 제약 조건만 검증
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody UserDto user) {
        // 비즈니스 로직
        return ResponseEntity.ok("User updated");
    }
}
```

---

### 2. 커스텀 유효성 검사기 (Custom Validators)

정규식만으로 부족하거나, 데이터베이스 조회 등 복잡한 비즈니스 로직이 필요한 검증을 직접 구현할 때 사용합니다.

#### 2.1. 커스텀 어노테이션 정의

`@Constraint` 어노테이션을 사용하여 검증 로직을 담은 Validator 클래스를 지정합니다.

```java
import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ ElementType.FIELD }) // 필드에 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
@Constraint(validatedBy = UniqueUsernameValidator.class) // 검증 로직을 담은 클래스
public @interface UniqueUsername {
    String message() default "이미 사용 중인 사용자 이름입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

#### 2.2. Validator 구현

`ConstraintValidator` 인터페이스를 구현하여 실제 검증 로직을 작성합니다.

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    // @Autowired
    // private UserRepository userRepository; // DB 접근이 필요하다면 Spring Bean으로 주입받아 사용

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            return true; // null이거나 비어있는 경우는 @NotBlank 등으로 처리하는 것이 좋음
        }
        // 실제로는 DB에서 사용자 이름 중복 체크 로직을 수행
        // return !userRepository.existsByUsername(username);
        
        // 예시 로직
        return !username.equals("existingUser");
    }
}
```

---

### 3. 중첩 객체 유효성 검사 (Cascaded Validation)

DTO 안에 다른 DTO나 객체 리스트가 포함될 때, 내부 객체까지 연쇄적으로 검증합니다. 필드에 `@Valid` 어노테이션을 붙여주면 됩니다.

```java
public class OrderRequest {
    @NotNull
    private Long orderId;

    @Valid // billingAddress 필드의 AddressDto 내부까지 검증
    @NotNull
    private AddressDto billingAddress;

    @Valid // items 리스트의 각 요소(ItemDto)를 모두 검증
    @NotEmpty
    private List<ItemDto> items;
}

public class AddressDto {
    @NotBlank
    private String street;
    @NotBlank
    private String zipCode;
}

public class ItemDto {
    @NotNull
    private Long itemId;
    @Positive
    private int quantity;
}
```

---

### 4. Hibernate Validator 전용 어노테이션

Jakarta 표준 외에 Hibernate Validator 구현체가 제공하는 유용한 추가 어노테이션들입니다.

- **`@URL`**: 값이 유효한 URL 형식인지 검증합니다. (프로토콜, 호스트, 포트 등 지정 가능)
- **`@Length(min, max)`**: 문자열의 길이를 검증합니다. (`@Size`와 유사하지만 `CharSequence` 타입에만 적용)
- **`@Range(min, max)`**: 숫자 또는 숫자 형태의 문자열 값의 범위(최소, 최대 포함)를 검증합니다.
- **`@CreditCardNumber`**: Luhn 알고리즘을 통해 신용카드 번호 형식인지 검증합니다.
- **`@Email`**: `@`의 존재 여부만 확인하는 표준 `@Email`보다 더 정교한 정규식을 사용하여 이메일 형식을 검증합니다.
