# java-notes
1. java日志的使用
http://aub.iteye.com/blog/1103685
2. 异常的一些处理方法
3. Swagger2Markup生成静态API文档
4. 参考：
+ http://www.jianshu.com/p/ecb8daa4ecf7
+ https://springfox.github.io/springfox/docs/current/#configuring-springfox-staticdocs
+ http://blog.csdn.net/daisy_xiu/article/details/52368920
+ http://www.robwin.eu/documentation-of-a-rest-api-with-swagger-and-asciidoc/
+ http://kanpiaoxue.iteye.com/blog/2151903

## How to generate static the document of endpoints? 
1. Run `mvn compile`  
1. Run `mvn test`
1. Run `mvn asciidoctor:process-asciidoc` 
1. Then the document can be found in `target\generated-docs\${project.version}\index.html` like the [example](src/docs/example/index.html)

## How to access swagger?
1. Run `mvn tomcat7:run`
2. Visit http://127.0.0.1:8080/notes/swagger-ui.html in browser