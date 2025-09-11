package five_minutes.service;


import five_minutes.model.dao.ProjectPerformDao;
import five_minutes.model.dto.ScheduledDto;
import five_minutes.util.ScheduledCatchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


// 스케쥴 쓰는 모든 메소드를 관리하는 서비스
@Service        // service IOC
@RequiredArgsConstructor    // DI 주입
public class ScheduledService { // class start

    // 프로젝트 실행 부분 dao 호출
    private final ProjectPerformDao projectPerformDao;
    // 롤백하고 이메일보낼 서비스 호출
    private final IsRollbackService isRollbackService;

    // 10초마다 알림 값 확인해서 보내는 메소드
    @Scheduled( fixedRate = 1000 * 10 ) // 10초
    public void alertToDoList () {
        System.out.println("스케쥴 메소드 실행됨");
        // 현재 시간 초로 바꿔주는 메소드 호출
        final long nowSec = ScheduledCatchUtil.nowSecond();

        // 가져온다.
        List<ScheduledDto> itemList = projectPerformDao.getScheduledItem();
        // 없으면 그냥 return
        if (itemList == null || itemList.isEmpty()) return;

        // 가져온 리스트 반복문으로 하나씩 조회
        for (ScheduledDto dto : itemList) {
            try {
                // 여기서 null 값 뜨면 Integer 객체에 null 이 뜬건데 일단 놔둠
                Long targetSec = ScheduledCatchUtil.targetSecond(dto);

                if (!ScheduledCatchUtil.isDueOrPast(targetSec, nowSec)) {
                    // 아직 시간이 안 지나거나 null 값 즉 값 자체가 없음. 근데 웬만하면 null이 뜰 수가 없는게
                    // 무조건 시작시간 종료시간은 정해져있음( not null 컬럼조건 ), 즉 db 불러오기 오류일 가능성이 큼
                    continue;   // 그냥 다시 돌림, 넘어가는 것
                }   // if end

                // 롤백 해야할 것 서비스로 보내기
                isRollbackService.transactionMethod(dto);

            } catch (Exception e) {
                System.out.println("스케쥴 처리 중 발생한 예외로 인한 스킵");
                // 실패해도 다음 항목/다음 tick은 계속 진행
            }   // catch end
        } // for end
    }   // func end

}   // class end
