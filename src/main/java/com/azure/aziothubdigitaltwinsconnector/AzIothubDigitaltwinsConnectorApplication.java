// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import org.springframework.boot.ApplicationArguments;
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

	/**
	 * Application restart
	 */
	public static void restart() {
		ApplicationArguments args = applicationContext.getBean(ApplicationArguments.class);

		Thread t = new Thread(() -> {
			applicationContext.close();
			applicationContext = SpringApplication.run(AzIoTHubDigitalTwinsConnector.class, args.getSourceArgs());
		});

		//Prevent the JVM shutdown triggered by the applicationContext.closed() from closing the application
		t.setDaemon(false);

		t.start();
	}
}
