// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure.digital-twins")
public class AzDigitalTwinsConfiguration {
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String serviceEndpoint;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    /**
     * Inject DigitalTwinsClient bean
     */
    @Bean
    DigitalTwinsClient digitalTwinsClient() {
        return new DigitalTwinsClientBuilder()
                .credential(new ClientSecretCredentialBuilder()
                        .tenantId(this.tenantId)
                        .clientId(this.clientId)
                        .clientSecret(this.clientSecret)
                        .build())
                .endpoint(this.serviceEndpoint)
                .buildClient();
    }
}
