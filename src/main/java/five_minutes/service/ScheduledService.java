package five_minutes.service;


import five_minutes.model.dao.ProjectPerformDao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



///  스케쥴 쓰는 모든 메소드를 관리하는 서비스 ~
@Service        // service IOC
@RequiredArgsConstructor    // DI 주입
public class ScheduledService { // class start

    // 프로젝트 실행 부분 dao 호출~
    private final ProjectPerformDao projectPerformDao;

    // 10초마다 알림 값 확인해서 보내는 메소드
    @Scheduled( fixedRate = 1000 * 10 ) // 10초
    public void alertToDoList ( ) {


    }   // func end

}   // class end
