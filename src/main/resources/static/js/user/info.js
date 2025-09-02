// ---------- 유틸 ----------
//  $ = (sel) => document.querySelector(sel); => $(sel) 값 넣으면 그것이 곧 document.querySelector(sel)이랑 똑같은 말
const $ = (sel) => document.querySelector(sel); // jQuery 아님 주의
// 요소에서 d-none 클래스를 제거 =>  보이게 함 부트스트랩의 dispaly-d-none이 있어야 한다는 전제 
const show = (el) => el && el.classList.remove('d-none');
// 요소에서 d-none 클래스를 추가 =>  안 보이게 함 부트스트랩의 dispaly-d-none이 있어야 한다는 전제 
const hide = (el) => el && el.classList.add('d-none');

// 안전하게 Dom textContent 가져오기 헬퍼 (null/undefined 에서 .trim() 오류 나는거 방지용
const getText = (sel) => ((document.querySelector(sel)?.textContent ?? '') + '').trim();

// 날짜 포맷 유틸
const fmtDate = (v) => {

    // 날짜가 값이 없으면 '-'를 반환
    if (!v) return '-';

    // 날짜 포맷이 정상적이면 그대로 보여줌. (백엔드에서 포맷을 이미 맞춰줬다는 가정) 
    if (/^\d{4}-\d{2}-\d{2}/.test(v)) return String(v).trim(); // "YYYY-MM-DD ..." 형태면 그대로

    // 그 외 문자열/타입이면 Date로 파싱해서 브라우저/사용자 로케일 기준으로 표시.
    try { return new Date(v).toLocaleString(); } catch { return String(v); }
};

// ---------- 1) 내 정보 조회 (/user/find/info) ----------
const getUserInfo = async () => {
    // 로딩 dom 객체화
    const loading = $('#user-loading');
    // 오류 메세지 dom 객체화
    const errorBox = $('#user-error');

    // 에러메세지는 초기값 숨기기, 로딩은 보여주기
    hide(errorBox);
    show(loading);

    // fetch
    try {
        const option = { method: 'GET' };
        const response = await fetch(`/user/find/info`, option);
        
        // 만약 패치가 잘 안 됐다?
        if (!response.ok) {
            // errorBox에 에러 메세지 표시
            errorBox.textContent = '사용자 정보를 불러오지 못했습니다.';
            // 로딩 제거, 에러메세지 표시
            show(errorBox);
            hide(loading);
            return;
        }   // if end

        // 잘 넘어왔으면 data 값 받아오기
        const data = await response.json();

        // 기대 DTO: userNo, email, userName, userPhone, roadAddress, detailAddress, createDate
        const userNo        = (data.userNo ?? '').toString().trim();
        const email         = (data.email ?? '').toString().trim();
        const userName      = (data.userName ?? '').toString().trim();
        const userPhone     = (data.userPhone ?? '').toString().trim();
        const roadAddress   = (data.roadAddress ?? '').toString().trim();
        const detailAddress = (data.detailAddress ?? '').toString().trim();
        const createDate    = (data.createDate ?? '').toString().trim();

        // DOM 바인딩
        // $(값) = document.querySelector(값) 임. 위에 유틸에 있음.
        const userNoBox  = $('#userNo-badge');
        const emailBox   = $('#email');
        const nameBox    = $('#userName');
        const phoneBox   = $('#userPhone');
        const addrBox    = $('#address');
        const addrDetail = $('#address-detail');
        const cdate      = $('#createDate');
        
        // textContent 는 단순 텍스트만 불러온다. innerHTML은 HTML 자체를 가져온다. 차이가 있다.
        // or 연산자 => userNo?? '-' 이거는 userNo값이 '', null, undefined, 0, false, NaN이면 '-'를 쓰겠다는 거
        userNoBox.textContent  = userNo || '-' ;
        emailBox.textContent   = email || '-' ;
        nameBox.textContent    = userName || '-' ;
        phoneBox.textContent   = userPhone || '-' ;
        addrBox.textContent    = roadAddress || '-';
        addrDetail.textContent = detailAddress ? `${detailAddress}` : '';
        // 날짜 유틸 씀
        cdate.textContent = fmtDate(createDate);

    } catch (e) {
        // 패치 오류 뜨면 => 세션이 없다면 error 메세지 띄움
        const errorBox = $('#user-error');
        errorBox.textContent = '로그인 정보가 없거나 조회에 실패했습니다.';
        show(errorBox);
    } finally {
        // fianlly 로딩 가리기
        hide(loading);
    }   // try end
};  // func end

// ---------- 2) 회사 정보 조회 (/business/find/info) — 관리자만 ----------

