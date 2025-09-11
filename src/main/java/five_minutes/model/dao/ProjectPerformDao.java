package five_minutes.model.dao;

import five_minutes.model.dto.ChkItemLookupDto;
import five_minutes.model.dto.ProjectPerformDto;

import five_minutes.model.dto.RoleLookupDto;
import five_minutes.model.dto.ScheduledDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectPerformDao extends Dao {    // class start

    // pjPerform 가져오기
    public List<ProjectPerformDto> findByProject(int pjNo) {
        // 빈 리스트 생성
        List<ProjectPerformDto> list = new ArrayList<>();
        try {

            String sql = "select perform.pfNo, perform.pjRoleNo, perform.pjChkItemNo, perform.pfStart, perform.pfEnd, perform.notifyType, " +
                    "perform.notifySetMins, perform.pfStatus from pjPerform as perform " +
                    "inner join pjWorker as role on perform.pjRoleNo = role.pjRoleNo inner join pjChecklistItem as chkItem on perform.pjChkItemNo = chkItem.pjChkItemNo " +
                    "where role.pjNo = ? and chkItem.pjNo = ? order by perform.pfNo asc";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setInt(2, pjNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectPerformDto dto = new ProjectPerformDto();
                dto.setPfNo(rs.getInt("pfNo"));
                dto.setPjRoleNo(rs.getInt("pjRoleNo"));
                dto.setPjChkItemNo(rs.getInt("pjChkItemNo"));
                dto.setPfStart(rs.getString("pfStart"));
                dto.setPfEnd(rs.getString("pfEnd"));
                dto.setNotifyType(rs.getInt("notifyType"));
                dto.setNotifySetMins(rs.getInt("notifySetMins"));
                dto.setPfStatus(rs.getInt("pfStatus"));

                list.add(dto);
            }   // while end
        } catch (Exception e) {
            System.out.println("예외 발생");
        }
        return list;
    }   // func end


    // 프로젝트에 맞는 역할 레코드 가져오기
    public List<RoleLookupDto> findRolesLookup(int pjNo) {
        // 빈 리스트 생성
        List<RoleLookupDto> list = new ArrayList<>();

        try {
            String sql = " select pjRoleNo, userName as pjUserName , pjRoleName " +
                    "from pjWorker as pj inner join Users as u on pj.userNo = u.userNo where pjNo = ? and pjRoleStatus = 1 ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RoleLookupDto dto = new RoleLookupDto();
                dto.setPjRoleNo(rs.getInt("pjRoleNo"));
                dto.setPjUserName(rs.getString("pjUserName"));
                dto.setPjRoleName(rs.getString("pjRoleName"));
                list.add(dto);
            }   // while end
        } catch (Exception e) {
            System.out.println("예외 발생");
        }
        return list;
    }   // func end


    // 프로젝트에 맞는 체크리스트 레코드 가져오기
    public List<ChkItemLookupDto> findChkItemsLookup(int pjNo) {
        // 빈 리스트 생성
        List<ChkItemLookupDto> list = new ArrayList<>();

        try {
            String sql = " select pjChkItemNo , pjChklTitle , pjHelpText from PjChecklistItem where pjNo = ? and pjChkIStatus = 1 ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChkItemLookupDto dto = new ChkItemLookupDto();
                dto.setPjChkItemNo(rs.getInt("pjChkItemNo"));
                dto.setPjChklTitle(rs.getString("pjChklTitle"));
                dto.setPjHelpText(rs.getString("pjHelpText"));

                list.add(dto);
            }   // while end
        } catch (Exception e) {
            System.out.println("예외 발생");
        }
        return list;
    }   // func end

    // 새로운 행 추가 dao
    public int insert(ProjectPerformDto d) {
        try {
            String sql = "insert into pjPerform " +
                            "(pjChkItemNo, pjRoleNo, pfStart, pfEnd, notifyType, notifySetMins) " +
                            "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, d.getPjChkItemNo());
            ps.setInt(2, d.getPjRoleNo());
            ps.setString(3, d.getPfStart());
            ps.setString(4, d.getPfEnd());
            ps.setInt(5, d.getNotifyType() == 0 ? 0 : d.getNotifyType());
            ps.setInt(6, d.getNotifySetMins() == 0 ? 0 : d.getNotifySetMins());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // 새 pfNo

        } catch (Exception e){
            System.out.println(e);
        }
        // 실패
        return -1;
    }   // func end

    // 수정된 행 수정
    public int update(ProjectPerformDto d) {
        try {
            String sql = "update pjPerform set pjChkItemNo= ?, pjRoleNo= ?, pfStart= ?, pfEnd= ?, notifyType= ?, notifySetMins=? "
                    + " where pfNo=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, d.getPjChkItemNo());
            ps.setInt(2, d.getPjRoleNo());
            ps.setString(3, d.getPfStart());
            ps.setString(4, d.getPfEnd());
            ps.setInt(5, d.getNotifyType());
            ps.setInt(6, d.getNotifySetMins());
            ps.setInt(7, d.getPfNo());
            return ps.executeUpdate();

        } catch (Exception e){
            System.out.println(e);
        }
        // 실패
        return -1;
    }   // func end

    // 삭제할 것들
    public int deleteByIds(List<Integer> deleteIndex) {
        if (deleteIndex == null || deleteIndex.isEmpty()) return 0;
        // builder 실행
        StringBuilder in = new StringBuilder();

        // list에서 가져온다. 몇 개의 ?를 줄 것인가
        for (int i = 0; i < deleteIndex.size(); i++) {
            if (i > 0) in.append(", ");
            in.append("?");
        }   // for end

        try {
            String sql = "delete from pjPerform where pfNo in (" + in + ")";
            PreparedStatement ps = conn.prepareStatement(sql);
            int idx = 1;
            // 받은 리스트를 하나하나 꺼내서 그 값을 넣어줘서 삭제한다.
            for (Integer id : deleteIndex) ps.setInt(idx++, id);
            return ps.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        }   // catch end
        // 실패 시 -1 반환
        return -1;
    }   // func end


    // 스케쥴 조회
    public List<ScheduledDto> getScheduledItem() {
        List<ScheduledDto> list = new ArrayList<>();
        try {
            // 대문자로 하여 구분 쉽게 함.
            String sql = "SELECT" +
                    // --메일 받는 유저 정보
                    // 이메일과 유저 이름
                    " u.userName As userName , u.email AS userEmail,"+
                    // --메일 주는 회사 정보
                    // 회사명과 회사 담당자 이름, 회사 연락처
                     "b.bnName As bnName, b.managerName	As managerName, b.managerPhone As managerPhone,"+
                    // --프로젝트 정보
                    // 프로젝트 이름 , 도로명주소와 상세주소 , 프로젝트 시작날짜와 종료날짜
                    " pj.pjName AS pjName, pj.roadAddress AS projectRoadAddress," +
                    " pj.detailAddress AS projectDetailAddress, pj.pjstartDate AS pjStartDate," +
                    " pj.pjEndDate  AS pjEndDate," +
                    // --역할
                    // 역할이름과 역할의 상세 설명
                    " pw.pjRoleName AS roleName," +
                    " pw.pjRoleDescription AS roleDescription, pw.pjRoleLv AS roleLevel," +
                    // --할 일들
                    // 체크리스트 사본과 그 사본의 상세정보
                    " pci.pjChklTitle AS todoTitle," +
                    " pci.pjHelpText AS todoHelpText," +
                    // --메일 발송 판단용
                    // 알림 타입과 분으로 나타낸 것, 언제 시작이고 언제 끝나는지, 그리고 그 수행하는 것의 수행상태
                    " pf.notifyType AS notifyType," +
                    " pf.notifySetMins AS notifySetMins," +
                    " pf.pfStart AS pfStart," +
                    " pf.pfEnd AS pfEnd," +
                    " pf.pfStatus AS pfStatus," +
                    " pf.pfNo AS pfNo"+
                    // --어느 테이블에서 가져올 것인가?
                    // 프로젝트 perform 에서
                    " FROM pjPerform AS pf" +
                    // --join 하는 것들
                    // pwWorker , Users, PjChecklistItem, ProjectInfo, BusinessUser
                    " JOIN pjWorker AS pw" +
                    " ON pw.pjRoleNo = pf.pjRoleNo" +
                    " JOIN Users AS u" +
                    " ON u.userNo = pw.userNo" +
                    " JOIN PjChecklistItem AS pci" +
                    " ON pci.pjChkItemNo = pf.pjChkItemNo" +
                    " JOIN ProjectInfo AS pj" +
                    " ON pj.pjNo = pci.pjNo" +
                    " AND pj.pjNo = pw.pjNo"+
                    " JOIN BusinessUser AS b"+
                    " ON b.bnNo = pj.bnNo"+
                    // -- 가져오는 조건
                    // 프로젝트, 역할과 체크리스트, 회사가 활성상태에 알림 컬럼이 1,2,3,4 만 허용
                    " WHERE pj.pjStatus = 1" +
                    " AND pw.pjRoleStatus = 1" +
                    " AND pci.pjChkIStatus = 1" +
                    " AND b.bnStatus = 1" +
                    " AND pf.notifyType IN (1,2,3,4)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ScheduledDto dto = new ScheduledDto();

                dto.setUserName(rs.getString("userName"));
                dto.setUserEmail(rs.getString("userEmail"));

                dto.setBnName(rs.getString("bnName"));
                dto.setManagerName(rs.getString("managerName"));
                dto.setManagerPhone(rs.getString("managerPhone"));

                dto.setPjName(rs.getString("pjName"));
                dto.setProjectRoadAddress(rs.getString("projectRoadAddress"));
                dto.setProjectDetailAddress(rs.getString("projectDetailAddress"));
                dto.setPjStartDate(rs.getString("pjStartDate"));
                dto.setPjEndDate(rs.getString("pjEndDate"));

                dto.setRoleName(rs.getString("roleName"));
                dto.setRoleDescription(rs.getString("roleDescription"));
                dto.setRoleLevel(rs.getInt("roleLevel"));

                dto.setTodoTitle(rs.getString("todoTitle"));
                dto.setTodoHelpText(rs.getString("todoHelpText"));

                dto.setNotifyType(rs.getInt("notifyType"));
                dto.setNotifySetMins(rs.getInt("notifySetMins"));
                dto.setPfStart(rs.getTimestamp("pfStart").toLocalDateTime());
                dto.setPfEnd(rs.getTimestamp("pfEnd").toLocalDateTime());
                dto.setPfStatus(rs.getInt("pfStatus"));
                dto.setPfNo(rs.getInt("pfNo"));

                list.add(dto);
            }   // while end

        } catch (Exception e){
            System.out.println("알림을 보내는데 가져오는 데이터에 예외가 있습니다.");
        }   // try end
        return list;
    }   // func end

    // 스케쥴 알림 보내고 난 후 상태 업데이트
    public boolean updateNotifyType(int pfNo) {
        try {
            String sql = "update pjPerform set notifyType = -1 where pfNo = ? and notifyType in (1,2,3,4)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pfNo);

            return ps.executeUpdate() == 1;

        } catch (Exception e){
            System.out.println("알림 보낸 후 상태가 업데이트 되지 않는 상태입니다.");
        } // try end
        return false;
    }   // func end

    // @Author OngTK
    // 엑셀출력용 메소드
    public List<ProjectPerformDto> readAllforExcel(int pjNo) {
        System.out.println("ProjectPerformDao.readAllforExcel");
        System.out.println("pjNo = " + pjNo);

        List<ProjectPerformDto> list = new ArrayList<>();
        try {
            String sql = "select p.*, w.pjroleName, w.pjNo, u.userName from pjPerform p inner join pjworker w on p.pjroleNo = w.pjroleNo " +
                    " inner join Users u on w.userNo = u.userNo where w.pjNo = ?  order by pfStart ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProjectPerformDto dto = ProjectPerformDto.builder()
                        .pfStart(rs.getString("pfStart"))
                        .pfEnd(rs.getString("pfEnd"))
                        .pfStatus(rs.getInt("pfStatus"))
                        .note(rs.getString("note"))
                        .createDate(rs.getString("createDate"))
                        .updateDate(rs.getString("updateDate"))
                        .pjRoleName(rs.getString("pjRoleName"))
                        .userName(rs.getString("userName")).build();
                list.add(dto);
            }
            return list;
        } catch (Exception e) {
            System.out.println("ProjectPerformDao.readAllforExcel " + e);
        }
        return null;
    } // func end

}   // class end
