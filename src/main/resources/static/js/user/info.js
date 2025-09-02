// ---------- 유틸 ----------
//  $ = (sel) => document.querySelector(sel); => $(sel) 값 넣으면 그것이 곧 document.querySelector(sel)이랑 똑같은 말
const $ = (sel) => document.querySelector(sel); // jQuery 아님 주의
// 요소에서 d-none 클래스를 제거 =>  보이게 함 부트스트랩의 dispaly-d-none이 있어야 한다는 전제 
const show = (el) => el && el.classList.remove('d-none');
// 요소에서 d-none 클래스를 추가 =>  안 보이게 함 부트스트랩의 dispaly-d-none이 있어야 한다는 전제 
const hide = (el) => el && el.classList.add('d-none');

// 날짜 포맷 유틸
const fmtDate = (v) => {

    // 날짜가 값이 없으면 '-'를 반환
    if (!v) return '-';

    // 날짜 포맷이 정상적이면 그대로 보여줌. (백엔드에서 포맷을 이미 맞춰줬다는 가정) 
    if (/^\d{4}-\d{2}-\d{2}/.test(v)) return v.trim(); // "YYYY-MM-DD ..." 형태면 그대로

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
    
    // data.userNo가 null이거나 undefined라면 ''(빈 문자열) 사용.
    // 그 결과를 문자열로 변환 (.toString()).
    // 앞뒤 공백 제거 (.trim()).
    const userNo       = (data.userNo ?? '').toString().trim();
    const email        = (data.email ?? '').toString().trim();
    const userName     = (data.userName ?? '').toString().trim();
    const userPhone    = (data.userPhone ?? '').toString().trim();
    const roadAddress  = (data.roadAddress ?? '').toString().trim();
    const detailAddress= (data.detailAddress ?? '').toString().trim();
    const createDate   = (data.createDate ?? '').toString().trim();

    // DOM 바인딩
    // $(값) = document.querySelector(값) 임. 위에 유틸에 있음.
    const userNoBox   = $('#userNo-badge');
    const emailBox    = $('#email');
    const nameBox     = $('#userName');
    const phoneBox    = $('#userPhone');
    const addrBox     = $('#address');
    const addrDetail  = $('#address-detail');
    const cdate       = $('#createDate');
    
    // textContent 는 단순 텍스트만 불러온다. innerHTML은 HTML 자체를 가져온다. 차이가 있다.
    // or 연산자 => userNo?? '-' 이거는 userNo값이 '', null, undefined, 0, false, NaN이면 '-'를 쓰겠다는 거
    userNoBox.textContent   = userNo || '-' ;
    emailBox.textContent    = email || '-' ;
    nameBox.textContent     = userName || '-' ;
    phoneBox.textContent    = userPhone || '-' ;
    addrBox.textContent    = roadAddress || '-';
    addrDetail.textContent = detailAddress ? `${detailAddress}` : '';
    // 날짜 유틸 씀
    cdate.textContent = fmtDate(createDate);

    } catch (e) {
        // 패치 오류 뜨면 => 세션이 없다면 error 메세지 띄움
        errorBox.textContent = '로그인 정보가 없거나 조회에 실패했습니다.';
        show(errorBox);
    } finally {
        // fianlly 로딩 가리기
        hide(loading);
    }   // try end
};  // func end

// // ---------- 2) 회사 정보 조회 (/business/find/info) — 관리자만 ----------

// const getBusinessInfo = async () => {
//     // 사업자 카드 DOM
//     const card    = $('#business-card');
//     // 로딩 DOM
//     const loading = $('#biz-loading');
//     // 에러 메세지 DOM
//     const errorBox= $('#biz-error');
    
//     // 만약 dom객체가 null 이면 return
//     if (!card) return;
    
//     // 초기로 에러메세지는 가리고 로딩은 보여주기
//     hide(errorBox);
//     show(loading);
    
//     // FETCH 부분
//     try {
//         const option = { method: 'GET' };
//         const response = await fetch(`/business/find/info`, option);

//         // 비관리자거나 데이터 없으면 카드 숨김 유지.
//         if (!response.ok) {
//         hide(loading);
//         hide(card);
//         return;
//         }   // if end

//         const data = await response.json();
        
