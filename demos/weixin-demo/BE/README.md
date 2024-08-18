# web
## Prerequisites
1. Install `IntelliJ IDEA 2024.1 (Community Edition)`
2. Install `Lombok` plugin and [set](https://www.baeldung.com/lombok-ide)
## Test Scope
Backend app for weixin service.  
1. SpringBoot & spring-boot-dependencies
2. Swagger UI
3. Lombok
4. Support WeChat mini program login and oauth
5. logbook-spring-boot-starter
6. spring-boot-starter-actuator
7. Database Migrations with Flyway 
8. spring-boot-starter-data-jpa and @EnableJpaAuditing
	
## Environment 
MySQL: 8.1.0  
JAVA: openjdk version "21.0.2" 2024-01-16  

## Install and verify this project
- Use maven commands
```shell
## DB migrate
Run DBMigration in wx-migration
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
| web         | http://127.0.0.1:8084/swagger-ui/index.html |

Switch environment by activating profile `-Dspring.profiles.active=local`

- The actuator endpoints

| Endpoint Name | Endpoint Address                      |
|---------------|---------------------------------------|
| Health Check  | http://127.0.0.1:8084/actuator/health |
| Info          | http://127.0.0.1:8084/actuator/info   |