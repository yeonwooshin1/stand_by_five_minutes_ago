package five_minutes.model.dao;

import five_minutes.model.dto.BusinessDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository     // Dao 어노테이션
public class BusinessDao extends Dao { // class start

    // 회사정보 조회
    public BusinessDto getBusinessInfo( String bnNo ){
        try{
            // 사업자등록번호,  기업명 , 담당자명 , 담당자번호 , 사업자이미지 , 업태 , 종목 , 기업등록일 ,  기업정보수정일 가져옴.
            String sql = "select bnName, managerName, managerPhone, bnDocuImg, bnType, bnItem , createDate from BusinessUser where bnNo = ?";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setString( 1 , bnNo );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                BusinessDto businessDto  = new BusinessDto();
                // 넣어줌
                businessDto.setBnNo( bnNo );
                businessDto.setBnName( rs.getString( "bnName" ) );
                businessDto.setManagerName( rs.getString( "managerName" ) );
                businessDto.setManagerPhone( rs.getString( "managerPhone" ) );
                businessDto.setBnDocuImg( rs.getString( "bnDocuImg" ) );
                businessDto.setBnType( rs.getString( "bnType" ) );
                businessDto.setBnItem( rs.getString( "bnItem" ) );
                businessDto.setCreateDate( rs.getString( "createDate" ) );

                // dto 반환
                return businessDto;
            }   // if end
        } catch (Exception e) {   System.out.println("예외 발생");   }
        // 없으면 null 반환
        return null;
    }   // func end
    // 회사정보수정 - 정보 업데이트하기
    public boolean updateBusinessInfo(BusinessDto businessDto){
        try{
            // 담당자명 , 담당자번호 , 사업자이미지 , 업태 , 종목 변경한다. 이미지 값이 null 일 경우에는 기존 값을 유지한다(COALESCE)
            String sql ="update BusinessUser set managerName = ?, managerPhone = ? , bnDocuImg = COALESCE(?, bnDocuImg) , bnType = ?" +
                    " ,bnItem = ? where bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement( sql );
            // 넣어줌
            ps.setString( 1 , businessDto.getManagerName() );
            ps.setString( 2 , businessDto.getManagerPhone());
            ps.setString( 3 , businessDto.getBnDocuImg());
            ps.setString( 4 , businessDto.getBnType());
            ps.setString( 5 , businessDto.getBnItem());

            ps.setString( 6, businessDto.getBnNo() );

            int count = ps.executeUpdate();
            // 업데이트 된 행이 1개면 성공
            return count == 1;
        } catch (Exception e) { System.out.println(e);  }
        // 아니라면 실패
        return false;
    }   // func end
    // 회사정보수정 - 사업자 이미지 이름 가져오기
    public String getBnDoImg(String bnNo){
        try{
            // 사업자 이미지 가져옴
            String sql = "select bnDocuImg from BusinessUser where bnNo = ?";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setString( 1 , bnNo );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                // 이미지 반환
                return rs.getString("bnDocuImg");
            }   // if end
        } catch (Exception e) {   System.out.println("예외 발생");   }
        // 없으면 null 반환
        return null;
    }   // func end


}   // class end
