# 🎭 공연5분전 - Java SpringBoot Project

**팀명**: 3조  
**팀원**: 신연우, 옹태경, 최동진  
**프로젝트 날짜**: 2025.09.12

---

## 📌 프로젝트 개요

출장 공연 특성상 돌발 상황(연락두절, 교통 지연 등)으로 인해 관리자 스트레스가 극대화되는 문제를 해결하기 위해, **단기 프로젝트 인력 관리에 특화된 협업 플랫폼**을 개발하였습니다.

### 🎯 기획 목표

- 현장 상황을 직관적으로 파악하고 빠르게 대응할 수 있는 협업 툴
- 단기 프로젝트 인력 관리의 효율성 향상
- 통합된 소통 플랫폼 구축 (대면, 통화, 문자, 카카오톡 등 혼선 제거)
- 프로젝트별 교통편, 준비물, 동행 인력 등 사전 준비 자동화

---

## 🧭 프로젝트 일정

| 주차 | 주요 작업 |
|------|-----------|
| 1주차 | 주제 선정, 기획서 작성 |
| 2주차 | 기능 설계, 프로토타입 작성, 백엔드 구현 |
| 3주차 | 프론트엔드 구현, 테스트 및 마무리, 발표 준비 |

---

## 👥 팀원 역할

- **신연우**: Git 취합, 체크리스트/템플릿 관리, 채팅 기능, JWT 토큰 및 캐시 처리
- **옹태경**: 프로젝트 업무 배정, CSV 암호화, Excel 다운로드, 공통 CRUD 인터페이스 설계
- **최동진**: PDF 출력, 프로젝트 타임라인 시각화, DashboardDto 구성

---

## 🛠️ 기술 스택

### Backend
- Java 17
- Spring Boot
- JPA, Lombok
- WebSocket
- Caffeine Cache
- Jasypt, bcrypt, JJWT

### Frontend
- HTML, CSS, JavaScript
- Bootstrap
- Noto Sans KR

### 외부 API 및 라이브러리
- Kakao Map API
- OpenPDF (PDF 생성)
- Apache POI (Excel 생성)
- OpenCSV (CSV 생성)
- Jakarta Mail API (이메일 전송)

---

## 🧩 시스템 설계

- **메뉴 구조**: 프로젝트 관리, 템플릿 관리, 업무 배정, 채팅, 알림, 계정 관리
- **DB 설계**: ERD 기반 PK/FK 구조, 사용자/사업자/프로젝트/근무자 등 도메인 분리
- **기능 흐름도**: 업무 배정 → 체크리스트 → PDF/Excel 출력

---

## 🔍 주요 기능

- 템플릿 관리 및 재사용
- 프로젝트 생성 및 인력 배정
- 업무 체크리스트 관리
- PDF/Excel 다운로드 기능
- 채팅 및 알림 시스템
- 사용자/사업자 계정 관리
- JWT 기반 인증 및 보안 처리

---

## 🧪 코드 리뷰 & 트러블슈팅

### JWT 토큰 처리
- JJWT 라이브러리로 토큰 생성 및 서명
- Caffeine Cache로 중복 사용 방지 및 TTL 관리
- 이메일 인증 시 쿨타임 설정

### Excel 출력
- Apache POI로 템플릿 기반 Excel 생성
- 프로젝트명 + 날짜 기반 파일명 자동 생성

### PDF 출력
- OpenPDF로 체크리스트 PDF 생성
- 시간 순서대로 정렬된 근무자 리스트 출력
- 테마 색상 및 로고 삽입으로 브랜드 강조

### CRUD 최적화
- Generic Repository + AbstractService 도입
- 공통 CRUD 로직 통합으로 코드 중복 제거

### JS 전역 변수 문제 해결
- `Promise` 기반 fetch 순서 보장
- 전역 변수 null 문제 해결

---

## 🚀 향후 개선 방향

