    # Build stage
    FROM maven:3.8.5-openjdk-17 AS build
    COPY . .
    RUN mvn clean package -DskipTests
    
    # Package stage
    FROM openjdk:17-jdk-slim
    COPY --from=build /target/secure-bank-0.0.1-SNAPSHOT.jar secure-bank.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "secure-bank.jar"]