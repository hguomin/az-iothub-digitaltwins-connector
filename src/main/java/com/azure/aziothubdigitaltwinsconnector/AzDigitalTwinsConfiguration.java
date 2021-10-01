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
        String tenantId = "";
        String clientId = "";
        String clientSecret = "";
        String adtEndpoint = "";

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