const getBusinessInfo = async () => {
    // 사업자 카드 DOM
    const card    = $('#business-card');
    // 로딩 DOM
    const loading = $('#biz-loading');
    // 에러 메세지 DOM
    const errorBox= $('#biz-error');
    
    // 만약 dom객체가 null 이면 return
    if (!card) return;
    
    // 초기로 에러메세지는 가리고 로딩은 보여주기
    hide(errorBox);
    show(loading);
    
    // FETCH 부분
    try {
        const option = { method: 'GET' };
        const response = await fetch(`/business/find/info`, option);

        // 비관리자거나 데이터 없으면 카드 숨김 유지.
        if (!response.ok) {
            hide(loading);
            hide(card);
            return;
        }   // if end

        const data = await response.json();
        
        // 기대 DTO: bnNo, bnName, managerName, managerPhone, bnDocuImg, bnType, bnItem, createDate, updateDate
        const bnNo         = (data.bnNo ?? '').toString().trim();
        const bnName       = (data.bnName ?? '').toString().trim();
        const managerName  = (data.managerName ?? '').toString().trim();
        const managerPhone = (data.managerPhone ?? '').toString().trim();
        const bnType       = (data.bnType ?? '').toString().trim();
        const bnItem       = (data.bnItem ?? '').toString().trim();
        const createDate   = (data.createDate ?? '').toString().trim();
        const updateDate   = (data.updateDate ?? '').toString().trim();

        // 관리자 데이터가 확실치 않으면 카드 숨김
        if (!bnNo && !bnName) {
            hide(loading);
            hide(card);
            return;
        }   // if end
        
        // setText라는 기능 추가
        // domId에는 위에 유틸에 있는 dom을 간소화한 값임. 그것을 isDom이라는 상수에 넣고 그 dom이 있다면 textContent에 data
        // 에서 받아온 것을 넣고 데이터 값이 없으면 '-'를 하라는 함수임.
        const setText = ( domId, dataVal ) => { const isDom = $(domId); if (isDom) isDom.textContent = (dataVal || '-'); };
        setText('#bnNo',          bnNo);
        setText('#bnName',        bnName);
        setText('#managerName',   managerName);
        setText('#managerPhone',  managerPhone);
        setText('#bnType',        bnType);
        setText('#bnItem',        bnItem);
        setText('#bizCreateDate', fmtDate(createDate));
        setText('#bizUpdateDate', fmtDate(updateDate));

        // 데이터 OK → 카드 보여줌
        show(card);

    } catch (e) {
        // 만약 사업자번호가 세션에 없으면 에러 메세지 띄우기
        const errorBox= $('#biz-error');
        errorBox.textContent = '회사 정보를 불러오는 중 오류가 발생했습니다.';
        show(errorBox);
    } finally {
        // 결론적으로 로딩 메세지는 끝나면 없애야함
        hide(loading);
    }   // try end
};  // func end

// 새로고침 누르면 새로고침되는거랑 초기 불러오는 것
document.addEventListener('DOMContentLoaded', () => {
  $('#btn-refresh-user')?.addEventListener('click', getUserInfo);
  $('#btn-refresh-business')?.addEventListener('click', getBusinessInfo);

  // 최초 자동 조회
  getUserInfo();
  getBusinessInfo();
});

// ========================== 공통 유효성 ==========================
// 값이 비었는지 확인하는 헬퍼.
// - undefined / null / ''(공백 포함) → true
const isBlank = (s) => !s || !String(s).trim();

// 전화번호 간단 형식 체크(하이픈 포함).
// - 02-123-4567 / 010-1234-5678 등 지원
// - 숫자 2~3자리 - 숫자 3~4자리 - 숫자 4자리
const phoneLike = (s) => /^\d{2,3}-\d{3,4}-\d{4}$/.test(String(s).trim());

// ========================== 모달 닫기/백드롭 정리 ==========================
// 부트스트랩 모달을 닫고, 혹시 남아있을 수 있는 백드롭/스크롤 잠금을 깨끗하게 청소.
// - 일부 환경/동작 순서에서 백드롭이 남아 화면이 회색으로 비활성처럼 보일 수 있어 대비용.
// ✅ 중요: Bootstrap이 cleanup을 수행하므로 강제 DOM 삭제는 하지 않음.
const closeModalClean = (modalId) => {
  // 모달 DOM을 찾고
  const el = document.getElementById(modalId);
  if (!el) return;
  // 이미 생성된 모달 인스턴스만 사용(새로 생성 금지: 상태 꼬임 방지)
  const inst = bootstrap.Modal.getInstance(el);
  if (inst) {
    // 포커스가 안쪽에 남아 접근성 경고가 나는 케이스 방지
    document.activeElement?.blur();
    // 모달 닫기 (transition → hidden.bs.modal → Bootstrap이 backdrop/body 정리)
    inst.hide();
  }
};