//         // 기대 DTO: bnNo, bnName, managerName, managerPhone, bnDocuImg, bnType, bnItem, createDate, updateDate
//         // data.XXX가  null이거나 undefined라면 ''(빈 문자열) 사용.
//         // 그 결과를 문자열로 변환 (.toString()).
//         // 앞뒤 공백 제거 (.trim()).
//         const bnNo         = (data.bnNo ?? '').toString().trim();
//         const bnName       = (data.bnName ?? '').toString().trim();
//         const managerName  = (data.managerName ?? '').toString().trim();
//         const managerPhone = (data.managerPhone ?? '').toString().trim();
//         const bnType       = (data.bnType ?? '').toString().trim();
//         const bnItem       = (data.bnItem ?? '').toString().trim();
//         const createDate   = (data.createDate ?? '').toString().trim();
//         const updateDate   = (data.updateDate ?? '').toString().trim();

//         // 관리자 데이터가 확실치 않으면 카드 숨김
//         if (!bnNo && !bnName) {
//             hide(loading);
//             hide(card);
//             return;
//         }   // if end
        
//         // setText라는 기능 추가
//         // domId에는 위에 유틸에 있는 dom을 간소화한 값임. 그것을 isDom이라는 상수에 넣고 그 dom이 있다면 textContent에 data
//         // 에서 받아온 것을 넣고 데이터 값이 없으면 '-'를 하라는 함수임.
//         const setText = ( domId, dataVal ) => { const isDom = $(domId); if (isDom) isDom.textContent = (dataVal || '-'); };
//         setText('#bnNo',          bnNo);
//         setText('#bnName',        bnName);
//         setText('#managerName',   managerName);
//         setText('#managerPhone',  managerPhone);
//         setText('#bnType',        bnType);
//         setText('#bnItem',        bnItem);
//         setText('#bizCreateDate', fmtDate(createDate));
//         setText('#bizUpdateDate', fmtDate(updateDate));

//         // 데이터 OK → 카드 보여줌
//         show(card);

//     } catch (e) {
//         // 만약 사업자번호가 세션에 없으면 에러 메세지 띄우기
//         errorBox.textContent = '회사 정보를 불러오는 중 오류가 발생했습니다.';
//         show(errorBox);
//     } finally {
//         // 결론적으로 로딩 메세지는 끝나면 없애야함
//         hide(loading);
//     }   // try end
// };  // func end

// // 새로고침 누르면 새로고침되는거랑 초기 불러오는 것
// document.addEventListener('DOMContentLoaded', () => {
//   $('#btn-refresh-user')?.addEventListener('click', getUserInfo);
//   $('#btn-refresh-business')?.addEventListener('click', getBusinessInfo);

//   // 최초 자동 조회
//   getUserInfo();
//   getBusinessInfo();
// });

// // ==========================
// // (공통) 간단 유효성
// // ==========================
// const isBlank = (s) => !s || !String(s).trim();
// const phoneLike = (s) => /^\d{2,3}-\d{3,4}-\d{4}$/.test(String(s).trim());

// // ==========================
// // A) 내 정보 수정 (US-07) — 모달 프리필 & 저장
// // PUT /user/update/info  application/json
// // body: { userName, roadAddress, detailAddress, userPhone }
// // 응답: int (-3,-2,-1,0,1)
// // ==========================

// // 버튼 클릭 → 모달 open + 프리필
// document.getElementById('btn-user-edit')?.addEventListener('click', () => {
//   const getText = (sel) => document.querySelector(sel)?.textContent.trim() || '';

//   document.getElementById('userEditName').value   = getText('#userName');
//   document.getElementById('userEditPhone').value  = getText('#userPhone');

//   // 주소는 분리 표기: 상단에는 합치지 않고 각각 프리필
//   // 현재 화면은 address(합친값) + address-detail(상세) 구조 → 원본 분리가 어려우므로
//   // 화면에서 보여준 텍스트를 그대로 프리필(roadAddress는 합친 값에서 상세 빼기 어려워서, 우선 합친 값 전체를 road에 두고 상세는 별도 유지)
//   const addr = getText('#address');
//   const detail = document.querySelector('#address-detail')?.textContent.replace(/^상세:\s*/, '').trim() || '';
//   document.getElementById('userEditRoad').value   = addr;
//   document.getElementById('userEditDetail').value = detail;

