# Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew ./gradlew
COPY gradle/wrapper ./gradle/wrapper
RUN chmod +x ./gradlew

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon --version

COPY . .

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew clean bootJar --no-daemon

# Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
