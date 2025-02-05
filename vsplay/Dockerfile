# 베이스 이미지 선택 (Java 17 사용 예시)
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app/vsplay-api

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/vsplay.jar vsplay-api.jar

# 컨테이너 실행 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "vsplay-api.jar"]
