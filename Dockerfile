FROM openjdk:17-jdk-alpine

# Change directory
WORKDIR /app

# Copy war file
COPY target/user-service-0.0.1-SNAPSHOT.war user-service.war

## Create non-root user
RUN adduser -D user_service
RUN chown -R user_service:user_service /app
USER user_service

# Run app
ENTRYPOINT ["sh","-c","java -jar -Dspring.config.location=${CONFIG_PATH} user-service.war"]

## Expose port 8080
EXPOSE 8080