## Builder Image
##FROM maven:3.8.1-openjdk-17 AS builder
##COPY src /src
##COPY pom.xml pom.xml
##RUN mvn clean package

##FROM openjdk:15-jdk-alpine
##RUN addgroup -S spring && adduser -S spring -G spring
##USER spring:spring
##ARG JAR_FILE=target/*.jar
##COPY --from=builder ${JAR_FILE} app.jar
##ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:15.0-buster
##RUN addgroup -S spring && adduser -S spring -G spring
##USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]