// ========================== 내 정보 수정 (US-07) ==========================
// 모달 열기 + 프리필
// ❗ 기존: 클릭에서 프리필 → ✅ 변경: 모달 show 시 프리필 (안정성↑)
document.getElementById('userEditModal')?.addEventListener('show.bs.modal', () => {
  // 유틸 함수 해당 매개변수의 dom의 해당 안에 있는 text를 가져오고 공백제거 한 값이 
  // null 이거나 false거나 공백이면 ' '으로 준다.  
  const addr   = getText('#address');
  const detail = getText('#address-detail');

  // 기존 값 수정 value 에 넣어주기
  $('#userEditName').value  = getText('#userName');
  $('#userEditPhone').value = getText('#userPhone');
  $('#userEditRoad').value   = addr;
  $('#userEditDetail').value = detail;

  // 에러메세지 안보이게 하기
  $('#userEditError')?.classList.add('d-none');
});

// 기존 클릭 핸들러는 "모달을 programmatic하게 오픈" 용으로만 사용(프리필은 위에서 함)
let _userEditInst = null;
$('#btn-user-edit')?.addEventListener('click', () => {
  // 모달을 programmatic하게 오픈
  const modalEl = $('#userEditModal');
  // 한 번만 생성해서 재사용 (중복 new 방지)
  _userEditInst = _userEditInst || new bootstrap.Modal(modalEl);
  _userEditInst.show();
});

// 저장
$('#btn-user-save')?.addEventListener('click', async (e) => {
  // 혹시 폼 submit 기본동작 방지 막는거 
  e.preventDefault();
  // error DOM 변수 지정
  const err = $('#userEditError');
  
  // obj 객체 담기 
  const obj = {
    userName:      $('#userEditName').value.trim(),
    userPhone:     $('#userEditPhone').value.trim(),
    roadAddress:   $('#userEditRoad').value.trim(),
    detailAddress: $('#userEditDetail').value.trim()
  };

  // 유효성 검사 : 모든 값들이 다 들어가야함
  if ([obj.userName, obj.userPhone, obj.roadAddress].some(isBlank)) {
    err.textContent = '모든 항목 입력 필수입니다.'; show(err); return;
  } // if end
  // 유효성 검사 : 전화번호 형식이 일치해야함.
  if (!phoneLike(obj.userPhone)) {
    err.textContent = '연락처 형식이 올바르지 않습니다. 예) 010-1234-5678'; show(err); return;
  } // if end
  // fetch 부분
  try {
    const response = await fetch('/user/update/info', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(obj)
    });

    const text = await response.text();
    const data = parseInt(text, 10);
    let msg;

    // 반환 값 부여
    if (data === 0) msg = '수정 중 오류가 발생했습니다. (= 이미 사용 중인 연락처 입니다.)';
    else if (data === -1) msg = '세션이 존재하지 않습니다. 재로그인 해주세요.';
    else if (data === -2) msg = '연락처 형식이 올바르지 않습니다. 예) 010-1234-5678';
    else if (data === -3) msg = '모든 항목 입력 필수입니다.';
    
    if (data !== 1) { err.textContent = msg ?? `수정 실패 (${msg})`; show(err); return; }

    // 화면 갱신
    // setText라는 기능 추가
    // domId에는 위에 유틸에 있는 dom을 간소화한 값임. 그것을 isDom이라는 상수에 넣고 그 dom이 있다면 textContent에 data
    // 에서 받아온 것을 넣고 데이터 값이 없으면 '-'를 하라는 함수임.
    const setText = (domId, dataVal ) => { const isDom = $(domId); if (isDom) isDom.textContent = (dataVal || '-'); };
    setText('#userName', obj.userName);
    setText('#userPhone', obj.userPhone);
    setText('#address', obj.roadAddress || '-');
    $('#address-detail').textContent = obj.detailAddress ? `${obj.detailAddress}` : '';

    // 닫기 (Bootstrap 루틴 그대로 타도록 취소버튼 programmatic click 사용)
    document.querySelector('#userEditModal [data-bs-dismiss="modal"]')?.click();
  } catch (e2) {
    // 오류 뜰시 에러메세지 넣기
    err.textContent = '네트워크 오류가 발생했습니다.'; show(err);
  } // try end
});

