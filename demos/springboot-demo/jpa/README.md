# web
## Prerequisites
1. Install `IntelliJ IDEA 2024.1 (Community Edition)`
2. Install `Lombok` plugin and [set](https://www.baeldung.com/lombok-ide)
## Test Scope
Test JPA application base on spring-boot.  
1. SpringBoot
2. JPA + HikariCP
3. Transactional Rollback
4. Swagger UI
5. Lombok
6. logbook-spring-boot-starter
7. spring-boot-starter-actuator
8. Use redis via spring-boot-starter-data-redis and lettuce-core
9. Integrate with easy-captcha for verification
	
## Environment 
MySQL: 8.1.0  
JAVA: openjdk version "21.0.2" 2024-01-16  

## Install and verify this project
- Run `docker-compose.yml`
- Run `DBMigration` in `jpa-dbm` module
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

| Module Name | Swagger Address                             |
|-------------|---------------------------------------------|
| jpa-service | http://127.0.0.1:8083/swagger-ui/index.html |

Switch environment by activating profile `-Dspring.profiles.active=local`