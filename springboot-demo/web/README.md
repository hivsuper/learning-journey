# web
Test web application base on spring-boot.  
1. spring-boot
2. mybatis + HikariCP
3. mybatis-generator
4. transactional rollback
5. swagger-ui
6. FilterChainProxy implementation
	
## Environment 
MySQL: 5.6.31-0ubuntu0.15.10.1  
JAVA: 1.8.0_91  

## Q&A
+	How to Package the Project?  
	Run `mvn clean package`
+	How to Generate Class/XML from Database?  
	Run `mvn mybatis-generator:generate`  