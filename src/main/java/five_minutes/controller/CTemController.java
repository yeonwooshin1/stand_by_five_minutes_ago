package five_minutes.controller;

import five_minutes.model.dto.CTemDto;
import five_minutes.service.CTemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "CheckTem APIs", description = "체크리스트 템플릿 관련 API 명세서")
@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/checktem")
public class CTemController {  // class start
    // CheckTemplate
    // * DI
    private final CTemService cTemService;

    // 로그인체크
    // URL : http://localhost:8080/user/login
    // BODY :   { "email": "mx2ur43n@example.com" , "passwordHash": "NyEUIQeE4N" }

    // [1] 체크리스트 템플릿 생성
    // URL : http://localhost:8080/checktem
    // BODY : { "ctName" : "출퇴근 돌파" , "ctDescription" : "네이버 지도를 활용하도록 합니다."}
    @Operation(summary = "체크리스트 템플릿 생성", description = "체크리스트 템플릿을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "생성 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "4000001"))),
            // HTTP 200 응답이 성공하고 생성에 성공하면, ctNo를 반환한다.
            @ApiResponse(
                    responseCode = "200",
                    description = "생성 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "0"))
            ),
            // HTTP 200 응답이 성공하고 생성에 실패하면, 0을 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "-1"))
            ),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, -1을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("")
    public int createCTem(@RequestBody CTemDto cTemDto, HttpSession session) {

        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중이면 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        cTemDto.setBnNo(bnNo);
        // 3. 리턴
        cTemDto.setStatus("ACCESS_OK");
        return cTemService.createCTem(cTemDto);
    }

    // [2] 체크리스트 템플릿 전체조회
    // URL : http://localhost:8080/checktem
    @Operation(summary = "체크리스트 템플릿 전체조회", description = "체크리스트 템플릿을 전체조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "전체조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CTemDto.class)))),
            // HTTP 200 응답이 성공하면, JSON 형태로 List 구조의 데이터를 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CTemDto.class, nullable = true)))
            ),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, null을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("")
    public List<CTemDto> getCTem(HttpSession session) {
        List<CTemDto> list = new ArrayList<>();

        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            list.add(dto);
            return list; // 비로그인시 status에서 NOT_LOGGED_IN 전송
        }
        // 2. 로그인 중일 때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        list = cTemService.getCTem(bnNo);

        // 3. 리턴
        for (CTemDto dto : list) {
            dto.setStatus("ACCESS_OK");
        }
        return list;
    }

    // [3] 체크리스트 템플릿 개별조회
    // URL : http://localhost:8080/checktem/indi?ctNo=4000001
    @Operation(summary = "체크리스트 템플릿 개별조회", description = "체크리스트 템플릿을 개별조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "개별조회 성공",
                    content = @Content(schema = @Schema(implementation = CTemDto.class))),
            // HTTP 200 응답이 성공하면, JSON 형태로 Dto 구조의 데이터를 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "개별조회 실패",
                    content = @Content(schema = @Schema(implementation = CTemDto.class, nullable = true))),
            // HTTP 200 응답이 성공하고 개별 조회에 실패하면, null을 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(schema = @Schema(implementation = CTemDto.class, nullable = true))),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, null을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/indi")
    public CTemDto getIndiCTem(@RequestParam int ctNo, HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return dto; // 비로그인시 setStatus 전송
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        CTemDto dto = cTemService.getIndiCtem(bnNo, ctNo);
        // 3. DTO에서 사용자에게 입력받은 ctNo를 찾을 수 없을 경우
        if (dto == null) {
            dto = new CTemDto();
            dto.setStatus("NOT_FOUND");
            return dto; // ctNo 없을 시 setStatus 전송
        }
        // 4. 리턴
        dto.setStatus("ACCESS_OK");
        return dto;
    }

    // [4] 체크리스트 템플릿 수정
    // URL : http://localhost:8080/checktem
    // { "ctName" : "출퇴근 돌파" , "ctDescription" : "네이버 지도를 활용하도록 합니다." , "ctNo" : 4000002 }
    @Operation(summary = "체크리스트 템플릿 수정", description = "체크리스트 템플릿을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "4000001"))),
            // HTTP 200 응답이 성공하고 수정에 성공하면, ctNo를 반환한다.
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "0"))),
            // HTTP 200 응답이 성공하고 수정에 실패하면, 0을 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "-1"))),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, -1을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PutMapping("")
    public int updateCTem(@RequestBody CTemDto cTemDto, HttpSession session) {
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        cTemDto.setBnNo(bnNo);
        // 3. 리턴
        cTemDto.setStatus("ACCESS_OK");
        return cTemService.updateCTem(cTemDto);
    }

    // [5] 체크리스트 템플릿 삭제
    // URL : http://localhost:8080/checktem?ctNo=4000001
    @Operation(summary = "체크리스트 템플릿 삭제", description = "체크리스트 템플릿을 휴면 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "4000001"))),
            // HTTP 200 응답이 성공하고 삭제에 성공하면, ctNo를 반환한다.
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "0"))),
            // HTTP 200 응답이 성공하고 개별 조회에 실패하면, 0을 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(schema = @Schema(type = "integer", example = "-1"))),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, -1을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("")
    public int deleteCTem(@RequestParam int ctNo, HttpSession session) {
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1;
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        // 3. DTO에서 사용자에게 입력받은 ctNo를 찾지 못했을 경우
        CTemDto dto = new CTemDto();
        if (dto == null) {
            dto = new CTemDto();
            dto.setStatus("NOT_FOUND");
            return 0;
        }
        // 4. 리턴
        dto.setStatus("ACCESS_OK");
        return cTemService.deleteCTem(bnNo, ctNo);
    }


} // class end

