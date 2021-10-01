// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzDigitalTwinsConfiguration {

    /**
     * Inject DigitalTwinsClient bean
     */
    @Bean
    DigitalTwinsClient digitalTwinsClient() {
        String tenantId = "c8006b8a-cb84-4197-bdfa-ddce719bd36d";
        String clientId = "65379042-8ac0-4ef5-badf-951d09f4f8df";
        String clientSecret = "LiH7Q~F4hlGN50Kkg9m0IQw_IEiW1cSxLaGRV";
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
}
