# Microservices with Spring boot
Example app microservices patter with Java 18 using:

- Microservices patter
- Data base per service patter
- Api Gateway patter
- Circuit Braker patter

- Spring Boot
- Spring Authorization Server
- Spring Security with JWT
- Spring data
- Spring cloud with feing
- Spring cloud gateway
- Spring Webflux

### Hello!

This repository is created for test in neoris



# Example microservice apps

1. [Auth microservices](/microservices-auth) The authorization and authentication for security, as well as JWT token generation for queries and access requests to the other microservices are in charge of the authorization and authentication, by [Spring Authorization server](https://spring.io/projects/spring-authorization-server)
2. [Users microservices](/microservices-users) It is responsible for the management of users, for subsequent consultation and verification of identity by the microservice auth, as well as the assignment of bank accounts.
3. [Account microservices](/microservices-accounts) It handles the management of accounts and amounts, serves as an intermediary for assigning accounts to users as well as being consulted by the transaction microservice.
4. [Transactional microservices](/microservices-transactional) Transfers of amounts between accounts are handled, as well as the generation of records on transactions made to the accounts.
5. [Gateway](/microservices-gateway) Functions as a gateway for the connection and routing of other microservices.

## Architecture Diagram

Take a look at the components diagram that describes them and their interactions.
![microservices-test-example](/test-neoris-diagram.jpg)

## How to start

The easiest way is to use `docker-compose`:

```
docker-compose up -d
```

1. Initial request is made to the uri: http://127.0.0.1:8001/oauth2/authorization/microservices-users in order to generate an authorization code.
2. which is consulted in: http://127.0.0.1:9000/login adding as form-data the user username and password.
3. then the query for the generation of the JWT token is made in: http://127.0.0.1:9000/oauth2/token where form-data must carry the parameters of code, grant_type and redirect_uri, corresponding to the authorization code generated in step 1.



## Contribution

Project oriented as a base test, for the implementation of microservices with technologies based on the spring framework and spring boot ecosystem.