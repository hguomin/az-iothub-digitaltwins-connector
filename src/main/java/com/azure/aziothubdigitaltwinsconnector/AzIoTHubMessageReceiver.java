package com.azure.aziothubdigitaltwinsconnector;

import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
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
/*
    @Bean
    DigitalTwinsClient digitalTwinsClient() {
        String tenantId = "c8006b8a-cb84-4197-bdfa-ddce719bd36d";
        String clientId = "65379042-8ac0-4ef5-badf-951d09f4f8df";
        String clientSecret = "VhA7Q~HSonaOfJreZDUSYY4OTKNNc6Zb~XZO2";
        String adtEndpoint = "https://gmadt.api.sea.digitaltwins.azure.net";

        return new DigitalTwinsClientBuilder()
                .credential(new ClientSecretCredentialBuilder()
                        .tenantId(tenantId)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build())
                .endpoint(adtEndpoint)
                .buildClient();
    }
 */
}
