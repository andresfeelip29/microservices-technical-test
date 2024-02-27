# Microservices with Spring boot

Example app microservices patter with Java 18 using:

- Microservices patter
- Data base per service patter
- Api Gateway patter

- Spring Boot
- Spring Security with JWT
- Spring data
- Spring cloud with feing
- Spring cloud gateway
- Spring Webflux

### Hello!

This repository is created for test in neoris

# Example microservice apps

1. [Auth microservices](/microservices-auth) The authorization and authentication for security, as well as JWT token
   generation for queries and access requests to the other microservices are in charge of the authorization and
   authentication, by [Spring Security](https://spring.io/projects/spring-securityr)
2. [Users microservices](/microservices-users) It is responsible for the management of users, for subsequent
   consultation and verification of identity by the microservice auth, as well as the assignment of bank accounts.
3. [Account microservices](/microservices-accounts) It handles the management of accounts and amounts, serves as an
   intermediary for assigning accounts to users as well as being consulted by the transaction microservice.
4. [Transactional microservices](/microservices-transactions) Transfers of amounts between accounts are handled, as well
   as the generation of records on transactions made to the accounts.
5. [Gateway microservices](/microservices-gateway) Functions as a gateway for the connection and routing of other
   microservices, by [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
5. [Registry microservices](/microservices-registry) Functions is to provide logging and verification of microservices
   using the eureka server, by [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix)

## Architecture Diagram

Take a look at the components diagram that describes them and their interactions.
![microservices-test-example](/test-neoris-diagram.jpg)

## How to start

The easiest way is to use `docker-compose`:

```
docker-compose up -d
```

1. You need to have a registered user in the system, this bread does not require authentication so you can proceed to
   register in this uri: http://host.docker.internal:8090/api/v1/auth/register

```json
{
  "username": "username",
  "password": "password",
  "status": true
}
```

2. For JWT token generation and access to all resources you must log in with the users and password registered in step
   2, in the following url: http://host.docker.internal:8090/api/v1/auth/token.

```json
{
  "username": "username",
  "password": "password"
}
```

## Contribution

Project oriented as a base test, for the implementation of microservices with technologies based on the spring framework
and spring boot ecosystem.