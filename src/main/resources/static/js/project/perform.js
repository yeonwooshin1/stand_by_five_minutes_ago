console.log("perform.js loaded");

// ===== 전역 fetch /DOM  =====
const API_LOOKUP = (pjNo) => `/project/lookup?pjNo=${pjNo}`;
const API_LIST   = (pjNo) => `/project/perform?pjNo=${pjNo}`;
const API_SAVE   = (pjNo) => `/project/perform?pjNo=${pjNo}`;

const $tbody  = document.querySelector("#pfBody");   // 테이블 바디
const $btnAdd = document.querySelector("#btnAdd");   // 행 추가 버튼
const $err    = document.querySelector("#pfError");  // 에러 영역

// 룩업 데이터 FK값
let ROLE_LIST = [];    // [{pjRoleNo, pjUserName, pjRoleName}]
let ITEM_LIST = [];    // [{pjChkItemNo, pjChklTitle}]  // [ADD] pjHelpText도 포함되어야 함(백엔드가 내려줌)

// 임시 저장될 배열
let ROWS = [];         // [{pfNo, pjRoleNo, pjChkItemNo, pfStart, pfEnd, notifyType, notifySetMins, pfStatus, changeStatus}]

// 신규행 임시PK --> 음수 ID(-1, -2, 같은 값)
let tempId = -1;

// [ADD] ===== 설명 모달 핸들 =====
let descModal;                                    // Bootstrap Modal 인스턴스 저장용
const $descTitle = () => document.querySelector("#descTitle"); // 모달 제목 영역
const $descBody  = () => document.querySelector("#descBody");  // 모달 본문 영역

// ===== 시작 지점 =====
// 로딩 렌더링 지정
window.onHeaderReady = async () => {
  await loginCheck();       // 로그인/권한 체크
  await loadLookups();      // 역할/체크리스트 조회
  await loadList();         // 업무 목록 조회
  render();                 // 테이블 그리기

  bindAddButton();          // 행 추가
  bindTableEvents();        // 테이블 값 변경/삭제
  bindTopButtons();         // 저장/다음 버튼

  // [ADD] 설명 버튼 및 모달 초기화
  addHelpButtons();         // 렌더된 각 행의 체크리스트 select 옆에 '설명' 버튼을 동적으로 추가
  bindHelpEvents();         // 설명 버튼 클릭 이벤트(위임)
  initDescModal();          // 모달 인스턴스 준비
};

// ===== 유틸리티 =====

// [0] 로그인 체크
const loginCheck = async () => {
    // console.log("loginCheck func exe")
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    } else if (businessNo == null || businessNo === 0) {
        alert("[경고] 일반회원은 사용불가능한 메뉴입니다.")
        location.href = "/index.jsp"
    }
}

// "HH:mm:ss" 또는 "HH:mm" → "HH:mm"으로 정리 
function toHHMM(v) {
  if (!v) return "";
  return v.length >= 5 ? v.slice(0, 5) : v;
}

// 상태 배지 HTML (읽기 전용) 
function statusBadge(st) {
  // 1: 시작전  2: 진행중  3: 완료됨  4: 취소됨  5: 보류
  const MAP = {
    1: { t: "시작전",  cls: "secondary" },
    2: { t: "진행중",  cls: "info" },
    3: { t: "완료됨",  cls: "success" },
    4: { t: "취소됨",  cls: "danger" },
    5: { t: "보류",    cls: "warning" }
  };
  const v = MAP[Number(st)] || { t: "-", cls: "dark" };
  return `<span class="badge text-bg-${v.cls}">${v.t}</span>`;
}

/** 숫자 변환(실패시 0) */
const toNum0 = (x) => (Number.isFinite(Number(x)) ? Number(x) : 0);

/** 기존행 여부 (pfNo > 0 이면 기존행) */
const isExisting = (pfNo) => Number(pfNo) > 0;

// ===== 데이터 로딩 =====

// 룩업(역할/체크리스트) 배열에 채워주기
async function loadLookups() {
  try {
    const response = await fetch(API_LOOKUP(pjNo));
    const data = await response.json();

    ROLE_LIST = Array.isArray(data.roles) ? data.roles : [];

    ITEM_LIST = Array.isArray(data.chkItems) ? data.chkItems : [];

    console.log("LOOKUP:", { ROLE_LIST, ITEM_LIST }); // 진단 로그
  } catch (e) {
    console.error(e);
    $err.textContent = "룩업 데이터를 불러오지 못했습니다.";
  }
}

