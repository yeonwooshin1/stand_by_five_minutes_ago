package five_minutes.model.dao;

import five_minutes.model.dto.*;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// **Info** =========================
///
/// project dashboard Dao
///
/// 프로젝트별 업무를 상세히 확인할 수 있는 대시보드를 구현합니다.
///
/// Google sheet > 21.테이블 > 1-1, 3 , 4
///
/// @author dongjin

@Repository
public class DashboardDao extends Dao {

    // performInfo 안의 pfNo 가져오기
    // Long pfNo = response.getPerformInfo().getPfNo();
    // performInfo 안의 시작시간 가져오기
    // LocalTime startTime = response.getPerformInfo().getStartTime();

    // [*] 사용자 확인
    public boolean checkUser(int userNo) {
        try {
            String sql = "select count(*) from users where userNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return false;
    }

    // [*] 관리자 확인
    public boolean checkBusiness(String bnNo) {
        try {
            String sql = "select count(*) from projectInfo where bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    } // func end

    // [*] pjNo 확인
    public boolean checkPjNo(int pjNo) {
        try {
            String sql = "SELECT COUNT(*) FROM ProjectInfo WHERE pjNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [1] 프로젝트 대시보드  - 기본정보 조회
    /*
        * 로직 안내
        1. bnNo를 확인한다(로그인 세션)
        2. pjNo를 확인한다(쿼리스트링)
        3. pjNo가 일치하는 ProjectInfo 레코드를 가져온다
     */
    public DashboardDto getInfoPJDash(int pjNo) {
        try {
            String sql = "select * from ProjectInfo where pjNo = ? and pjStatus != 0 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PjDto pjDto = new PjDto();
                pjDto.setPjNo(rs.getInt("pjNo"));
                pjDto.setPjName(rs.getString("pjName"));
                pjDto.setPjMemo(rs.getString("pjMemo"));
                pjDto.setPjStartDate(rs.getString("pjStartDate"));
                pjDto.setPjEndDate(rs.getString("pjEndDate"));
                pjDto.setRoadAddress(rs.getString("roadAddress"));
                pjDto.setDetailAddress(rs.getString("detailAddress"));
                pjDto.setClientName(rs.getString("clientName"));
                pjDto.setClientRepresent(rs.getString("clientRepresent"));
                pjDto.setClientPhone(rs.getString("clientPhone"));
                pjDto.setClientMemo(rs.getString("clientMemo"));
                pjDto.setPjStatus(rs.getString("pjStatus"));
                pjDto.setCreateDate(rs.getString("createDate"));
                pjDto.setUpdateDate(rs.getString("updateDate"));
                pjDto.setBnNo(rs.getString("bnNo"));
                // pjDto에서 불러온 데이터를 DashboardDto로 반환
                DashboardDto dto = new DashboardDto();
                dto.setPjDto(pjDto);
                return dto;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return null;
    } // func end

    // [2-1] 프로젝트 대시보드  - 근무리스트 전체 조회
    /*
        pjNo가 일치하는 pjPerform 테이블의 모든 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * (관리자/근무자 체크) loginUserNo를 확인하여, null이 아닐 경우 모든 레코드 조회. null일 경우 해당 userNo를 pjRoleNo로 inner  join할 수 있는 레코드만 조회.
     */
    public List<DashboardDto> getListPJDash(int pjNo) {
        List<DashboardDto> list = new ArrayList<>();
        try {
            String sql = "SELECT \n u.userNo," +
                    "    pp.pfNo, \n" +
                    "    u.userName,\n" +
                    "    pw.pjRoleName, \n" +
                    "    pci.pjChklTitle, \n" +
                    "    pp.pfStart, \n" +
                    "    pp.pfEnd, \n" +
                    "    pp.notifyType, \n" +
                    "    pp.notifySetMins, \n" +
                    "    pp.pfStatus,\n" +
                    "    pp.note\n" +
                    "FROM pjPerform pp\n" +
                    "INNER JOIN pjWorker pw \n" +
                    "    ON pp.pjRoleNo = pw.pjRoleNo\n" +
                    "    inner join users u\n" +
                    "    on pw.userno = u.userno\n" +
                    "    inner join PjChecklistItem pci\n" +
                    "    on pp.pjChkItemNo = pci.pjChkItemNo\n" +
                    "WHERE pw.pjNo = ? ;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DashboardDto dto = new DashboardDto();
                dto.getPjPerDto().setPfNo(rs.getInt("pfNo"));
                dto.getUsersDto().setUserName(rs.getString("userName"));
                dto.getPjWorkerDto().setPjRoleName(rs.getString("pjRoleName"));
                dto.getPjCheckDto().setPjChklTitle(rs.getString("pjChklTitle"));
                dto.getPjPerDto().setPfStart(rs.getString("pfStart"));
                dto.getPjPerDto().setPfEnd(rs.getString("pfEnd"));
                dto.getPjPerDto().setNotifyType(rs.getInt("notifyType"));
                dto.getPjPerDto().setNotifySetMins(rs.getInt("notifySetMins"));
                dto.getPjPerDto().setPfStatus(rs.getInt("pfStatus"));
                dto.getPjPerDto().setNote(rs.getString("note"));
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return list;
    }

    // [2-2] 프로젝트 대시보드  - 근무리스트 전체 조회 (사용자)
    // 내 이름(사용자명)이 들어간 근무리스트만 전체 조회한다.

    public List<DashboardDto> getListPJDashForUser(int pjNo, int userNo) {
        List<DashboardDto> list = new ArrayList<>();
        try {
            String sql = "SELECT \n u.userNo," +
                    "    pp.pfNo, \n" +
                    "    u.userName,\n" +
                    "    pw.pjRoleName, \n" +
                    "    pci.pjChklTitle, \n" +
                    "    pp.pfStart, \n" +
                    "    pp.pfEnd, \n" +
                    "    pp.notifyType, \n" +
                    "    pp.notifySetMins, \n" +
                    "    pp.pfStatus,\n" +
                    "    pp.note\n" +
                    "FROM pjPerform pp\n" +
                    "INNER JOIN pjWorker pw \n" +
                    "    ON pp.pjRoleNo = pw.pjRoleNo\n" +
                    "    inner join users u\n" +
                    "    on pw.userno = u.userno\n" +
                    "    inner join PjChecklistItem pci\n" +
                    "    on pp.pjChkItemNo = pci.pjChkItemNo\n" +
                    "WHERE pw.pjNo = ? and u.userNo = ? ;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setInt(2, userNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DashboardDto dto = new DashboardDto();
                dto.getPjPerDto().setPfNo(rs.getInt("pfNo"));
                dto.getUsersDto().setUserName(rs.getString("userName"));
                dto.getPjWorkerDto().setPjRoleName(rs.getString("pjRoleName"));
                dto.getPjCheckDto().setPjChklTitle(rs.getString("pjChklTitle"));
                dto.getPjPerDto().setPfStart(rs.getString("pfStart"));
                dto.getPjPerDto().setPfEnd(rs.getString("pfEnd"));
                dto.getPjPerDto().setNotifyType(rs.getInt("notifyType"));
                dto.getPjPerDto().setNotifySetMins(rs.getInt("notifySetMins"));
                dto.getPjPerDto().setPfStatus(rs.getInt("pfStatus"));
                dto.getPjPerDto().setNote(rs.getString("note"));
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return list;
    }

    // [3-1] 프로젝트 대시보드  - 근무리스트 개별 조회
    /*
        pjNo와 pfNo를 확인하여 pjPerform 테이블에서 일치하는 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * 이동시간 측정 메소드 서비스단에 추가.
        사용자와 프로젝트의 위치 정보 기반으로 대중교통, 도보, 차량 이용 시 소요 시간과 출발 시간을 출력해주고, 시간 데이터로 크롬 푸시 해주기
     */
    public DashboardDto getIndiListPJDash(int pjNo, int pfNo) {
        try {
            String sql = " SELECT \n u.userNo," +
                    "    pp.pfNo, \n" +
                    "    u.userName,\n" +
                    "    pw.pjRoleName, \n" +
                    "    pci.pjChklTitle, \n" +
                    "    pp.pfStart, \n" +
                    "    pp.pfEnd, \n" +
                    "    pp.notifyType, \n" +
                    "    pp.notifySetMins, \n" +
                    "    pp.pfStatus,\n" +
                    "    pp.note\n" +
                    "FROM pjPerform pp\n" +
                    "INNER JOIN pjWorker pw \n" +
                    "    ON pp.pjRoleNo = pw.pjRoleNo\n" +
                    "    inner join users u\n" +
                    "    on pw.userno = u.userno\n" +
                    "    inner join PjChecklistItem pci\n" +
                    "    on pp.pjChkItemNo = pci.pjChkItemNo\n" +
                    "WHERE pw.pjNo = ? and pp.pfNo = ?; ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setInt(2, pfNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DashboardDto dto = new DashboardDto();
                dto.getPjPerDto().setPfNo(rs.getInt("pfNo"));
                dto.getUsersDto().setUserName(rs.getString("userName"));
                dto.getPjWorkerDto().setPjRoleName(rs.getString("pjRoleName"));
                dto.getPjCheckDto().setPjChklTitle(rs.getString("pjChklTitle"));
                dto.getPjPerDto().setPfStart(rs.getString("pfStart"));
                dto.getPjPerDto().setPfEnd(rs.getString("pfEnd"));
                dto.getPjPerDto().setNotifyType(rs.getInt("notifyType"));
                dto.getPjPerDto().setNotifySetMins(rs.getInt("notifySetMins"));
                dto.getPjPerDto().setPfStatus(rs.getInt("pfStatus"));
                dto.getPjPerDto().setNote(rs.getString("note"));
                return dto;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // [3-2] 프로젝트 대시보드  - 근무리스트 개별 조회 (사용자)
    public DashboardDto getIndiListPJDashForUser(int pjNo, int pfNo, int userNo) {
        try {
            String sql = " SELECT \n u.userNo," +
                    "    pp.pfNo, \n" +
                    "    u.userName,\n" +
                    "    pw.pjRoleName, \n" +
                    "    pci.pjChklTitle, \n" +
                    "    pp.pfStart, \n" +
                    "    pp.pfEnd, \n" +
                    "    pp.notifyType, \n" +
                    "    pp.notifySetMins, \n" +
                    "    pp.pfStatus,\n" +
                    "    pp.note\n" +
                    "FROM pjPerform pp\n" +
                    "INNER JOIN pjWorker pw \n" +
                    "    ON pp.pjRoleNo = pw.pjRoleNo\n" +
                    "    inner join users u\n" +
                    "    on pw.userno = u.userno\n" +
                    "    inner join PjChecklistItem pci\n" +
                    "    on pp.pjChkItemNo = pci.pjChkItemNo\n" +
                    "WHERE pw.pjNo = ? and pp.pfNo = ? and u.userNo = ?; ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setInt(2, pfNo);
            ps.setInt(3, userNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DashboardDto dto = new DashboardDto();
                dto.getPjPerDto().setPfNo(rs.getInt("pfNo"));
                dto.getUsersDto().setUserName(rs.getString("userName"));
                dto.getPjWorkerDto().setPjRoleName(rs.getString("pjRoleName"));
                dto.getPjCheckDto().setPjChklTitle(rs.getString("pjChklTitle"));
                dto.getPjPerDto().setPfStart(rs.getString("pfStart"));
                dto.getPjPerDto().setPfEnd(rs.getString("pfEnd"));
                dto.getPjPerDto().setNotifyType(rs.getInt("notifyType"));
                dto.getPjPerDto().setNotifySetMins(rs.getInt("notifySetMins"));
                dto.getPjPerDto().setPfStatus(rs.getInt("pfStatus"));
                dto.getPjPerDto().setNote(rs.getString("note"));
                return dto;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // [4] 프로젝트 대시보드 - 파일 업로드
    /*
        pfNo, fileName을 입력받아 pjPerformFile 테이블에 저장한다.
        * pjNo, bnNo를 확인하기
     */
    public int uploadFilePJDash(DashboardDto dashboardDto) {
        ProjectPerformFileDto dto = dashboardDto.getPjFileDto(); // 대시보드 디토에서 불러오기
        try {
            String sql = "insert into pjPerformFile (pfNo, fileName) value ( ? , ? ) ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getPfNo());
            ps.setString(2, dto.getFileName());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    // [5] 프로젝트 대시보드 - 업로드 파일 삭제
    /*
        fileNo를 입력받아 일치하는 pjPerformFile 테이블 레코드를 삭제한다.
        * pjNo, bnNo를 확인하기
     */
    public int deleteFilePJDash(int fileNo) {
        try {
            String sql = "delete from pjPerformFile where fileNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, fileNo);
            if (ps.executeUpdate() == 1) {
                ps.close();
                return fileNo;
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return 0;
    }

    // [6-1] 근무 정보 수정을 위한 관계 확인 메소드
//    public boolean checkUpdate(int pfNo, int userNo) {
//        try {
//            String sql = "      SELECT COUNT(*)\n" +
//                    "      FROM pjPerform p\n" +
//                    "      INNER JOIN pjWorker w ON p.pjRoleNo = w.pjRoleNo\n" +
//                    "      WHERE p.pfNo = ? AND w.userNo = ?; ";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, pfNo);
//            ps.setInt(2, userNo);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                boolean result = rs.getInt(1) > 0;
//                System.out.println("checkUpdate - pfNo: " + pfNo + ", userNo: " + userNo + ", result: " + result);
//                return result;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return false;
//    }


    // [6-2] 프로젝트 대시보드 - 근무 정보 메모 수정
    /*
        pjPerform 테이블의 pfNo를 확인 후 pfStatus, note를 입력받아 수정한다.
        * pjNo, bnNo를 확인하기
     */
    public int updateNotePJDash(DashboardDto dashboardDto) {
        ProjectPerformDto dto = dashboardDto.getPjPerDto();
        try {
            String sql = "update pjPerform set pfStatus = ? , note = ? where pfNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getPfStatus());
            ps.setString(2, dto.getNote());
            ps.setInt(3, dto.getPfNo());
            int count = ps.executeUpdate();
            System.out.println("updateNotePJDash - pfNo: " + dto.getPfNo() +
                    ", pfStatus: " + dto.getPfStatus() +
                    ", note: " + dto.getNote() +
                    ", affected rows: " + count);
            return count;
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return 0;
    } // func end

    // [7] 프로젝트 대시보드 - 근무자 상세 정보
    /*
        1. pjPerform의 pjRoleNo를 확인하여, pjWorker 테이블에서 일치하는 레코드를 확인한다.
        2. 일치하는 레코드의 userNo를 확인하여, Users 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 근무자 텍스트를 클릭할 시 화면에 띄우기
     */
    public UsersDto getUserInDash(int pjNo, int userNo) {
        return null;
    }

    // [8] 프로젝트 대시보드 - 역할명 상세 정보
    /*
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 역할명 텍스트를 클릭할 시 화면에 띄우기
     */
    public ProjectWorkerDto getWorkerInDash(int pjNo, int pjRoleNo) {
        return null;
    }

    // [9] 프로젝트 대시보드 - 체크리스트 상세 정보
    /*
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 체크리스트명 텍스트를 클릭할 시 화면에 띄우기
     */
    public PjCheckDto getCheckInDash(int pjNo, int pjChkItemNo) {
        return null;
    }


} // class end
