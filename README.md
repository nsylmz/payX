# payX
An Example Payment Backend Application with MicroServices Architecture

Used Technologies & Architectures & Principles & Disciplines,
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

Not Achived and future improvement steps
- Adding Swagger2 (Already tried but there is incompatiblity problem on swagger 2.9.2 version with spring-boot-starter 2.2.4.RELEASE)
- Adding spring security (no time left to complete)
- Email configuration scanerio