// 업무 목록 로딩 => ROWS 배열 채우기
async function loadList() {
  try {
    const response = await fetch(API_LIST(pjNo));
    const list = await response.json();
    // 있으면 채우기 map 써서
    ROWS = (Array.isArray(list) ? list : []).map((dto) => ({
      pfNo:            Number(dto.pfNo) || 0,
      pjRoleNo:        Number(dto.pjRoleNo) || 0,
      pjChkItemNo:     Number(dto.pjChkItemNo) || 0,
      pfStart:         toHHMM(dto.pfStart || ""),
      pfEnd:           toHHMM(dto.pfEnd || ""),
      notifyType:      Number(dto.notifyType ?? 0),
      notifySetMins:   Number(dto.notifySetMins ?? 0),
      pfStatus:        Number(dto.pfStatus ?? 1),
      changeStatus:    0
    }));
  } catch (e) {
    console.error(e);
    $err.textContent = "업무 목록을 불러오지 못했습니다.";
  }
}   // func end

// ===== 렌더링 =====

// 역할 옵션 
function roleOptions(selected) {
  let html = `<option value="0">-- 역할 선택 --</option>`;
  // 역할 리스트 돌면서 없으면 "" 있으면 채워주기
  for (const r of ROLE_LIST) {
    const sel = Number(selected) === Number(r.pjRoleNo) ? "selected" : "";
    html += `<option value="${r.pjRoleNo}" ${sel}>${r.pjRoleName} (${r.pjUserName})</option>`;
  }
  return html;
}

// 체크리스트 옵션 
function itemOptions(selected) {
  let html = `<option value="0">-- 체크리스트 선택 --</option>`;
  // 체크 리스트 돌면서 없으면 "" 있으면 채워주기
  for (const c of ITEM_LIST) {
    const sel = Number(selected) === Number(c.pjChkItemNo) ? "selected" : "";
    html += `<option value="${c.pjChkItemNo}" ${sel}>${c.pjChklTitle}</option>`;
  }
  return html;
}

/** 알림 조건 옵션 (notifyType) */
function notifyTypeOptions(selected) {
  const L = [
    { v: 0, t: "미발송" },
    { v: 1, t: "시작 전" },
    { v: 2, t: "시작 후" },
    { v: 3, t: "종료 전" },
    { v: 4, t: "종료 후" }
  ];
  return L.map(o => `<option value="${o.v}" ${Number(selected)===o.v ? "selected":""}>${o.t}</option>`).join("");
}

/** 테이블 렌더링 */
function render() {
  const rows = ROWS.filter(r => r.changeStatus !== 2 && r.changeStatus !== 4);

  const html = rows.map((row, idx) => {
    const isSent = Number(row.notifyType) === -1;               // 알람발송됨
    const disabledMins = (Number(row.notifyType) === 0 || isSent) ? "disabled" : "";

    const notifyCell = isSent
      ? `<div class="form-control-plaintext text-success fw-semibold">알람발송됨</div>`
      : `<select class="form-select pf-notify-type">${notifyTypeOptions(row.notifyType)}</select>`;

    return `
      <tr data-pfno="${row.pfNo}">
        <td class="text-center">${idx + 1}</td>

        <!-- 역할/체크리스트/삭제: 그대로 편집 가능 -->
        <td>
          <select class="form-select pf-role">
            ${roleOptions(row.pjRoleNo)}
          </select>
        </td>

        <td>
          <div class="d-flex align-items-center gap-2 flex-nowrap">
            <select class="form-select pf-chk w-auto">
              ${itemOptions(row.pjChkItemNo)}
            </select>
            <button type="button" class="btn btn-sm btn-outline-secondary pf-help" style="min-width:50px">설명</button>
          </div>
        </td>

        <!-- 시간: 발송완료여도 편집 가능(요구사항) -->
        <td><input type="time" class="form-control pf-start" value="${row.pfStart}"></td>
        <td><input type="time" class="form-control pf-end"   value="${row.pfEnd}"></td>

        <!-- 알림 타입: 발송완료면 배지로 고정 -->
        <td>${notifyCell}</td>

        <!-- 알림 분: 0 또는 발송완료면 비활성 -->
        <td>
          <input type="number" class="form-control pf-notify-mins" min="0" step="1"
                 value="${toNum0(row.notifySetMins)}" ${disabledMins}>
        </td>

        <td>${statusBadge(row.pfStatus)}</td>

        <td><button type="button" class="btn btn-sm btn-danger pf-del">삭제</button></td>
      </tr>
    `;
  }).join("");

  $tbody.innerHTML = html;
  addHelpButtons();
}


