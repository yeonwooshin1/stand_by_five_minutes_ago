package five_minutes.model.Repository;

import java.util.List;

/// info ========================
///
/// Dao/service class 들의 공통기능인 CRUD 인터페이스
///
/// @author OngTK

public interface CommonRepository<T, ID, S> {
    // 제네릭
    // T dto    : Dto를 담을 제네릭
    // ID id    : PK 값을 받을 제넥릭 / 일반적으로 PK-Integer을 가장 많이 사용할 예정
    // S s      : bnNo를 매개변수로 하여야 하나 Dto를 변수로 가지지 않을 경우를 대비한, 주로 String 타입의 BnNo를 담을 제네릭

    int create(T dto);          // 생성 func
    T read(ID id, S s);          // 개별 조회 func
    List<T> readAll(S s);      // 전체 조회 func
    int update(T dto);          // 수정 func
    int delete(ID id, S s);      // 삭제(비활성화) func

} // class end
