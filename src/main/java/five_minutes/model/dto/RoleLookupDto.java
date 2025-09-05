package five_minutes.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
public class RoleLookupDto {        // class start

    private int pjRoleNo;          // -- [PK] pj 역할 No
    private String pjUserName;     // 조인해서 가져올 역할을 가진 사원 이름
    private String pjRoleName;     // 역할 이름

}   // class end
