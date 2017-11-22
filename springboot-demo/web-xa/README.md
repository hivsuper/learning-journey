# web-xa
Clone web-non-web module and support XA transactions.
	
## Environment 
MySQL: 5.6.31-0ubuntu0.15.10.1  
JAVA: 1.8.0_91  

## Q&A
+	How to Package the Project?  
	Run `mvn clean package`
+	How to Generate Class/XML from Database?  
	Run `mvn mybatis-generator:generate`  

## Reference
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html  
http://fabiomaffioletti.me/blog/2014/04/15/distributed-transactions-multiple-databases-spring-boot-spring-data-jpa-atomikos/  
http://z-xiaofei168.iteye.com/blog/1044843  