//   document.getElementById('userEditError')?.classList.add('d-none');

//   // 모달 띄우기
//   const modalEl = document.getElementById('userEditModal');
//   const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
//   modal.show();
// });

// // 저장
// document.getElementById('btn-user-save')?.addEventListener('click', async () => {
//   const err = document.getElementById('userEditError');

//   const payload = {
//     userName:     document.getElementById('userEditName').value.trim(),
//     userPhone:    document.getElementById('userEditPhone').value.trim(),
//     roadAddress:  document.getElementById('userEditRoad').value.trim(),
//     detailAddress:document.getElementById('userEditDetail').value.trim()
//   };

//   // 필드 제한: 명세 외에는 전송 안 함 (이미 payload 한정됨)
//   // 간단 유효성
//   if ([payload.userName, payload.userPhone, payload.roadAddress].some(isBlank)) {
//     err.textContent = '이름, 연락처, 도로명 주소는 필수입니다.';
//     err.classList.remove('d-none');
//     return;
//   }
//   if (!phoneLike(payload.userPhone)) {
//     err.textContent = '연락처 형식이 올바르지 않습니다. 예) 010-1234-5678';
//     err.classList.remove('d-none');
//     return;
//   }

//   const option = {
//     method: 'PUT',
//     headers: { 'Content-Type': 'application/json' },
//     body: JSON.stringify(payload)
//   };

//   try {
//     const res = await fetch('/user/update/info', option);
//     // 응답이 int 단일값일 수 있으므로 안전 파싱
//     const text = await res.text();
//     const code = parseInt(text, 10);

//     if (isNaN(code) || !res.ok) {
//       err.textContent = `수정 실패 (HTTP ${res.status})`;
//       err.classList.remove('d-none');
//       return;
//     }

//     // 코드별 메시지
//     const msgBy = {
//       [-3]: '값이 공백이거나 null 값이 있습니다.',
//       [-2]: '전화번호 형식 오류입니다.',
//       [-1]: '세션이 없습니다. 다시 로그인하세요.',
//       [0]:  '수정에 실패했습니다.',
//       [1]:  '수정 성공!'
//     };

//     if (code !== 1) {
//       err.textContent = msgBy[code] ?? `수정 실패 (code ${code})`;
//       err.classList.remove('d-none');
//       return;
//     }

//     // 성공 → 화면 갱신
//     const setText = (sel, v) => { const el = document.querySelector(sel); if (el) el.textContent = (v || '-'); };
//     setText('#userName',  payload.userName);
//     setText('#userPhone', payload.userPhone);

//     // 주소 갱신: 상단 address에는 road + detail 합치기
//     const addressJoin = [payload.roadAddress, payload.detailAddress].filter(Boolean).join(' ');
//     setText('#address', addressJoin || '-');
//     document.querySelector('#address-detail').textContent = payload.detailAddress ? `상세: ${payload.detailAddress}` : '';

//     // 모달 닫기
//     const modalEl = document.getElementById('userEditModal');
//     const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
//     modal.hide();

//   } catch (e) {
//     err.textContent = '네트워크 오류가 발생했습니다.';
//     err.classList.remove('d-none');
//   }
// });

// // ==========================
// // B) 사업자 정보 수정 (BS-04) — 모달 프리필 & 저장
// // PUT /business/update/info  multipart/form-data
// // body: managerName, managerPhone, bnDocuImg(file), bnType, bnItem
// // 응답: int (-4,-3,-2,-1,0,1)
// // ==========================

// document.getElementById('btn-edit-business')?.addEventListener('click', () => {
//   const getText = (sel) => document.querySelector(sel)?.textContent.trim() || '';

//   // 현재 카드 값으로 프리필 (bnNo/bnName은 수정 금지 → readonly)
//   document.getElementById('bizEditBnNo').value         = getText('#bnNo');
//   document.getElementById('bizEditBnName').value       = getText('#bnName');
//   document.getElementById('bizEditManagerName').value  = getText('#managerName');
//   document.getElementById('bizEditManagerPhone').value = getText('#managerPhone');
//   document.getElementById('bizEditBnType').value       = getText('#bnType');
//   document.getElementById('bizEditBnItem').value       = getText('#bnItem');

