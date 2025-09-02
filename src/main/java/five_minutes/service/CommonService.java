package five_minutes.service;

import java.util.List;

/// **info** ========================
///
/// Service class 들의 공통기능인 CRUD 인터페이스
///
/// @author OngTK

public interface CommonService<T, ID, S> {
    // 제네릭
    // T dto : 어떤 Dto가 들어올지 모르니 제네릭을 사용
    // ID id : 일반적으로 PK 값을 받기위한 int 타입에 사용할 제네릭
    // S s : String 으로 된 BnNo를 받기위한 제네릭

    int create(T dto);      // 생성 func
    T read(ID id, S s);          // 개별 조회 func
    List<T> readAll(S s);      // 전체 조회 func
    int update(T dto);      // 수정 func
    int delete(ID id, S s);      // 삭제(비활성화) func

} //func end
