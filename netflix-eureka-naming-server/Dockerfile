FROM openjdk:8-jdk-alpine
RUN addgroup -S nsylmz && adduser -S nsylmz -G nsylmz
USER nsylmz:nsylmz
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]