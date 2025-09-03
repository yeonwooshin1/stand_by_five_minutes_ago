// 로그인 js 콘솔
console.log( 'login.js open!');

// [1] 로그인 , login 
const login = async() =>{
    
    // 마크업을 DOM객체로 가져온다.
    const emailInput = document.querySelector('.emailInput'); // 지정한 선택자의 마크업을 DOM객체로 가져오기 
    const pwdInput =  document.querySelector('.pwdInput');
    
    // 가져온 마크업의 입력받은 값 가져오기
    
    const email = emailInput.value;   
    const passwordHash = pwdInput.value; 
    
    // 3. 객체화 , 객체내 속성명과 속성값변수명이 같으면 생략가능 , 주의할점 *자바의 DTO 의 멤버변수명*과 동일
    const obj = { email , passwordHash };

    try{ // 4. fetch 실행 
        const option = {  method : "POST",  headers : { "Content-Type" : "application/json"},  body : JSON.stringify( obj ) }
        const response = await fetch( "/user/login" , option ); // login API 요청 
        const data = await response.text();
        
        // 5. fetch 응답
        if( data > 0 ){ // 0보다 크면 로그인성공이고 회원번호 반환 
            alert('환영합니다.');
            location.href="/index.jsp"; // 메인페이지로 이동 
        }else{
            alert('이메일 또는 비밀번호가 일치하지 않습니다.');
        }
    }catch{  }

} // func end 