# payX
An Example Payment Backend Application with MicroServices Architecture

#### Used Technologies & Architectures & Principles & Disciplines:
- MicroServices Architecture
- SpringBoot(starter version 2.2.4.RELEASE)
- MongoDB(4.2.3) (MongoDB is used. PayDay scenarios don't need transaction in DB. MongoDB will increase perf significantly)
- Reactive Programing
- RESTful
- Using correct Http methods(GET, POST, DELETE)
- Returning proper HttpStatus codes(200,201,404,400,500)
- Generic Exception handling(CustomerNotFoundException, UserNotFoundException)
- Validation with javax.validation api(example in Account and Customer documents)
- HATEOAS(in HttpStatus.created, example -->/account/accounts/2/openDebitAccount)
- Spring Cloud Feign Client(example usage in AccountResource.openDebitAccount and CustomerServiceProxy)
- Spring Cloud Ribbon Load Balancer(example usage in AccountResource.openDebitAccount and CustomerServiceProxy)
- Spring Cloud Config Server(in Docker you will use prod env values)
- Netflix Eureka Naming Server
- Netflix Zuul API Gateway
- Docker configurations with DockerFile

#### Not Achived and future improvement steps:
- Adding Swagger2 (Already tried but there is incompatiblity problem on swagger 2.9.2 version with spring-boot-starter 2.2.4.RELEASE)
- Spring security should be added (no time left to complete)
- Email confirmation scanerio

### PORTS & APPS
APP NAME | PORT
------------ | -------------
spring-cloud-config-server | 8888
netflix-eureka-naming-server | 8761
netflix-zuul-api-gateway-server | 8765
customer | 8090
account | 8080

### Docker Deployment Instructions & Steps
- Enter your local git repo file path for payX clone on cmd
- Run "docker pull mongo:4.2.3 && docker run --name mongodb mongo:4.2.3" on cmd
- Run "cd spring-cloud-config-server" on cmd
- Run "mvnw package && docker build -f Dockerfile -t spring-cloud-config-server ." on cmd
- Run "docker run --name spring-cloud-config-server -p 8888:8888 spring-cloud-config-server" on cmd
- Run "cd ../netflix-eureka-naming-server" on cmd
- Run "mvnw package && docker build -f Dockerfile -t netflix-eureka-naming-server ." on cmd
- Run "docker run --name netflix-eureka-naming-server -p 8761:8761 netflix-eureka-naming-server" on cmd
- Run "cd ../netflix-zuul-api-gateway-server" on cmd
- Run "mvnw package && docker build -f Dockerfile -t netflix-zuul-api-gateway-server ." on cmd
- Run "docker run --name netflix-zuul-api-gateway-server -p 8765:8765 netflix-zuul-api-gateway-server" on cmd
- Run "cd ../customer" on cmd
- Run "mvnw package && docker build -f Dockerfile -t customer ." on cmd
- Run "docker run --name customer_1 -p 8090:8090 customer" on cmd
- Run "docker run --name customer_2 -p 8091:8091 customer" on cmd
- Run "cd ../Account" on cmd
- Run "mvnw package && docker build -f Dockerfile -t account ." on cmd
- Run "docker run --name account -p 8080:8080 account" on cmd