// 현재 시각(초)
function nowSeconds() {
  const d = new Date();
  return d.getHours() * 3600 + d.getMinutes() * 60 + d.getSeconds();
}
// "HH:mm" → 초
function hhmmToSeconds(v) {
  if (!v) return null;
  const [h, m] = String(v).split(":").map(n => parseInt(n, 10));
  if (!Number.isFinite(h) || !Number.isFinite(m)) return null;
  return h * 3600 + m * 60;
}
// row 기준 타겟 초 계산 (null: 계산 불가)
function calcTargetSecond(row) {
  const mins = Number(row.notifySetMins) || 0;
  const offset = mins * 60;
  const startSec = hhmmToSeconds(row.pfStart);
  const endSec   = hhmmToSeconds(row.pfEnd);

  switch (Number(row.notifyType)) {
    case 1: return (startSec != null) ? (startSec - offset) : null; // 시작 전
    case 2: return (startSec != null) ? (startSec + offset) : null; // 시작 후
    case 3: return (endSec   != null) ? (endSec   - offset) : null; // 종료 전
    case 4: return (endSec   != null) ? (endSec   + offset) : null; // 종료 후
    case 0: return null; // 미발송은 검증 대상 아님
    default: return null; // -1 등
  }
}
// 발송 가능 검증: target >= now
function isSchedulableNow(row) {
  const t = calcTargetSecond(row);
  if (t == null) return true; // 계산 불가/미발송 등은 통과
  return t >= nowSeconds();
}

/** 번호 다시 채우기(삭제 후) */
function renumber() {
  [...$tbody.querySelectorAll("tr")].forEach((tr, i) => {
    const cell = tr.querySelector("td");
    if (cell) cell.textContent = String(i + 1);
  });
}

// ===== 이벤트 바인딩 =====

/** 상단 버튼(저장/다음) */
function bindTopButtons() {
  // id가 있으면 그걸 쓰고, 없으면 타이틀 영역 첫 번째=저장, 두 번째=다음으로 가정
  const $save = document.querySelector("#btnSave") || document.querySelectorAll(".titleArea .btn.btn-primary")[0];
  const $next = document.querySelector("#btnNext") || document.querySelectorAll(".titleArea .btn.btn-primary")[1];

  $save?.addEventListener("click", onClickSave);
  $next?.addEventListener("click", onClickNext);
}

/** 행 추가 */
function bindAddButton() {
  $btnAdd?.addEventListener("click", () => {
    ROWS.push({
      pfNo: tempId--,          // 신규 임시 음수 ID 음수라서 계속 -- 해줌
      pjRoleNo: 0,
      pjChkItemNo: 0,
      pfStart: "",
      pfEnd: "",
      notifyType: 0,
      notifySetMins: 0,
      pfStatus: 1,
      changeStatus: 1          // 신규 플래그
    });
    render();
  });
}



// 테이블 값 변경/삭제 (이벤트 위임)
function bindTableEvents() {
  $tbody.addEventListener("change", (e) => {
    const tr = e.target.closest("tr");
    if (!tr) return;

    const pfNo = Number(tr.dataset.pfno);
    const row = ROWS.find(r => Number(r.pfNo) === pfNo);
    if (!row) return;

    // 스냅샷(되돌리기용)
    const prev = { ...row };

    // 편집 분기
    if (e.target.classList.contains("pf-role")) {
      row.pjRoleNo = toNum0(e.target.value);

    } else if (e.target.classList.contains("pf-chk")) {
      row.pjChkItemNo = toNum0(e.target.value);

    } else if (e.target.classList.contains("pf-start")) {
      row.pfStart = toHHMM(e.target.value);
      if (row.pfStart && row.pfEnd && cmpHHMM(row.pfEnd, row.pfStart) < 0) {
        alert("종료시간은 시작시간보다 빠를 수 없습니다.");
        Object.assign(row, prev);
        render();
        return;
      }

    } else if (e.target.classList.contains("pf-end")) {
      row.pfEnd = toHHMM(e.target.value);
      if (row.pfStart && row.pfEnd && cmpHHMM(row.pfEnd, row.pfStart) < 0) {
        alert("종료시간은 시작시간보다 빠를 수 없습니다.");
        Object.assign(row, prev);
        render();
        return;
      }

    } else if (e.target.classList.contains("pf-notify-type")) {
      // -1 옵션은 셀렉트에 없으니 여기로 안 들어옴 (발송완료 행은 배지)
      row.notifyType = toNum0(e.target.value);
      const minsEl = tr.querySelector(".pf-notify-mins");
      minsEl.disabled = row.notifyType === 0;
      if (row.notifyType === 0) { row.notifySetMins = 0; minsEl.value = 0; }

    } else if (e.target.classList.contains("pf-notify-mins")) {
      row.notifySetMins = toNum0(e.target.value);

    } else {
      return;
    }

    // === 공통: 현재 시각 기준 발송 불가면 되돌림 ===
    if (!isSchedulableNow(row)) {
      alert("현재 시각 기준으로 알림 발송 시간이 이미 지났습니다. 알림 설정/시간을 다시 선택하세요.");
      Object.assign(row, prev);
      render();
      return;
    }

    if (isExisting(pfNo)) row.changeStatus = 3; // 기존행 수정 플래그
  });
}


