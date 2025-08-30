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



}   // class end
