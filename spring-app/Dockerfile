# 1. 빌드 단계 (Gradle을 사용하여 JAR 파일 생성)
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# gradlew에 실행 권한을 주고 빌드 진행
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# 2. 실행 단계 (가벼운 JDK 환경에서 실행)
FROM openjdk:17-jdk-slim
EXPOSE 8080
# 빌드 단계에서 생성된 jar 파일만 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]