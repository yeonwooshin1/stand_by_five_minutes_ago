// 이메일 찾기 js 콘솔
console.log( 'findEmail.js open!');

// 이메일 찾기  
const findEmail = async() =>{
    
    // 마크업을 DOM객체로 가져온다.
    const userNameInput = document.querySelector('#userName'); // 지정한 선택자의 마크업을 DOM객체로 가져오기 
    const userPhoneInput =  document.querySelector('#userPhone');
    const getDataBox = document.querySelector('#getDataBox')

    // 가져온 마크업의 입력받은 값 가져오기
    
    const userName = userNameInput.value;   
    const userPhone = userPhoneInput.value;

    // 프론트 유효성 검사
        if(userName == null || userName.trim() == "" || userPhone == null || userPhone.trim() == ""){
            getDataBox.innerHTML = `<span style="color:red;">모든 항목을 유효한 값을 입력해주세요.</span>`;
            return;
        }   // if end

    // 전화번호 검증
    if(!phoneLike(userPhone)) {
        getDataBox.innerHTML = `<span style="color:red;">전화번호 형식이 잘못 되었습니다.</span>`;
        return;
    }   // if end

    try{
        // 1. fetch 실행
        const option = { method : "GET"}
        const response = await fetch( `/user/recoverEmail?userName=${userName}&userPhone=${userPhone}` , option );
        const data = await response.json();
        let success = data.checkSuccess
        if(success){
            getDataBox.innerHTML = `<span style="color:green"> 회원님의 이메일은 <span style="color:gray"><strong> ${data.getEmail}  </strong></span> 입니다. </span><br/>`
            + `<a href = "/user/login.jsp"><strong> 로그인하러 가기</strong></a> `
        } else {
            getDataBox.innerHTML = `<span style="color:red;">입력한 정보가 일치하지 않습니다.</span>`;
        }
        }catch{

        }
} // func end

// 전화번호 간단 형식 체크(하이픈 포함).
// - 02-123-4567 / 010-1234-5678 등 지원
// - 숫자 2~3자리 - 숫자 3~4자리 - 숫자 4자리
const phoneLike = (s) => /^\d{3}-\d{4}-\d{4}$/.test(String(s).trim());