// ========================== 사업자 정보 수정 (BS-04) ==========================
// 모달 열기 + 프리필
// ❗ 기존: 버튼 클릭 핸들러에서 프리필 → ✅ 변경: 모달 show 시 프리필
document.getElementById('bizEditModal')?.addEventListener('show.bs.modal', () => {
  
  // 유틸 함수 해당 매개변수의 dom의 해당 안에 있는 text를 가져오고 공백제거 한 값이 
  // null 이거나 false거나 공백이면 ' '으로 준다. 
  $('#bizEditBnNo').value         = getText('#bnNo');
  $('#bizEditBnName').value       = getText('#bnName');
  $('#bizEditManagerName').value  = getText('#managerName');
  $('#bizEditManagerPhone').value = getText('#managerPhone');
  $('#bizEditBnType').value       = getText('#bnType');
  $('#bizEditBnItem').value       = getText('#bnItem');
  const file = $('#bizEditDocuFile'); if (file) file.value = '';
  // 에러메세지 안보이게 하기
  $('#bizEditError')?.classList.add('d-none');
});

// ✅ 버튼 클릭 핸들러는 "값 세팅"을 더 이상 하지 않음 (data-bs-toggle 이 열어줌)
$('#btn-edit-business')?.addEventListener('click', () => {
  // (프리필은 show.bs.modal에서 수행)
  $('#bizEditError')?.classList.add('d-none');
});

// 저장 (PUT multipart/form-data)
$('#btn-biz-save')?.addEventListener('click', async (e) => {
  // 혹시 폼 submit 기본동작 방지 막는거 
  e.preventDefault();
  // error DOM 변수 지정
  const err = $('#bizEditError');

  // dom 값 담기
  const managerName  = $('#bizEditManagerName').value.trim();
  const managerPhone = $('#bizEditManagerPhone').value.trim();
  const bnType       = $('#bizEditBnType').value.trim();
  const bnItem       = $('#bizEditBnItem').value.trim();
  const fileInput    = $('#bizEditDocuFile');
  // 유효성 검사 공백 있나 없나 확인
  if ([managerName, managerPhone, bnType, bnItem].some(isBlank)) { err.textContent = ' 담당자명, 담당자번호, 업태, 종목은 필수입니다. '; show(err); return; }
  
  // 형식 검사
  if (!phoneLike(managerPhone)) { err.textContent = '담당자번호 형식이 올바르지 않습니다. 예) 010-1234-5678'; show(err); return; }
  
  // formData 생성
  const formData = new FormData();
  formData.append('managerName',  managerName);
  formData.append('managerPhone', managerPhone);
  formData.append('bnType',       bnType);
  formData.append('bnItem',       bnItem);

  if (fileInput?.files?.[0]) {
    // DTO의 MultipartFile 필드명과 동일하게 보냄: uploadBnImg
    formData.append('uploadBnImg', fileInput.files[0]);
  } // if end

  // fetch
  try {
    const response = await fetch('/business/update/info', { method: 'PUT', body: formData });
    const text = await response.text();
    const data = parseInt(text, 10);
    let msg;
    // 반환 값 부여
    if (data === 0) msg = '수정 중 오류가 발생했습니다. (= 이미 사용 중인 연락처 입니다.)';
    else if (data === -1) msg = '세션이 존재하지 않습니다. 재로그인 해주세요.';
    else if (data === -2) msg = '연락처 형식이 올바르지 않습니다. 예) 010-1234-5678';
    else if (data === -3) msg = '모든 항목 입력 필수입니다.';

    if (data !== 1) { err.textContent = msg ?? `수정 실패 (${msg})`; show(err); return; }

    // 화면 갱신
    const setText = (sel, v) => { const el = $(sel); if (el) el.textContent = (v || '-'); };
    setText('#managerName',  managerName);
    setText('#managerPhone', managerPhone);
    setText('#bnType',       bnType);
    setText('#bnItem',       bnItem);

    // 파일 보냈으면 이미지 캐시 무효화(이미지 태그가 있다면)
    const imgEl = $('#bnDocuImg');
    if (fileInput?.files?.[0] && imgEl && imgEl.src) {
      const u = new URL(imgEl.src, location.origin);
      u.searchParams.set('t', Date.now().toString());
      imgEl.src = u.toString();
    }

    // 안전하게 닫기 (취소 버튼 programmatic click → Bootstrap 루틴 100%)
    document.querySelector('#bizEditModal [data-bs-dismiss="modal"]')?.click();

  } catch (e2) {
    // 오류 뜰 시 메세지 보내기
    err.textContent = '네트워크 오류가 발생했습니다.'; show(err);
    // 실패시에는 닫지 않음(UX)
  } // try end
});
