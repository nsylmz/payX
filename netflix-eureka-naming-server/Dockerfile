FROM openjdk:8-jdk-alpine
RUN addgroup -S nsylmz && adduser -S nsylmz -G nsylmz
USER nsylmz:nsylmz
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} netflix-eureka-naming-server.jar
ENTRYPOINT ["java","-jar","/netflix-eureka-naming-server.jar"]