package five_minutes.service;

import five_minutes.model.dao.ProjectPerformDao;
import five_minutes.model.dto.ScheduledDto;
import five_minutes.util.EmailSendFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IsRollbackService {   // class start

    private final ProjectPerformDao projectPerformDao;
    private final EmailService emailService;

    // NotifyType 업데이트가 안됐을 때 롤백 시키는 헬퍼 메소드
    // 만약 업데이트 됐으면 이메일 발송
    public void transactionMethod (ScheduledDto dto) {
        // 상태 업데이트 됐으면 true 반환 업데이트 안 됐으면 false를 반환하는 로직이 있음.
        // dao 호출
        boolean updated = projectPerformDao.updateNotifyType(dto.getPfNo());

        // 만약 업데이트 안됐으면 런타임 예외를 던져서 트렌젝션 롤백 시킴
        if (!updated) {
            throw new IllegalStateException("[실패] 컬럼 수정 오류로 인한 해당 항목 롤백");
        }   // if end

        // 잘 수정됐으면 메일 보내기
        String subject = EmailSendFormat.subject(dto);
        String bodyHtml = EmailSendFormat.html(dto,"http://localhost:8080/index.jsp");
        emailService.sendHtml(dto.getUserEmail(), subject, bodyHtml);
    }   // func end
}   // class end
