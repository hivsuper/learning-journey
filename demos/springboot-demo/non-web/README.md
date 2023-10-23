# non-web
Test CommandLineRunner base on spring-boot.  
1. spring-boot  
2. mybatis + HikariCP
3. mybatis-generator
4. transactional rollback
	
## Environment 
MySQL: 5.6.31-0ubuntu0.15.10.1  
JAVA: 1.8.0_91  

## Q&A
+	How to Package the Project?  
	Run `mvn clean package`
+	How to Generate Class/XML from Database?  
	Run `mvn mybatis-generator:generate`    

## Reference
*	https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html  
*	https://www.mkyong.com/spring-boot/spring-boot-jdbc-mysql-hikaricp-example/  
*	https://stackoverflow.com/questions/35610147/spring-boot-mybatis-mapperscan-and-sqlsessionfactory  
*	http://www.cnblogs.com/mahuan2/p/6420577.html  