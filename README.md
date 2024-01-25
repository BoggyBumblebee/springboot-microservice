# springboot-microservice
The Spring Boot Microservice Example Application

Based upon [Daily Code Buffer's Microservices using SpringBoot 3.0 Tutorial](https://github.com/shabbirdwd53/springboot-3-microservices)
we take this further by adding in:

- Maven Project Hierarchy - Done
- JPA Repositories - Done
- Authentication and Authorization - TODO
- AOP Logging - Done
- ...

## Running the Application
Please ensure Docker or Docker Desktop is running on the instance, and then run the following command line:

> docker run -d -p 9411:9411 openzipkin/zipkin

Then start the modules in the following order:

1. config-server
2. service-registry
3. employee-service
4. department-service
5. api-gateway