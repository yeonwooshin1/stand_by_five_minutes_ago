package five_minutes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정용 Bean(객체) 등록
public class OpenApiConfig {
    // 스웨거 UI에서 Taland API처럼 테스트할 수 있는 "Authorize" 버튼을 생성하는 Class
    @Bean // 스프링 빈 등록
    public OpenAPI openAPI() {
        // 1. 기본 정보 입력(제목, 버전, 설명)
        Info info = new Info()
                .title("Project 'Stand by Five Minutes Ago' API")
                .version("v1.0.0")
                .description("출장 공연 관리 프로그램 제작 프로젝트를 위한 API 명세서");

        // 2. JWT 인증 스키마 설정
        // API 문서 상단의 "Authorize" 버튼에서 사용할 인증 스키마 정의
        String jwtSchemeName = "jwtAuth";
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .name(jwtSchemeName)            // 인증 스키마 이름
                .type(SecurityScheme.Type.HTTP) // HTTP 기반으로 인증하기
                .scheme("bearer")               // Bearer 토큰 방식
                .bearerFormat("JWT");           // 토큰 형식 지정

        // 3. 인증 요구 조건 설정
        // 모든 요청에 Authorization 헤더를 자동 추가하도록 설정
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);        // 2에서 만든 jwtAuth 스키마 사용

        // 4. OpenAPI 객체 생성 후 반환
        return new OpenAPI()
                .info(info) // 정보 설명
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName, jwtSecurityScheme)) // JWT 스키마 등록
                .addSecurityItem(securityRequirement); // 인증 요구 조건 추가
    } // func end

} // class end 
