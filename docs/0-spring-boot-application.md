
## DI & Spring boot autoconfiguration
* Bean definition and its instance creation(@Bean then new a instance)
* Dependency(Bean) injection to MVC Controller (@Autowired for Constructor inject/Field inject etc...)

## MVC view with Thymeleaf
* [Add CSS and JS to Thymeleaf](https://www.baeldung.com/spring-thymeleaf-css-js)
* [Access model data from view templates](https://www.thymeleaf.org/doc/articles/springmvcaccessdata.html)
  * Model data  
  * Request parameters
* [Javascript variable in view templates](https://attacomsian.com/blog/thymeleaf-set-javascript-variable)
* 
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
