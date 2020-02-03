FROM openjdk:8-jdk-alpine
RUN addgroup -S nsylmz && adduser -S nsylmz -G nsylmz
USER nsylmz:nsylmz

ARG JAR_FILE=target/*.jar
ARG EUREKA_URL=http://172.17.0.4:8761/eureka/
ENV eureka.client.serviceUrl.defaultZone ${EUREKA_URL}

COPY ${JAR_FILE} spring-cloud-config-server.jar
ENTRYPOINT ["java","-jar","/spring-cloud-config-server.jar"]