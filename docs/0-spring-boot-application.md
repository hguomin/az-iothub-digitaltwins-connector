# Notes for spring boot development

## Build tools
* [Use project-specific maven wrapper script to build the source code](https://www.baeldung.com/maven-wrapper)

## DI & Spring boot autoconfiguration
* Bean definition and its instance creation(@Bean then new a instance)
* Dependency(Bean) injection to MVC Controller (@Autowired for Constructor inject/Field inject etc...)
* Use @Import/@ComponentScan to import related dependency configuration class when missing dependency

## Spring Cloud Stream configuration
In class `org.springframework.cloud.stream.config.BindingServiceConfiguration`

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

## Websocket integration  
* The most of easy way is use the STOMP approach in spring.  
Check [this series of blogs](https://juejin.cn/post/6844903655477346317) to see  more different implementation in springboot

* WebSocket integrate with Spring cloud stream (Refer to this [link](https://domenicosibilio.medium.com/how-to-create-a-spring-cloud-stream-binder-from-scratch-ab8b29ee931b))
  * NOTE: the spring cloud stream binder project need to be an independent java library, [Reason is here](https://stackoverflow.com/questions/54332239/cannot-retrieve-binder-configuration-in-spring-cloud-stream-2-1-0)
  * Implement `ProvisioningProvider` interface to provide producer/consumer destination, how to use the destination is actually 
  up to the binder implementation, `destination` is configured in spring.cloud.stream.[binding name].destination
  * Extends `MessageProducerSupport` to implement a message producer endpoint, which RECEIVE message from message broker or 
  GENERATE message itself, then send it to application consumer code by calling `MessageProducerSupport.sendMessage`.
    * `MessageProducerSupport.outputChannel` is a `MessageChannel` instance for a binding defined in spring.cloud.stream.bindings,
    will be set by spring automatically.
  * Implement a `org.springframework.messaging.MessageHandler` subclass or just usa a function to handle message that from application code and
  send it to message broker
    * We can subclass the `org.springframework.integration.handler.AbstractMessageProducingHandler` as a MessageHandler
  * Subclass `org.springframework.cloud.stream.binder.AbstractMessageChannelBinder` to implement a binder
  * Add a configuration class to crate the provisioning provider bean, the binder bean
  * Map the binder name to above binder configuration class in `spring.binders` file
* In Spring boot, websocket's handler mapping bean need to be registered in Application Context, that means registered 
in Binder's context you will got 404 error. WebSocket Handler Mapping bean are created in 
`org.springframework.web.socket.config.annotation.WebSocketConfigurationSupport`, so when binder create consumer endpoint,
we need to get web socket's handler bean from Application Context and link to inbound channel to receive websocket message
* Add websocket binder project as a git submodule to this project
 
## Spring servlet request processing
* See `org.springframework.web.servletFramework.Servlet.processRequest`