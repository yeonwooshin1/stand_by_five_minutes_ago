package five_minutes.util;

import five_minutes.model.dto.DashboardDto;

import java.util.List;

public interface DashboardMapper {
    List<DashboardDto> getPersonalPerformancesSorted(int pjNo, int userNo);
}
