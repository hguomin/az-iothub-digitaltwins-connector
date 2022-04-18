# Receive message from IoT Hub using Spring Cloud Stream API

## Azure Event Hub Binder for Spring Cloud Stream  
Add Azure Event Hub binder dependency to pom.xml  
```xml
<dependency>
    <groupId>com.azure.spring</groupId>
    <artifactId>azure-spring-cloud-stream-binder-eventhubs</artifactId>
    <version>2.8.0</version>
</dependency>
```

## Add IoT Hub message receiver code
```java
package io.devplus.aziothubdigitaltwinsconnector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AzIoTHubMessageReceiver {
    @Bean
    Consumer<String> receive() {
        return message -> {
            System.out.println("Azure IoT Hub: " + message);
        };
    }
}
```

## Add configuration in application.yaml
```yaml
spring:
  cloud:
    azure:
      eventhub:
        connection-string: [IoT hub built-in endpoint connection string]
        checkpoint-storage-account: [Storage account name, eg: gmdtstorage]
        checkpoint-access-key: [Storage account's access key]
        checkpoint-container: [container name in storage account]

    stream:
      bindings:
        receive-in-0:
          destination: [IoT Hub name, eg: gm-dt-iothub]
          group: [Consumer group, eg: $Default]

      function:
        definition: receive;
```  
Please note that the 'receive' in [receive-in-0] and function.definition.receive is come from the method AzIoTHubMessageReceiver.receive  

  
  
Now run the application you will receive iot hub message...

## Support multiple Event Hub binders
We will use this setup to receive telemetry from IoT Hub, and events from Azure Digital Twins Event Route by Event Hub.  
Configuration see [Spring Cloud Azure Stream Binder for Multiple Event Hub Namespace](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/eventhubs/azure-spring-cloud-stream-binder-eventhubs/eventhubs-multibinders).

## References  
* [Produce/Consume Events with Spring Cloud Stream and Event Hub](https://medium.com/@aviadpines/produce-consume-events-with-spring-cloud-stream-and-event-hub-4b41fdc1a9f6)