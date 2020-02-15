FROM openjdk:8-jdk-alpine
RUN addgroup -S nsylmz && adduser -S nsylmz -G nsylmz
USER nsylmz:nsylmz

ARG JAR_FILE=target/*.jar
ARG ACTIVE_PROFILE=prod
ARG CLOUD_CONFIG_URI=http://172.17.0.3:8888
ARG EUREKA_URL=http://172.17.0.4:8761/eureka/

ENV spring.profiles.active ${ACTIVE_PROFILE}
ENV spring.cloud.config.uri ${CLOUD_CONFIG_URI}
ENV eureka.client.serviceUrl.defaultZone ${EUREKA_URL}

COPY ${JAR_FILE} accountService.jar
ENTRYPOINT ["java","-jar","/accountService.jar"]