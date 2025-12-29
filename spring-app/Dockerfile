# 1. 빌드 단계
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# 2. 실행 단계 (이 부분을 수정했습니다)
FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]