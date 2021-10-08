//
//Guomin Huang @07/10/2021
//
package com.azure.aziothubdigitaltwinsconnector;

import com.microsoft.azure.sdk.iot.service.IotHubConnectionString;
import com.microsoft.azure.sdk.iot.service.IotHubConnectionStringBuilder;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure.iothub")
public class AzIoTHubConfiguration {
    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Bean
    public RegistryManager iotHubRegistryManager() {
        return new RegistryManager(this.connectionString);
    }

    @Bean
    public IotHubConnectionString iotHubConnectionString() {
        return IotHubConnectionStringBuilder.createIotHubConnectionString(this.connectionString);
    }
}
