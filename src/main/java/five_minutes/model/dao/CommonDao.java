package five_minutes.model.dao;

import java.util.List;

// info ========================
// Dao class 들의 공통기능인 CRUD 인터페이스
// Writer : OngTK

public interface CommonDao<T, ID> {
    // 제네릭
    // T dto : 어떤 Dto가 들어올지 모르니 제네릭을 사용
    // ID id : 일반적으로 PK 값을 받아서 사용

    int create(T dto);      // 생성 func
    T read(ID id);          // 개별 조회 func
    List<T> readAll();      // 전체 조회 func
    int update(T dto);      // 수정 func
    int delete(ID id);      // 삭제(비활성화) func

} // class end
