let msg;

// 메시지 출력 함수
function show(text, ok) {
  msg.textContent = text || '';
  msg.style.color = ok ? 'green' : 'red';
}

// 비밀번호 형식 체크 (8자 이상, 영문 대소문자 최소 1개 포함)
const strongPwd = (s) => {
  const v = String(s || '');
  return (
    v.length >= 8 &&
    /[A-Za-z]/.test(v)   // 영문 대소문자 최소 1개 포함
  );
};

const resetPassword = async () => {
  msg = document.querySelector('#msg');

  const newPassword = document.querySelector('#pw1').value.trim();
  const confirmPassword = document.querySelector('#pw2').value.trim();

  // 프론트 유효성 검사
  if (!newPassword || !confirmPassword) {
    show('모든 값을 입력해 주세요.', false);
    return;
  }
  if (newPassword !== confirmPassword) {
    show('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.', false);
    return;
  }
  if (!strongPwd(newPassword)) {
    show('비밀번호 형식이 올바르지 않습니다. (영문/숫자 포함 8자 이상)', false);
    return;
  }

  try {
    const response = await fetch('/password/reset/confirm', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ newPassword, confirmPassword })
    });

    const text = await response.text();
    const code = parseInt(text, 10);

    let message;
    if (code === 1) {
      message = '비밀번호가 성공적으로 변경되었습니다.';
      show(message, true);

      // 2초 뒤 성공 페이지 이동
      setTimeout(() => {
        location.href = '/password/reset/success';
      }, 2000);
    } else if (code === 0) {
      message = '시스템 오류로 변경에 실패했습니다.';
      show(message, false);
    } else if (code === -1) {
      message = '새 비밀번호와 확인 비밀번호가 일치하지 않습니다.';
      show(message, false);
    } else if (code === -2) {
      message = '모든 값을 입력해 주세요.';
      show(message, false);
    } else if (code === -3) {
      message = '비밀번호 형식이 올바르지 않습니다. (영문/숫자 포함 8자 이상)';
      show(message, false);
    } else if (code === -4) {
      message = '토큰이 만료되었거나 유효하지 않습니다. 다시 요청해 주세요.';
      show(message, false);
      setTimeout(() => {
        location.href = '/password/reset/invalid';
      }, 2000);
    } else {
      show(`알 수 없는 오류 (code: ${code})`, false);
    }
  } catch (err) {
    show('네트워크 오류가 발생했습니다.', false);
  }
};

// 엔터키로 제출 가능
document.getElementById('resetForm')?.addEventListener('submit', (e) => {
  e.preventDefault();
  resetPassword();
});

// 눈모양 토글(표시/숨김)
// resetForm id를 가진 dom을 불러오는데 없으면 그냥 넘어가라, 있으면 click시 이벤트를 실행해라. 라는 뜻
document.getElementById('resetForm')?.addEventListener('click', (e) => {
  // 클릭한 곳의 부모와 조상을 찾아서 근처에 closest() 안에 있는 것을 찾는다.
  // []는 그 요소들을 가진 모든 것을 찾는다. => 그 속성이 있으면 다 잡힘 (값은 상관 없음)
  const btn = e.target.closest('[data-toggle-eye]');
  // 없으면 return;
  if (!btn) return;
  // getAttibute(). 그 안에 있는 속성값을 가져온다. value 값을 가져온다는 뜻
  const sel = btn.getAttribute('data-toggle-eye');
  // 그것을 dom화 한다.
  const input = document.querySelector(sel);
  // 그 dom 값이 없으면 return 한다.
  if (!input) return;
  // 그 input의 타입이 password이면 text로 바꾸고 아니면 password로 바꾼다.
  input.type = input.type === 'password' ? 'text' : 'password';
  // 그 btn의 text값을 바꾸는데 type 이 password면 표시로 뜨게 아니면 숨김으로 뜨게 한다.
  btn.textContent = input.type === 'password' ? '표시' : '숨김';
});
