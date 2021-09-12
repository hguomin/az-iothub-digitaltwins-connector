package com.azure.aziothubdigitaltwinsconnector;

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