//   // 파일 인풋 초기화
//   const file = document.getElementById('bizEditDocuFile');
//   if (file) file.value = '';

//   document.getElementById('bizEditError')?.classList.add('d-none');
// });

// // 저장
// document.getElementById('btn-biz-save')?.addEventListener('click', async () => {
//   const err = document.getElementById('bizEditError');

//   const managerName  = document.getElementById('bizEditManagerName').value.trim();
//   const managerPhone = document.getElementById('bizEditManagerPhone').value.trim();
//   const bnType       = document.getElementById('bizEditBnType').value.trim();
//   const bnItem       = document.getElementById('bizEditBnItem').value.trim();
//   const fileInput    = document.getElementById('bizEditDocuFile');

//   // 필수/형식 체크 (명세에 따라 최소한)
//   if ([managerName, managerPhone, bnType, bnItem].some(isBlank)) {
//     err.textContent = '담당자명, 담당자번호, 업태, 종목은 필수입니다.';
//     err.classList.remove('d-none');
//     return;
//   }
//   if (!phoneLike(managerPhone)) {
//     err.textContent = '담당자번호 형식이 올바르지 않습니다. 예) 010-1234-5678';
//     err.classList.remove('d-none');
//     return;
//   }

//   const formData = new FormData();
//   formData.append('managerName',  managerName);
//   formData.append('managerPhone', managerPhone);
//   formData.append('bnType',       bnType);
//   formData.append('bnItem',       bnItem);
//   if (fileInput?.files?.[0]) {
//     formData.append('bnDocuImg', fileInput.files[0]); // 파일 선택 시에만 전송
//   }

//   try {
//     const res = await fetch('/business/update/info', {
//       method: 'PUT',               // 명세에 맞춤
//       body: formData               // multipart/form-data (헤더 직접 설정 금지!)
//     });

//     const text = await res.text();
//     const code = parseInt(text, 10);

//     if (isNaN(code) || !res.ok) {
//       err.textContent = `저장 실패 (HTTP ${res.status})`;
//       err.classList.remove('d-none');
//       return;
//     }

//     const msgBy = {
//       [-4]: '이전 파일 삭제 실패 (업로드는 성공).',
//       [-3]: '공백 또는 null 값이 있습니다.',
//       [-2]: '전화번호 형식 오류입니다.',
//       [-1]: '세션이 없습니다. 다시 로그인하세요.',
//       [0]:  '수정에 실패했습니다.',
//       [1]:  '수정 성공!'
//     };

//     if (code < 1) {
//       err.textContent = msgBy[code] ?? `저장 실패 (code ${code})`;
//       err.classList.remove('d-none');
//       return;
//     }

//     // 성공 → 화면 값 갱신
//     const setText = (sel, v) => { const el = document.querySelector(sel); if (el) el.textContent = (v || '-'); };
//     setText('#managerName',  managerName);
//     setText('#managerPhone', managerPhone);
//     setText('#bnType',       bnType);
//     setText('#bnItem',       bnItem);

//     // 이미지 갱신(파일 보냈을 때만) — 서버가 새 파일명을 알려주지 않으면 새로고침이 가장 확실
//     // 여기서는 간단히 캐시 무효화를 위해 쿼리 붙여 재로딩 시도
//     if (fileInput?.files?.[0]) {
//       const imgEl = document.querySelector('#bnDocuImg');
//       if (imgEl && imgEl.src) {
//         const u = new URL(imgEl.src, location.origin);
//         u.searchParams.set('t', Date.now().toString());
//         imgEl.src = u.toString();
//       }
//     }

//     // 모달 닫기
//     const modalEl = document.getElementById('bizEditModal');
//     const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
//     modal.hide();

//     // code === -4 (old 파일 삭제 실패)도 성공으로 간주되므로 알림만 주고 넘어갈 수도 있음
//     if (code === -4) {
//       console.warn('기존 파일 삭제 실패(업로드 성공). 관리자 점검 필요');
//     }

//   } catch (e) {
//     err.textContent = '네트워크 오류가 발생했습니다.';
//     err.classList.remove('d-none');
//   }
// });