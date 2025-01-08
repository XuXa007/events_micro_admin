FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

ENV GRADLE_VERSION=8.11.1
RUN apt-get update && apt-get install -y curl unzip \
    && curl -sL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip -d /opt/ \
    && rm gradle-${GRADLE_VERSION}-bin.zip
ENV PATH="/opt/gradle-${GRADLE_VERSION}/bin:${PATH}"

COPY . .
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jdk AS runtime
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]