- **근로자-사업체 매칭 시스템**: 이력서 업로드 및 공고 신청 기능
- **사업체 근로자 관리**: 계약된 근로자 관리 및 배치 편의성 향상
- **상호 평가 시스템**: 근로자와 사업체 간 평점 부여
- **권한 부여 기능**: 관리자/매니저 역할 분리
- **메시지 알림 기능**: 프로젝트 시작 전 이메일 및 채팅 알림
- **GPS 기반 안내**: 근로자의 현재 위치 기반 이동 경로 안내

---

## 🙏 감사 인사

**신연우, 옹태경, 최동진**  
Java SpringBoot Project  
2025.09.12



---



# 🎭 공연5분전 (Stand by Five Minutes Ago)
> 출장·공연 **업무관리 생산성 향상 툴** — 현장 인력/업무/이동을 한 화면에서 계획·배치·기록·내보내기까지

![Java](https://img.shields.io/badge/Java-21+-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JSP](https://img.shields.io/badge/View-JSP%2FJS%2FBootstrap-7952B3)
![Security](https://img.shields.io/badge/Security-JJWT%20%7C%20HS256-important)
![Docs](https://img.shields.io/badge/Docs-OpenPDF-orange)
![Excel](https://img.shields.io/badge/Excel-Apache%20POI-1E8449)
![Routing](https://img.shields.io/badge/Routing-GraphHopper%20%2B%20TransitAPI-2E86C1)

---

## 🔎 TL;DR
행사·공연 현장에서 **인력 관리(역할 템플릿)** → **업무 체크리스트** → **이동 경로 안내** → **PDF/Excel 보고서 출력**까지 한 번에 처리하는 웹 애플리케이션입니다.  
JWT(JJWT) 기반의 보안 토큰, 이메일 인증/안내, Excel/PDF 내보내기, 경로 안내(차량·대중교통)를 지원합니다.

---

## 🗂️ 목차
- [프로젝트 소개](#-프로젝트-소개)
- [기간/일정](#-기간일정-2025-08-22--2025-09-12-16일)
- [주요 기능](#-주요-기능-by-module--by-owner)
- [시스템 아키텍처](#-시스템-아키텍처)
- [기술 스택](#-기술-스택)
- [빠른 시작](#-빠른-시작-getting-started)
- [보안 & 인증(JJWT)](#-보안--인증-jjwt)
- [데이터 내보내기(Excel/PDF)](#-데이터-내보내기-excelpdf)
- [이동 경로 안내](#-이동-경로-안내-graphhopper--대중교통-api)
- [프로젝트 구조](#-프로젝트-구조)
- [API 개요](#-api-개요-일부)
- [팀 & 역할](#-팀--역할)
- [Roadmap](#-roadmap)
- [라이선스](#-라이선스)
- [연락처](#-연락처)

---

## 📘 프로젝트 소개
이전 직장에서 **출장 행사 업무**를 맡으며, 외부 현장에서 **인원과 업무를 동시에 관리**하는 일이 어렵다는 문제를 체감했습니다.  
**공연5분전**은 이러한 문제를 해결하기 위해 기획된 **현장 업무 통합 관리 도구**입니다.
- **프로젝트 단위 관리**: 일정·장소·클라이언트·요청사항을 한 번에 등록
- **역할 템플릿**: 현장 별 역할 구성을 템플릿으로 만들어 재사용
- **체크리스트**: 업무 항목의 배정·진척도·담당자 확인
- **경로 안내**: 팀원 이동 동선(차량·대중교통) 제공
- **산출물 출력**: 운영 결과를 Excel/PDF로 회수·보고

---

## 📅 기간/일정 (2025-08-22 ~ 2025-09-12, 16일)
| 단계 | 일정 | 기간 | 산출물 |
|---|---|---:|---|
| 1차 설계 | 2025-08-22 ~ 2025-08-29 | 8일 | 요구사항·ERD·화면흐름 |
| 1차 코드 | 2025-08-29 ~ 2025-09-05 | 8일 | 기본 CRUD/페이지 스켈레톤 |
| 2차 설계 | 2025-09-01 ~ 2025-09-05 | 5일 | 보완 설계(보안/성능/UX) |
| 2차 코드 | 2025-09-04 ~ 2025-09-12 | 9일 | 기능 고도화, 내보내기, 경로 |
| 발표 준비 | 2025-09-09 ~ 2025-09-12 | 4일 | 발표 자료/데모 스크립트 |

> 상세 일정은 팀 내부 노션/GitHub Projects 기준으로 운영

---

## ✨ 주요 기능 (by module & by owner)
- **공통**: 프로젝트 실행/배포, 기본 레이아웃 및 내비게이션
- **신연우**: 사용자단(회원/로그인/세션), **JJWT 기반 보안 토큰**, 이메일 서비스
- **옹태경**: **역할 템플릿 모듈**, **Apache POI**로 Excel 내보내기
- **최동진**: **체크리스트 모듈**, **OpenPDF**로 PDF 출력, **GraphHopper + 대중교통 API**로 이동 경로

**핵심 시나리오**
1) 프로젝트 생성 → 2) 역할 템플릿 적용/수정 → 3) 체크리스트 배정 → 4) 이동 경로 확인 → 5) Excel/PDF 보고서 출력

---

## 🏗️ 시스템 아키텍처
```text
[JSP/JS/Bootstrap]  ──>  [Spring MVC Controller]  ──>  [Service Layer]  ──>  [DAO/Repository]  ──>  [MySQL 8.0]
                           │            │
                           │            ├── [EmailService: SMTP]
                           │            ├── [JwtUtil (JJWT, HS256)]
                           │            ├── [Caffeine Cache (선택)]
                           │            ├── [Apache POI / OpenPDF]
                           └────────────└── [GraphHopper & Transit API]
```

- 인증: **JWT(JJWT, HS256)** 기반 (만료/목적별 토큰, jti 중복 방지)
- 메일: SMTP 기반 HTML 메일 전송
- 내보내기: Excel(POI), PDF(OpenPDF)
- 경로: GraphHopper(차량) + 대중교통 API(환승/도보)

---

## 🛠 기술 스택
- **Backend**: Java 21+, Spring Boot 3.5.x, Gradle 8.x
- **DB**: MySQL 8.0
- **View**: JSP, Bootstrap 5, Vanilla JS/Fetch
- **Security**: JJWT (HS256), HttpSession (세션 보조)
- **I/O**: Apache POI (Excel), OpenPDF (PDF)
- **Routing**: GraphHopper + 대중교통 API
- **Etc.**: Lombok, Validation, (옵션) Caffeine Cache

---

## ⚡ 빠른 시작 (Getting Started)

### 1) 사전 준비
- JDK 21+, Gradle 8.x, MySQL 8.0
- SMTP 계정(발신 전용), JWT 비밀키

### 2) 환경 변수 / 설정
`src/main/resources/application.properties` 예시:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/standby5m?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.username=your_user
spring.datasource.password=your_password

spring.mail.host=smtp.yourmail.com
spring.mail.port=587
spring.mail.username=your_account
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

app.jwt.secret=change_this_to_long_random_secret
app.jwt.expiry-minutes=30
```

### 3) DB 스키마
```sql
CREATE DATABASE IF NOT EXISTS standby5m DEFAULT CHARACTER SET utf8mb4;
```

### 4) 실행
```bash
# clone
git clone https://github.com/your-org/stand-by-five-minutes-ago.git
cd stand-by-five-minutes-ago

# run
./gradlew bootRun
# or
./gradlew clean build && java -jar build/libs/*.jar
```

### 5) 접속
- http://localhost:8080

---

## 🔐 보안 & 인증 (JJWT)

- **알고리즘**: HS256
- **권장 Claims**
    - `sub`: 이메일 또는 사용자 식별자
    - `userNo`: 내부 PK
    - `jti`: 토큰 식별자(재사용/중복 방지용)
    - `purpose`: 토큰 목적(`AUTH`, `PW_RESET`, `EMAIL_VERIFY` 등)
    - `iat`/`exp`: 발급/만료 시각
- **원칙**
    - 목적별 **분리된 토큰** 사용 (인증/비번재설정/이메일검증 분리)
    - `jti`를 캐시/DB에 기록하여 **1회성 보장(Replay 방지)**
    - 만료·서명 검증 실패 시 즉시 무효 처리
- **예시** (생성 로직 스니펫)
```java
String jwt = Jwts.builder()
    .subject(email)
    .claim("userNo", userNo)
    .claim("purpose", "PW_RESET")
    .id(UUID.randomUUID().toString()) // jti
    .issuedAt(Date.from(Instant.now()))
    .expiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
    .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
    .compact();
```

---

## 🧾 데이터 내보내기 (Excel/PDF)
- **Excel (Apache POI)**: 역할 템플릿/근무편성/출근부 등 스프레드시트 출력
- **PDF (OpenPDF)**: 현장 체크리스트, 배치도, 요약 리포트 PDF 변환
- 페이지 번호, 표 스타일링, 로고/QR 등 추가 커스터마이즈 가능

---

## 🧭 이동 경로 안내 (GraphHopper + 대중교통 API)
- **차량 경로**: 현장 간 이동 시간/거리 산출
- **대중교통 경로**: 출발지/도착지 환승 안내(도보 포함)
- **활용 예**: “스태프 집결 → 리허설 장소 → 공연장 본동” 동선 작성 및 공유

---

## 📁 프로젝트 구조
```text
src
├─ main
│  ├─ java/five_minutes
│  │  ├─ AppStart.java
│  │  ├─ controller/...
│  │  ├─ service/...
│  │  ├─ model/dao/...
│  │  ├─ model/dto/...
│  │  ├─ util/ (JwtUtil, PhoneNumberUtil, EmailSendFormat ...)
│  │  └─ config/...
│  └─ resources
│     ├─ application.properties
│     ├─ static/ (css, js, img, sql ...)
│     └─ templates/ or webapp/ (JSP)
└─ test/...
```

---

## 📡 API 개요 (일부)
> 실제 엔드포인트는 구현/보안 정책에 따라 조정될 수 있습니다.

### Users
| 메서드 | 경로 | 설명 |
|---|---|---|
| POST | `/api/users/signup` | 회원가입 |
| POST | `/api/users/login` | 로그인(JWT 발급) |
| GET  | `/api/users/me` | 내 정보 조회 |
| POST | `/api/users/password/reset-link` | 비밀번호 재설정 메일 발송 |
| POST | `/api/users/password/reset` | 비밀번호 재설정(토큰 검증) |

### Role Template
| 메서드 | 경로 | 설명 |
|---|---|---|
| POST | `/api/roles/templates` | 역할 템플릿 생성 |
| GET  | `/api/roles/templates` | 템플릿 목록 |
| PUT  | `/api/roles/templates/{id}` | 수정 |
| DELETE | `/api/roles/templates/{id}` | 삭제 |
| POST | `/api/projects/{pjNo}/roles/apply-template/{tplId}` | 프로젝트에 템플릿 적용 |


---

## 👥 팀 & 역할
| 팀 | 이름 | 담당 |
|---|---|---|
| 3조 | **신연우** | 사용자단, **보안 토큰(JJWT)**, 이메일 |
| 3조 | **옹태경** | **역할 템플릿**, **Excel 내보내기(POI)** |
| 3조 | **최동진** | **체크리스트**, **PDF(OpenPDF)**, **경로(GraphHopper+대중교통)** |

---

## 🧭 Roadmap
- [ ] 역할 템플릿 → 프로젝트 복사 로직 최적화(충돌/중복 처리)
- [ ] 체크리스트 드래그&드롭 정렬(Seq 업데이트, Undo/Redo)
- [ ] 대중교통 경로 ETA/비용 비교 탭
- [ ] 관리자 대시보드(근무자 평판/출근부/알림)
- [ ] 멀티테넌시(사업자 계정, 권한·범위 분리)
- [ ] 배포 자동화(GitHub Actions)

---

> _문서·이미지(스크린샷/ERD 등)는 `/docs` 경로에 추가해 주세요._
