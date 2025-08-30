package five_minutes.service;


// info ========================
// CommonService를 상속 받아 CRUD 기능을 구현
// getDao 추상 클래스를 정의
// Writer : OngTK

import five_minutes.model.dao.CommonDao;

import java.util.List;

public abstract class AbstractService<T, ID, S> implements CommonService<T, ID, S> {

    // [1] 추상 메소드
    protected abstract CommonDao<T, ID, S> getDao();
    
    // [2] CommonService 구현
    @Override
    // [2.1] 생성 구현
    public int create(T dto) {
        return getDao().create(dto);
    } // func end

    @Override
    // [2.2] 전체 조회 구현
    public List<T> readAll(S s) {
        return getDao().readAll(s);
    } // func end

    @Override
    // [2.3] 개별 조회 구현
    public T read(ID id, S s) {
        return getDao().read(id, s);
    } // func end

    @Override
    // [2.4] 수정 구현
    public int update(T dto) {
        return getDao().update(dto);
    } // func end

    @Override
    // [2.5] 삭제(비활성화) 구현
    public int delete(ID id, S s) {
        return getDao().delete(id, s);
    } // func end

} // func end
