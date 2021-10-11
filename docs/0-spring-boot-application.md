## Build tools
* [Use project-specific maven wrapper script to build the source code](https://www.baeldung.com/maven-wrapper)

## DI & Spring boot autoconfiguration
* Bean definition and its instance creation(@Bean then new a instance)
* Dependency(Bean) injection to MVC Controller (@Autowired for Constructor inject/Field inject etc...)

## MVC view with Thymeleaf
* [Add CSS and JS to Thymeleaf](https://www.baeldung.com/spring-thymeleaf-css-js)
* [Access model data from view templates](https://www.thymeleaf.org/doc/articles/springmvcaccessdata.html)
  * Model data  
  * Request parameters
* [Javascript variable in view templates](https://attacomsian.com/blog/thymeleaf-set-javascript-variable)
* [Customized view template layouts support in Spring boot MVC application](https://blog.codeleak.pl/2013/11/thymeleaf-template-layouts-in-spring.html)  
  * Use [Spring Request Interceptor](https://zhuanlan.zhihu.com/p/78447141) to intercept request and change the view name to load layout template. 
  * Use [Thymleaf fragments](https://www.baeldung.com/spring-thymeleaf-fragments) to include page content into layout file.   
    * Define fragment placeholder in layout file to include page fragments 
    * Define page fragment in page view.
    * In layout file, use `th:block` to [conditional include](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#advanced-conditional-insertion-of-fragments) css/javascript fragments which is defined in page view 
* xx

## Dynamic configuration & application restart support
Check this [Link](https://www.baeldung.com/java-restart-spring-boot-app) to see implementation.  
It's for IoT Hub/Event Hub/Digital Twins configuration like connection string change, then the application can connect
to different instance of above services

## Configuration reference
* Check this [Link](https://zhuanlan.zhihu.com/p/57693064) for configuration overview  
* [Configuration profiles](https://www.baeldung.com/spring-profiles)  
For configuration file check-in(don't commit secret keys/connection string etc.), different settings for 
production/development/debugging etc.
* 
