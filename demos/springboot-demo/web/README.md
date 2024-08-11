# web
## Prerequisites
1. Install `IntelliJ IDEA 2024.1 (Community Edition)`
2. Install `Lombok` plugin and [set](https://www.baeldung.com/lombok-ide)
## Test Scope
Test web application base on spring-boot.  
1. SpringBoot
2. MyBatis + HikariCP
3. Transactional Rollback
4. Swagger UI
5. Lombok
6. Send email via spring-boot-starter-mail
7. Spring-retry + @Async
8. spring-boot-starter-cache
	
## Environment 
MySQL: 8.1.0  
JAVA: openjdk version "21.0.2" 2024-01-16  

## Install and verify this project
- Use maven commands
```shell
## Create git.properties
git-commit-id:revision
## Run Unit Tests
mvn test
## Run Integration Tests
mvn install -DskipUTs=true failsafe:integration-test -DskipITs=false
## Package
mvn clean package
```
- Start modules by running the `Bootstarp` class. Below is the swagger pages to see the endpoints' detail

| Module Name | Swagger Address                                  |
|-------------|--------------------------------------------------|
| web         | http://127.0.0.1:8082/swagger-ui/index.html      |

Switch environment by activating profile `-Dspring.profiles.active=local`