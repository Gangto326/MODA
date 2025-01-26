FROM eclipse-temurin:17-jdk as builder
WORKDIR /build
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:17-jdk
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]