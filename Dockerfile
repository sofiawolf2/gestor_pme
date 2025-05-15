
FROM amazoncorretto:21.0.0-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8005

ENTRYPOINT ["java", "-jar", "app.jar"]