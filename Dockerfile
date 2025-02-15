FROM openjdk:17-jdk-alpine

## Change directory
WORKDIR /app

## Create non-root user
RUN adduser -D user_service
RUN chown -R user_service:user_service /app
USER user_service

## Copy war file and run app
COPY target/user-service-0.0.1-SNAPSHOT.war user_service.war
ENTRYPOINT ["java","-jar","user_service.war"]

## Expose port 8080
EXPOSE 8080