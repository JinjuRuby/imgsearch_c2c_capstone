#  SnapShopping - 이미지 기반 C2C 중고거래 플랫폼

> 사진 한 장으로 원하는 상품을 쉽게 찾는 이미지 검색 기반 중고거래 플랫폼

##  프로젝트 소개

SnapShopping은 텍스트 대신 이미지를 기반으로 상품을 검색할 수 있는 **C2C 중고거래 웹 플랫폼**입니다.  
기존 키워드 검색의 한계를 넘어, **디자인**과 **색상**을 직관적으로 탐색할 수 있는 새로운 거래 경험을 제공합니다.

-  개발 기간: 2025년 3월 ~ 6월
- 개발자: 개인 프로젝트 (1인 개발)

---

## 주요 기능

| 기능 | 설명 |
|------|------|
| 이미지 기반 검색 | 업로드한 이미지와 유사한 상품을 검색 (CLIP + Cosine Similarity) |
| 색상 기반 필터링 | 이미지에서 추출한 대표 색상 기반 필터링 (ColorThief) |
| 상품 업로드/삭제 | 이미지와 설명, 카테고리 포함 상품 등록 및 삭제 |
| 포인트 시스템 | 가상 포인트 충전 및 결제, 판매자 수익 적립 |
| 댓글 기능 | 상품에 대한 댓글 및 대댓글 작성, 수정, 삭제 |
| 마이페이지 | 내 상품, 내 거래 내역, 포인트 관리 |
| 인증/인가 | 회원가입, 로그인, 탈퇴, 이메일 중복 확인 |

---

## 기술 스택

### Frontend
- React
- Bootstrap
- Axios

### Backend
- Spring Boot
- Spring Data JPA
- MySQL
- REST API

### AI 이미지 분석 서버
- Python (FastAPI)
- OpenAI CLIP (이미지 임베딩 추출)
- ColorThief (대표 색상 추출)
- Cosine Similarity 계산

---

## 프로젝트 구조

```
snapshopping/
├── frontend/                # React 프로젝트
├── backend/                 # Spring Boot 프로젝트
└── imgsearch/               # Python 서버 (CLIP + ColorThief)
```

---

## 이미지 검색 예시

> 사용자가 이미지를 업로드하고, 유사한 디자인의 중고상품을 빠르게 탐색할 수 있습니다.

<!-- 예시 이미지가 있다면 아래 URL을 교체하세요 -->
![search-example](https://private-user-images.githubusercontent.com/119550060/464037542-b570b856-d2ac-4651-9dba-aac3d92e01fb.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTIwNDQ2NjgsIm5iZiI6MTc1MjA0NDM2OCwicGF0aCI6Ii8xMTk1NTAwNjAvNDY0MDM3NTQyLWI1NzBiODU2LWQyYWMtNDY1MS05ZGJhLWFhYzNkOTJlMDFmYi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNzA5JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDcwOVQwNjU5MjhaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0yMzA3N2UwMzE2OTg1MTNiZTJmODVhZmUwYjRlMTYzODBkYTA0YjlhOGVkNDQ2MThjODAzNzFjZWU2OWZjNTljJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.n2nRTTB-GEFCYcH_oroqWdY8HTBvHEsCtKkovE42VC4)

---

## 🚀 실행 방법

### Backend (Spring Boot)

```bash
cd backend
./gradlew build
java -jar build/libs/your-app.jar
```

### Frontend (React)

```bash
cd frontend
npm install
npm start
```

### AI 서버 (Python)

```bash
cd imgsearch
pip install -r requirements.txt
uvicorn main:app --reload
```

---

## 📌 향후 개선사항

- 인기순 / 최신순 정렬 기능 추가
- 실 결제 API 연동
- 관리자 페이지 도입
- 모바일 최적화
