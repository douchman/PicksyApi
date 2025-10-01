# Picksy API 
### 🚀 프로젝트 소개 
- Picksy 는 단순한 "이상형 월드컵" 을 넘어, **데이터 기반 선호도 분석** 을 제공하는 플랫폼
- 사용자는 자신만의 주제를 만들고, 엔트리를 등록해 토너먼트를 진행하며, 그 결과를 기반으로 **승률·랭킹·매치업 통계**를 확인


### 🛠️ 스택
- **Language** : Java 17
- **Backend**: Spring Boot v3.4.0, JPA(Hibernate), MapStruct  
- **Database**: H2(dev), AWS RDS : MySQL 8.4.5 (prod)  
- **Cache**: Redis (회원/엔트리 캐싱)  
- **Auth**: Spring Security (JWT)  
- **Batch**: Spring Batch (주기적 랭킹 계산)  
- **Infra**: AWS EC2, Docker Compose, GitHub Actions (CI/CD)  
- **Storage**: AWS S3 (Presigned URL 기반 업로드)  
- **Monitoring**: Prometheus, Grafana, Elasticsearch, Kibana


### ✨ 주요 기능
- **대결 관리**: 토너먼트 생성, 대결주제/엔트리 등록
- **통계 집계**: 승률, 랭킹, 매치업 분석 (Spring Batch)  
- **인증/인가**: JWT 기반 토큰 인증  
- **캐싱 최적화**: Redis 기반 회원/데이터 캐싱으로 DB 부하 감소  
- **미디어 관리**: S3 Presigned URL 기반 미디어 업로드/조회
- **로그 & 모니터링**: ELK + Prometheus


### 📚 향후 계획
- 신규 컨텐츠 **밸런스 게임**
- 소셜 연동
- 대결 관련 알림 시스템
- 대결 제작자 랭킹
 

### 👨‍💻 제작
- 박상현 - Backend Developer
- 기획, API/DB 설계 및 구현, 인프라 및 모니터링 구축