/* HH:mm 비교 유틸: a(종료) - b(시작)
   반환값 <0  => a < b (잘못된 종료시간)
            0 => a == b
           >0 => a > b
*/
function cmpHHMM(a, b) {
  // a,b는 "HH:mm" 가정 (toHHMM으로 정리됨)
  const [ah, am] = String(a).split(":").map(n => parseInt(n, 10));
  const [bh, bm] = String(b).split(":").map(n => parseInt(n, 10));
  const aMin = (Number.isFinite(ah) ? ah : 0) * 60 + (Number.isFinite(am) ? am : 0);
  const bMin = (Number.isFinite(bh) ? bh : 0) * 60 + (Number.isFinite(bm) ? bm : 0);
  return aMin - bMin;
}


// 삭제
$tbody.addEventListener("click", (e) => {
  if (!e.target.classList.contains("pf-del")) return;
  const tr = e.target.closest("tr");
  const pfNo = Number(tr.dataset.pfno);
  const row = ROWS.find(r => Number(r.pfNo) === pfNo);
  if (!row) return;

  if (!confirm("삭제하시겠습니까? (저장 후엔 되돌릴 수 없습니다.))")) return;

  // 기존행이면 4(서버 삭제 대상), 신규행이면 2(로컬만 제거)
  row.changeStatus = isExisting(pfNo) ? 4 : 2;
  tr.remove();
  renumber();
});


// ===== 저장/이동 =====

