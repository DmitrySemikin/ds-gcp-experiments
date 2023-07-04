FROM gradle:8.1.1-jdk17-alpine as builder

WORKDIR /home/gradle/project
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# TODO: Probably add "skip tests" to reduce build cost if executed in the cloud
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /home/gradle/project/build/libs/helloworld-0.0.1-SNAPSHOT.jar /helloworld.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/helloworld.jar"]
