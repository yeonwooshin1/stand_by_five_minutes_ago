package five_minutes.model.dao;

import five_minutes.model.dto.RtiDto;
import org.springframework.stereotype.Repository;

import java.util.List;

// Info =========================
// RoleTemplateItem Dao
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// 작성자 : OngTK

@Repository     // Dao 어노테이션
public class RtiDao extends Dao implements CommonDao<RtiDto, Integer, String>{

    // [ RTI-01 ] 상세 역할 템플릿 생성 create()
    @Override
    public int create(RtiDto dto) {
        return 0;
    } // [ RTI-01 ] func end

    // [ RTI-02 ] 상세 역할 템플릿 개별 조회
    @Override
    public List<RtiDto> readAll(String s) {
        return List.of();
    } // [ RTI-02 ] func end

    // [ RTI-03 ] 상세 역할 템플릿 전체 조회
    @Override
    public RtiDto read(Integer i, String s) {
        return null;
    } // [ RTI-03 ] func end

    // [ RTI-04 ] 상세 역할 템플릿 수정
    @Override
    public int update(RtiDto dto) {
        return 0;
    } // [ RTI-04 ] func end

    // [ RTI-05 ] 상세 역할 템플릿 삭제(비활성화)
    @Override
    public int delete(Integer i, String s) {
        return 0;
    } // [ RTI-05 ] func end
}   // class end
