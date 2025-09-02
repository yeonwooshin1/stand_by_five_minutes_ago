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

    try{
        // 1. fetch 실행
        const option = { method : "GET"}
        const response = await fetch( `/user/recoverEmail?userName=${userName}&userPhone=${userPhone}` , option );
        const data = await response.json();
        let success = data.checkSuccess
        if(success){
            getDataBox.innerHTML = "회원님의 이메일은 <strong>" + data.getEmail +" </strong> 입니다. <br/>"
            + `<a href = "/user/login.jsp"> 로그인하러 가기</a> ` 
        } else {
            getDataBox.innerHTML = "입력한 정보가 일치하지 않습니다."
        }
        }catch{

        }
} // func end 