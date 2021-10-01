// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class AzIothubDigitaltwinsConnectorApplication {
	private static ConfigurableApplicationContext applicationContext;

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void main(String[] args) {

		applicationContext = SpringApplication.run(AzIoTHubDigitalTwinsConnector.class, args);

	}

}
