package five_minutes.controller;

import five_minutes.model.dao.CTItemDao;
import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import five_minutes.service.CTItemService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "CheckTemItem APIs", description = "상세 체크리스트 템플릿 관련 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/checkitem")
public class CTItemController {
    // CheckTemplateItem
    // * DI
    private final CTItemService ctItemService;

    // 로그인체크
    // URL : http://localhost:8080/user/login
    // BODY :   { "email": "mx2ur43n@example.com" , "passwordHash": "NyEUIQeE4N" }

    // [1] 상세 체크리스트 템플릿 생성
    // URL : http://localhost:8080/checkitem
    // BODY:
    /*
    {
      	"ctNo" : 4000001 ,
     	"ctiTitle" : "출퇴근 세부업무 1" ,
     	"ctiHelpText" : "특이사항 발생 시 반드시 담당자에게 연락 할 수 있도록 합니다."
    }
    */
    @Operation(summary = "상세 체크리스트 템플릿 생성", description = "상세 체크리스트 템플릿을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "생성 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "5000001"))),
            // HTTP 200 응답이 성공하고 생성에 성공하면, ctiNo를 반환한다.
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
    public int createCTItem(@RequestBody CTItemDto ctItemDto, HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중이면 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        ctItemService.searchCtNo(ctItemDto.getCtNo(), bnNo);
        // 3. 리턴
        ctItemDto.setStatus("ACCESS_OK");
        return ctItemService.createCTItem(ctItemDto);
    }

    // [2] 상세 체크리스트 템플릿 전체조회
    // URL : http://localhost:8080/checkitem?ctNo=4000001
    @Operation(summary = "상세 체크리스트 템플릿 전체조회", description = "상세 체크리스트 템플릿을 전체조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "전체조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CTItemDto.class)))),
            // HTTP 200 응답이 성공하면, JSON 형태로 List 구조의 데이터를 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CTItemDto.class, nullable = true)))
            ),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, null을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("")
    public List<CTItemDto> getCTItem(@RequestParam int ctNo, HttpSession session) {
        List<CTItemDto> list;
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            list = new ArrayList<>();
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            list.add(dto);
            return list;    // 비로그인시 status에서 NOT_LOGGED_IN 전송
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        ctItemService.searchCtNo(ctNo, bnNo);
        // 3. 사용자에게 입력받은 ctNo를 찾을 수 없을 경우
        if (ctNo < 4000000) {
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_FOUND");
        }
        // 4. 리턴
        list = ctItemService.getCTItem(ctNo);
        for (CTItemDto dto : list) {
            dto.setStatus("ACCESS_OK");
            dto.setBnNo(bnNo); // 세션에서 가져온 bnNo를 직접 넣기
        }
        return list;
    }

    // [3] 상세 체크리스트 템플릿 개별조회
    // URL : http://localhost:8080/checkitem/indi?ctNo=4000001&ctiNo=5000001 (ctiStatus 0이라서 조회 안 됨)
    // URL : http://localhost:8080/checkitem/indi?ctNo=4000002&ctiNo=5000003
    @Operation(summary = "상세 체크리스트 템플릿 개별조회", description = "상세 체크리스트 템플릿을 개별조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "개별조회 성공",
                    content = @Content(schema = @Schema(implementation = CTItemDto.class))),
            // HTTP 200 응답이 성공하면, JSON 형태로 Dto 구조의 데이터를 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "개별조회 실패",
                    content = @Content(schema = @Schema(implementation = CTItemDto.class, nullable = true))),
            // HTTP 200 응답이 성공하고 개별 조회에 실패하면, 0을 반환한다
            @ApiResponse(
                    responseCode = "200",
                    description = "세션 실패",
                    content = @Content(schema = @Schema(implementation = CTItemDto.class, nullable = true))),
            // HTTP 200 응답이 성공하고 로그인 세션에 실패하면, -1을 반환한다
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/indi")
    public CTItemDto getIndiCTItem(@RequestParam int ctNo, @RequestParam int ctiNo, HttpSession session) {
        CTItemDto dto = new CTItemDto();
        System.out.println(dto);
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return dto;
        }
        // 2. (로그인 성공 시 )세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        ctItemService.searchCtNo(ctNo, bnNo);
        // 3. DTO에서 ctiNo를 찾을 수 없을 경우
        if (ctiNo < 5000000) {
            dto = new CTItemDto();
            dto.setStatus("NOT_FOUND");
        }
        // 4. 리턴
        dto = ctItemService.getIndiCTItem(ctNo, ctiNo);
        dto.setStatus("ACCESS_OK");
        dto.setBnNo(bnNo); // 세션에서 가져온 bnNo 넣기
        return dto;
    }

    // [4] 상세 체크리스트 템플릿 수정
    // URL : http://localhost:8080/checkitem
    // BODY : { "ctiTitle" : "현장 도착" , "ctiHelpText" : "현장 도착 시 클라이언트 담당자에게 연락 후, 현장 사진을 5장 촬영하여 공연5분전에 업로드합니다." , "ctiNo" : 5000001}
    @Operation(summary = "상세 체크리스트 템플릿 수정", description = "상세 체크리스트 템플릿을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "5000001"))),
            // HTTP 200 응답이 성공하고 수정에 성공하면, ctiNo를 반환한다.
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
    public int updateCTItem(@RequestBody CTItemDto ctItemDto, HttpSession session) {
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            ctItemDto.setStatus("NOT_LOGGED_IN");
            return -1;
        }
        // 2. 로그인 중일때 세션에서 사업자 번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        ctItemService.searchCtNo(ctItemDto.getCtNo(), bnNo);
        // 3. DTO에서 ctiNo를 찾을 수 없는 경우
        if (ctItemDto.getCtiNo() < 5000000) {
            ctItemDto.setStatus("NOT_FOUND");
        }
        // 4. 리턴
        ctItemDto.setStatus("ACCESS_OK");
        ctItemDto.setBnNo(bnNo);
        return ctItemService.updateCTItem(ctItemDto);
    }

    // [5] 상세 체크리스트 템플릿 삭제
    // URL : http://localhost:8080/checkitem?ctiNo=5000001
    @Operation(summary = "상세 체크리스트 템플릿 삭제", description = "체크리스트 템플릿을 휴면 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(type = "integer", example = "5000001"))),
            // HTTP 200 응답이 성공하고 삭제에 성공하면, ctiNo를 반환한다.
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
    public int deleteCTItem(@RequestParam int ctiNo, HttpSession session) {
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1;
        }
        // 2. 로그인 중일 때 세션에서 사업자 번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        CTItemDto dto = new CTItemDto();
        ctItemService.searchCtNo(dto.getCtNo() , bnNo);
        // 3. DTO에서 사용자의 ctiNo를 찾지 못했을 경우
        if (ctiNo < 5000000){
            dto.setStatus("NOT_FOUND");
        }
        // 4. 리턴
        dto.setStatus("ACCESS_OK");
        dto.setBnNo(bnNo);
        return ctItemService.deleteCTItem(ctiNo);
    }

} // class end
