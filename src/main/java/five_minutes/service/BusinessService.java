package five_minutes.service;

import five_minutes.model.dao.BusinessDao;
import five_minutes.model.dto.BusinessDto;

import five_minutes.util.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class BusinessService { // class start
    // 비지니스 dao 의존성 주입
    private final BusinessDao businessDao;
    // 파일 서비스 의존성 주입
    private final FileService fileService;

    // 회사 정보 조회 서비스
    public BusinessDto getBusinessInfo(String bnNo) {
        BusinessDto dto = businessDao.getBusinessInfo(bnNo);
        // 이미지 경로 지정해주기 , 유효성 검사도 해줌
        if (dto != null && dto.getBnDocuImg() != null && !dto.getBnDocuImg().isBlank()) {
            dto.setBnDocuImg("/upload/bnDocu/" + dto.getBnDocuImg());
        }else{ dto.setBnDocuImg(null); }   // if end
        // dto 반환
        return dto;
    }   // func end

    // 회사정보수정 서비스
    public int updateBusinessInfo(BusinessDto businessDto  , String bnNo ){

        // 가져온 dto 값들 유효성 검사용 , null 이면 null 값 주기 아니라면 공백제거해서 가져오기
        String managerName = businessDto.getManagerName() == null ? null : businessDto.getManagerName().trim();
        String managerPhone = businessDto.getManagerPhone() == null ? null : businessDto.getManagerPhone().trim();
        MultipartFile uploadBnImg = businessDto.getUploadBnImg() == null ? null : businessDto.getUploadBnImg();
        String bnType = businessDto.getBnType() == null? null : businessDto.getBnType().trim();
        String bnItem = businessDto.getBnItem() == null? null : businessDto.getBnItem().trim();

        // 값이 없으면 -3 반환 => 사업자 정보가 모두 있어야 한다는 전제조건이 깔림. => 단 이미지는 제외
        if ( managerName == null || managerPhone == null  || bnType == null || bnItem == null ||
                managerName.isBlank() || managerPhone.isBlank() || bnType.isBlank() || bnItem.isBlank() ) {
            return -3;
        }   // if end

        // 번호가 null 이 아니면서 형식 검사에서 틀리면 -2 반환 => 형식 오류
        if( !PhoneNumberUtil.isValid(managerPhone) ) return -2;

        // 파일 업데이트 하려고 가져온 이미지가 null 이거나 비어있으면 그냥 나머지 애들만 수정
        if(uploadBnImg == null || uploadBnImg.isEmpty() ){
            // 보낼 dto 새로 만들기 => 생성자에 넣기
            BusinessDto dto = new BusinessDto(bnNo , null ,  managerName , managerPhone , null
                 , null , bnType , bnItem , 0 , null , null , null);

            // dao 호출해서 값 반환 => true 면 1 , 아니면 0 반환
            return businessDao.updateBusinessInfo(dto) ? 1 : 0;
        }   // if end

        // 삭제할 이미지를 dao 에서 가져온다.
        String oldImg = businessDao.getBnDoImg(bnNo);

        // 파일 서비스 호출하여 업로드 하고 파일 이름 반환
        String bnDocuImg = fileService.fileUpload(1, uploadBnImg );

        // 파일 서비스에서 가져온 사진이 null 일 경우 실패 반환
        if(bnDocuImg == null || bnDocuImg.isBlank()){
            return 0;
        }   // if end

        // 보낼 dto 새로 만들기 => 생성자에 넣기
        BusinessDto dto = new BusinessDto(bnNo , null ,  managerName , managerPhone , null , bnDocuImg
                , bnType , bnItem , 0 , null , null , null);

        // dao 호출해서 값 반환
        boolean check = businessDao.updateBusinessInfo(dto);

        // 실패 했으면 0 반환
        if(!check){
            // 업데이트 실패했으니 업로드한 사진 삭제
            fileService.fileDelete(1, bnDocuImg);
            return 0;
        } else {
            // 성공했으면 1 반환.
            return 1;
        }   // if end
    }   // func end

}   // class end