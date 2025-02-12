##Builder Image
##FROM maven:3.8.1-openjdk-15 AS builder
##COPY src /src
##COPY pom.xml pom.xml
##RUN mvn clean package

FROM openjdk:15.0.2-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

