FROM openjdk:18-jdk-slim
WORKDIR /app
COPY deploy/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]