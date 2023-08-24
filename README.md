![header](https://capsule-render.vercel.app/api?type=waving&color=1DAE86&height=250&section=header&text=산학연계%20프로젝트%20우수상&fontSize=60&animation=fadeIn&fontAlignY=32&desc=2023%20동국대학교%20웹%20개발자%20양성%20장학%20과정&descAlignY=51&descAlign=70)

# 여행 비디오 커머스 플랫폼 WANDERLUST

## 서비스 구현 스택
Front-End
Back-End

## 담당 파트 : WANDERLUST CMS(컨텐츠 관리 시스템) WEB

## 🏫 연계 기관 : 모바일 앱 협동 조합

- 기업 요구사항
  - 클라우드 웹컨텐츠 서비스
    - 사용자 인증 기능 개발
    - 컨텐츠 등록 관리 기능 개발
    - 컨텐츠 스트리밍 기능 개발

Why, 여행 전문 비디오 커머스 플랫폼?
- 코로나 19 엔데믹 이후, 여행 수요 급증
- 비디오 커머스 산업 급성장
- 유튜버와 여행업체 협업 증가
- 영상 조회수 대비 광고 판매 실적 저조

제안
1. AI를 활용한 추천 여행 영상 제공
2. 사용 기록 기반 개인 추천 영상 제공
3. 구매 성사 기반의 광고 비즈니스 모델

해결 방안
1. 영상 인코딩 및 CDN
   - AWS CloudFron, S3
   - FFMPEG, Video.js(React Native) 라이브러리
2. 사용자를 위한 개인화 영상 제공
   - GPT API 활용, 자연어 기반 추천 영상 제공
   - TF-IDF 알고리즘 활용 개인화 영상 제공
3. 판매자를 위한 통계 제공
   - Chart.js 라이브러리 활용 데이터 시각화
   - Redis 활용 통계 등록 중복 방지 구현
4. UX 향상을 위한 성능 개선
   - 영상 인코딩 서버 분리 배포
   - SockJS Stomp JS 활용 동영상 진행도 시각화

### CMS WEB UI 디자인
![Wanderlust 로그인](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/e13285ae-d0d7-4920-88f6-5a283519f375)
![Wanderlust 회원가입](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/a5bd621f-b0a2-4f75-992c-dd1c42d6266f)
![Wanderlust 비밀번호 찾기](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/0891e9db-27e5-4a2c-9e1b-8ecb09e91490)
![Wanderlust 통계 페이지](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/96a6e547-1861-4d4f-8c85-6305709caf8e)
![Wanderlust 업로드 페이지](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/cb20436a-8de9-4098-b7c3-7c8f493bfba3)
![Wanderlust 관리 페이지](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/3794aae7-c9f9-4d35-868a-ef9c3945eaf2)
![Wanderlust 회원 정보 수정](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/dc5b3d1f-627d-468e-be3d-3bed07aea6ae)


### 시스템 구조도
![WANDERLUST 시스템 구조도](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/7709a13e-3c18-4e01-8801-4e7a6b10d392)


<hr/>

### 배포 링크
http://www.roberniro-projects.xyz/

### 유튜브 시연
https://www.youtube.com/watch?v=EyDKypFDwgE

### 발표 자료
https://drive.google.com/drive/folders/1bAkz9T9r50oekLjm5VMDQYYh0wHFJowW

