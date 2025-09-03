let msg;

function show(text, ok) {
    msg.textContent = text || '';
    msg.style.color = ok ? 'green' : 'red';
}

const phoneLike = (s) => /^\d{2,3}-\d{3,4}-\d{4}$/.test(String(s || '').trim());

const sendEmail = async() => {
    msg  = document.querySelector('#msg');
    
    const userName  = document.querySelector('#userName').value.trim();
    const email     = document.querySelector('#email').value.trim();
    const userPhone = document.querySelector('#userPhone').value.trim();

    if (!userName || !email || !userPhone) { show('모든 값을 입력해 주세요.', false); return; }
    
    if (!phoneLike(userPhone)) { show('연락처 형식이 올바르지 않습니다. 예) 010-1234-5678', false); return; }

    try {
        const response = await fetch('/password/reset/link', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userName, email, userPhone })
        });
        const text = await response.text();
        const data = parseInt(text, 10);
        
        // 반환 값 부여
        if (data === 1){show(('해당 이메일로 메일이 발송되었습니다. 해당 이메일을 확인해주세요.') , true); return;}  
        else if (data === -1) {show(('요청이 너무 잦습니다. 잠시 후 시도하세요.') , false); return;} 
        else if (data === -2) {show(('모든 값을 입력해주세요.') , false); return;} 
        else if (data === -3) {show(('연락처 형식이 올바르지 않습니다. 예) 010-1234-5678') , false); return;} 
        
    } catch (err) {
        // 실패값 반환
        show('네트워크 오류가 발생했습니다.', false);
    }
    ;
}
// 엔터키로 제출 가능
document.getElementById('resetForm')?.addEventListener('submit', (e) => {
  e.preventDefault();
  resetPassword();
});