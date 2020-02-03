docker pull mongo:4.2.3 && docker run --name mongodb mongo:4.2.3
cd spring-cloud-config-server
mvnw package && docker build -f Dockerfile -t spring-cloud-config-server .
docker run --name spring-cloud-config-server -p 8888:8888 spring-cloud-config-server
cd ../netflix-eureka-naming-server
mvnw package && docker build -f Dockerfile -t netflix-eureka-naming-server .
docker run --name netflix-eureka-naming-server -p 8761:8761 netflix-eureka-naming-server
cd ../netflix-zuul-api-gateway-server
mvnw package && docker build -f Dockerfile -t netflix-zuul-api-gateway-server .
docker run --name netflix-zuul-api-gateway-server -p 8765:8765 netflix-zuul-api-gateway-server
cd ../customer
mvnw package && docker build -f Dockerfile -t customer .
docker run --name customer_1 -p 8090:8090 customer
docker run --name customer_2 -p 8090:8091 customer
cd ../Account
mvnw package && docker build -f Dockerfile -t account .
docker run --name account -p 8080:8080 account