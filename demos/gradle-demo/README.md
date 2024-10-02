# gradle demo
## Test Scope
1. Usage of `gradle`
2. Usage of `spring-boot-starter-validation`
3. Usage of `spring-boot-starter-data-jpa`
4. Usage of `org.testcontainers:testcontainers`
5. Usage of `postgreSQL`

## Environment
MySQL: 8.1.0  
JAVA: openjdk version "21.0.2" 2024-01-16

## Install and verify this project
- Run `docker-compose.yml`
- Run `DBMigration` in `gradle-dbm` module
- Start modules by running the `Bootstarp` class. Below is the swagger pages to see the endpoints' detail

| Module Name | Swagger Address                             |
|-------------|---------------------------------------------|
| web         | http://127.0.0.1:8088/swagger-ui/index.html |