/** 저장 버튼 → creates/updates/deletes 만들고 POST, 응답으로 최신 목록 받아 렌더링 */
async function onClickSave() {
  try {
    const payload = buildSavePayload();

    // 가벼운 검증: 신규/수정에 필수값 존재하는지
    const problems = [];
    payload.creates.forEach((c, i) => {
      if (!c.pjRoleNo)    problems.push(`신규[${i+1}] 역할 미선택`);
      if (!c.pjChkItemNo) problems.push(`신규[${i+1}] 체크리스트 미선택`);
      if (!c.pfStart)     problems.push(`신규[${i+1}] 시작시간 미입력`);
      if (!c.pfEnd)       problems.push(`신규[${i+1}] 종료시간 미입력`);
    });
    payload.updates.forEach((u, i) => {
      if (!u.pfNo)        problems.push(`수정[${i+1}] pfNo 없음`);
      if (!u.pjRoleNo)    problems.push(`수정[${i+1}] 역할 미선택`);
      if (!u.pjChkItemNo) problems.push(`수정[${i+1}] 체크리스트 미선택`);
      if (!u.pfStart)     problems.push(`수정[${i+1}] 시작시간 미입력`);
      if (!u.pfEnd)       problems.push(`수정[${i+1}] 종료시간 미입력`);
    });
    // 문제행이 있으면?
    if (problems.length) {
      alert("저장 불가:\n- " + problems.join("\n- "));
      return;
    }   // if end

    const response = await fetch(API_SAVE(pjNo), {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      // 400 등 에러메시지 보기 좋게
      const txt = await response.text().catch(() => "");
      alert(`저장 실패 (${response.status})\n${txt || ""}`);
      return;
    }

    // 백엔드가 최신 목록 반환하는 것
    const fresh = await response.json();

    // 화면 버퍼 교체와 초기화
    ROWS = (Array.isArray(fresh) ? fresh : []).map((dto) => ({
      pfNo:            Number(dto.pfNo) || 0,
      pjRoleNo:        Number(dto.pjRoleNo) || 0,
      pjChkItemNo:     Number(dto.pjChkItemNo) || 0,
      pfStart:         toHHMM(dto.pfStart || ""),
      pfEnd:           toHHMM(dto.pfEnd || ""),
      notifyType:      Number(dto.notifyType ?? 0),
      notifySetMins:   Number(dto.notifySetMins ?? 0),
      pfStatus:        Number(dto.pfStatus ?? 1),
      changeStatus:    0
    }));

    render();
    alert("저장되었습니다.");
  } catch (e) {
    console.error(e);
    alert("저장 중 오류가 발생했습니다.");
  }
}
// 다음 페이지 이동 
const onClickNext = async () => {
    let result = confirm(`[경고] 저장을 하지 않고 다음 페이지로 이동하시면, 변경된 내용은 삭제되며 복구할 수 없습니다. \n계속 진행하시겠습니까?`)
    if (result == false) { return }

    location.href = `/project/performCheck.jsp?pjNo=${pjNo}`;
} // func end

/** 저장 페이로드 만들기
 * - 신규(changeStatus=1, pfNo<0) → creates
 * - 수정(changeStatus=3, pfNo>0) → updates
 * - 삭제(changeStatus=4, pfNo>0) → deletes
 */
function buildSavePayload() {
  const creates = [];
  const updates = [];
  const deletes = [];

  for (const r of ROWS) {
    // 신규
    if (r.changeStatus === 1 && r.pfNo < 0) {
      creates.push({
        pjRoleNo:       r.pjRoleNo,
        pjChkItemNo:    r.pjChkItemNo,
        pfStart:        r.pfStart,       // "HH:mm"
        pfEnd:          r.pfEnd,         // "HH:mm"
        notifyType:     r.notifyType,
        notifySetMins:  r.notifySetMins
      });
      continue;
    }

    // 수정
    if (r.changeStatus === 3 && r.pfNo > 0) {
      updates.push({
        pfNo:           r.pfNo,
        pjRoleNo:       r.pjRoleNo,
        pjChkItemNo:    r.pjChkItemNo,
        pfStart:        r.pfStart,
        pfEnd:          r.pfEnd,
        notifyType:     r.notifyType,
        notifySetMins:  r.notifySetMins
      });
      continue;
    }

    // 삭제(기존행만 서버 전송)
    if (r.changeStatus === 4 && r.pfNo > 0) {
      deletes.push(r.pfNo);
    }
  }

  return { creates, updates, deletes };
}

/** 

0 : 깨끗한 상태(서버와 동기화 완료)

1 : 신규(클라이언트에서만 생김, 서버에 아직 없음)

2 : 신규였는데 화면에서만 삭제됨(서버에 요청 불필요)

3 : 기존행 수정됨(서버에 update 필요)

4 : 기존행 삭제됨(서버에 delete 필요) 

*/

// [ADD] ======================= 설명 모달 관련 유틸/이벤트 =======================

/** render() 이후, 각 행의 체크리스트 select(.pf-chk) 옆에 '설명' 버튼을 동적으로 삽입 */
function addHelpButtons() {
  $tbody.querySelectorAll("tr").forEach(tr => {
    const sel = tr.querySelector(".pf-chk");
    if (!sel) return;

    // 이미 버튼이 있다면 중복 삽입하지 않음
    if (tr.querySelector(".pf-help")) return;

    const btn = document.createElement("button");
    btn.type = "button";
    btn.className = "btn btn-sm btn-outline-secondary pf-help";
    btn.textContent = "설명";

    // select 바로 뒤에 삽입
    sel.insertAdjacentElement("afterend", btn);
  });
}

/** 모달 인스턴스 초기화 */
function initDescModal() {
  const el = document.getElementById("descModal");
  if (!el) return; // JSP에 모달 마크업이 없다면 아무 것도 하지 않음
  if (!descModal) {
    descModal = new bootstrap.Modal(el, { backdrop: "static" });
  }
}

/** 설명 버튼 클릭 이벤트 위임 */
function bindHelpEvents() {
  $tbody.addEventListener("click", (e) => {
    const btn = e.target.closest(".pf-help");
    if (!btn) return;

    const tr = e.target.closest("tr");
    const sel = tr?.querySelector(".pf-chk");
    const itemId = sel?.value;

    if (!itemId || Number(itemId) === 0) {
      alert("먼저 체크리스트 항목을 선택하세요.");
      return;
    }

    openDescModal(itemId);
  });
}

/** 모달 오픈: ITEM_LIST에서 itemId를 찾아 제목/본문을 채움 */
function openDescModal(itemId) {
  initDescModal();
  if (!descModal) return;

  // ITEM_LIST에서 해당 항목 찾기 (백엔드가 pjHelpText를 내려줘야 함)
  const item = ITEM_LIST.find(x => String(x.pjChkItemNo) === String(itemId));
  const title = item?.pjChklTitle || "(제목 없음)";
  const help  = (item?.pjHelpText && String(item.pjHelpText).trim())
    ? item.pjHelpText
    : "설명(도움말)이 등록되어 있지 않습니다.";

  if ($descTitle()) $descTitle().textContent = title;
  if ($descBody())  $descBody().textContent  = help;

  descModal.show();
}

// ======================================================================
