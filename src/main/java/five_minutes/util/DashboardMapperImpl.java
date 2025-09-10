package five_minutes.util;

import five_minutes.model.dto.DashboardDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DashboardMapperImpl implements DashboardMapper {

    private Connection conn;  // 생성자에서 주입받거나 DataSource 통해 가져오기

    public DashboardMapperImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<DashboardDto> getPersonalPerformancesSorted(int pjNo, int userNo) {
        List<DashboardDto> list = new ArrayList<>();
        String sql = "SELECT " +
                "p.pf_no, " +
                "p.pf_start, " +
                "p.pf_end, " +
                "p.pf_status, " +
                "w.pj_role_name, " +
                "c.pj_chkl_title, " +
                "u.user_name " +
                "FROM pj_perform p " +
                "INNER JOIN pj_worker w ON p.pj_role_no = w.pj_role_no " +
                "INNER JOIN users u ON w.user_no = u.user_no " +
                "INNER JOIN pj_checklist_item c ON p.pj_chk_item_no = c.pj_chk_item_no " +
                "WHERE p.pj_no = ? AND u.user_no = ? " +
                "ORDER BY p.pf_start ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pjNo);
            ps.setInt(2, userNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DashboardDto dto = new DashboardDto();

                // dto 내부에 pjPerDto, pjWorkerDto, usersDto, pjCheckDto 객체들이 있을 경우
                dto.getPjPerDto().setPfNo(rs.getInt("pf_no"));
                dto.getPjPerDto().setPfStart(rs.getString("pf_start"));
                dto.getPjPerDto().setPfEnd(rs.getString("pf_end"));
                dto.getPjPerDto().setPfStatus(rs.getInt("pf_status"));

                dto.getPjWorkerDto().setPjRoleName(rs.getString("pj_role_name"));
                dto.getPjCheckDto().setPjChklTitle(rs.getString("pj_chkl_title"));
                dto.getUsersDto().setUserName(rs.getString("user_name"));

                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}