FROM openjdk:17-jdk-alpine

# Change directory
WORKDIR /app

# Download the OTel Java Agent
ARG OTEL_AGENT_VERSION=2.8.0
ARG OTEL_AGENT_JAR=/app/opentelemetry-javaagent.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar $OTEL_AGENT_JAR

# Copy war file
COPY target/user-service-0.0.1-SNAPSHOT.war user-service.war

## Create non-root user
RUN adduser -D user_service
RUN chown -R user_service:user_service /app
USER user_service

# Run app
ENTRYPOINT ["sh","-c","java -javaagent:opentelemetry-javaagent.jar -jar -Dspring.config.location=${CONFIG_PATH} user-service.war"]

## Expose port 8080
EXPOSE 8080