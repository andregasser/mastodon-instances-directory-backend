FROM gradle:7.5.1-jdk17-alpine as builder
COPY . /home/gradle/
RUN mkdir /app && \
    cd /home/gradle && \
    gradle build --no-daemon

FROM azul/zulu-openjdk-alpine:17.0.2
RUN mkdir /app
COPY --from=builder /home/gradle/build/libs/*.jar /app/mastodon-instances-directory-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/mastodon-instances-directory-backend.jar"]