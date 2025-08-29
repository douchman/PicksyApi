# Picksy API 

- 취향 기반 선택 토너먼트를 발전시켜 ** 선호도 대결 플랫폼 Picksy **의 서버 어플리케이션
- 사용자가 생성한 대결 토너먼트의 진행, 결과 통계 집계, 인증/인가, 미디어 업로드 등 핵심 로직을 담당


## 🚀 프로젝트 소개 
- Picksy 는 단순한 "이상형 월드컵" 을 넘어, ** 데이터 기반 선호도 분석** 을 제공하는 플랫폼 입니다.
- 사용자는 자신만의 주제를 만들고, 엔트리를 등록해 토너먼트를 진행하며, 그 결과를 기반으로 **승률·랭킹·매치업 통계**를 확인할 수 있습니다.


## 🛠️ 기술스택
- **Backend**: Spring Boot, JPA(Hibernate), MapStruct  
- **Database**: MySQL (RDS)  
- **Cache**: Redis (회원/엔트리 캐싱)  
- **Auth**: Spring Security (JWT)  
- **Batch**: Spring Batch (주기적 랭킹 계산)  
- **Infra**: AWS EC2, Docker Compose, GitHub Actions (CI/CD)  
- **Storage**: AWS S3 (Presigned URL 기반 업로드)  
- **Monitoring**: Prometheus, Grafana, Elasticsearch, Kibana


## ✨ 주요 기능
- **대결 관리**: 토너먼트 생성, 엔트리 등록
- **통계 집계**: 승률, 랭킹, 매치업 분석 (Spring Batch)  
- **인증/인가**: JWT 기반 토큰 인증  
- **캐싱 최적화**: Redis 기반 회원/데이터 캐싱으로 DB 부하 감소  
- **미디어 관리**: S3 Presigned URL로 안전한 업로드/다운로드  
- **로그 & 모니터링**: ELK + Prometheus로 운영 가시성 확보


- ## 📌 개발 경과
- Redis 캐싱으로 DB 조회 부하 감소
- Spring Batch 로 주기적 엔트리 랭킹 산출 자동화
- S3 PreSigned URL 적용으로 안정적인 미디어 업로드


## 📚 향후 계획
- 신규 컨텐츠 ** 밸런스 게임**
- 소셜 연동
- 대결 관련 알림 시스템
- 대결 제작자 랭킹 페이지
 

## 👨‍💻 제작
- 박상현 - Backend Developer
- 기획, 서버/DB 설계, 인프라 및 모니털이 환경 구축

