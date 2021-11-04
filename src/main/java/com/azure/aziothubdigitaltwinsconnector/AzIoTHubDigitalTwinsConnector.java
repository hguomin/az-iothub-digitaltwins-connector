
// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import com.azure.aziothubdigitaltwinsconnector.messaging.websocket.WebSocketHub;
import com.azure.core.models.JsonPatchDocument;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.azure.digitaltwins.core.DigitalTwinsClient;
import org.springframework.messaging.Message;
import rx.Producer;

@SpringBootApplication
public class AzIoTHubDigitalTwinsConnector {
    private Logger log = LoggerFactory.getLogger(AzIoTHubDigitalTwinsConnector.class);
    private final ReadContext mapping;

    public AzIoTHubDigitalTwinsConnector() {
        this.mapping = JsonPath.parse("{ \"twinId\": \"${$.deviceId}\",\"version\": 5, \"mappings\": { \"Telemetry\": { \"temperature\": \"${$.temperature}\"}, \"Property\": {\"avgTemperature\": \"${$.temperature}\"}}}");
    }

    private Object queryDataFromJson(ReadContext jsonContext, String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            throw new IllegalArgumentException("Argument queryString is empty");
        }
        queryString = queryString.trim();
        Object data = queryString;
        if (queryString.startsWith("${")) {
            data = jsonContext.read(queryString.substring(2, queryString.length()-1).trim());
        }
        return data;
    }

    @Bean
    Consumer<String> receive(DigitalTwinsClient digitalTwinsClient) {
        return message -> {
            try {
                ReadContext msgCtx = JsonPath.parse(message);
                String twinIdQueryString = this.mapping.read("$.twinId");
                String twinId = (String) this.queryDataFromJson(msgCtx, twinIdQueryString);
                if (StringUtils.isNotEmpty(twinId)) {
                    Map<String, Object> mappings = this.mapping.read("$.mappings");
                    if (null != mappings) {
                        mappings.forEach((k, v) -> {
                            HashMap<String, Object> mapping = (HashMap<String, Object>) v;
                            if (StringUtils.equals(k, "Telemetry")) {

                                HashMap<String, Object> telemetryPayload = new HashMap<>();
                                mapping.forEach((mk, mv) -> {
                                    Object data = this.queryDataFromJson(msgCtx, (String) mv);
                                    telemetryPayload.put(mk, data);
                                });

                                digitalTwinsClient.publishTelemetry(twinId, null, telemetryPayload);
                            } else if (StringUtils.equals(k, "Property")) {
                                JsonPatchDocument propOp = new JsonPatchDocument();
                                mapping.forEach((mk, mv) -> {
                                    Object data = this.queryDataFromJson(msgCtx, (String) mv);
                                    propOp.appendReplace("/avgTemperature", data);
                                });
                                digitalTwinsClient.updateDigitalTwin(twinId, propOp);
                            }
                        });
                    }
                    String dt = digitalTwinsClient.getDigitalTwin(twinId, String.class);
                    System.out.println(dt);
                }
            }
            catch(Exception e){
                System.out.println("Error occurs while processing events from Azure IoT Hub: " + e.getMessage());
                System.out.println("Message is : " + message);
            }
        };
    }

    @Bean
    public Consumer<String> deviceTelemetryReceiver(WebSocketHub wsHub) {
        return message -> {
            System.out.println("Azure IoT Hub: " + message);
            wsHub.sendEvent(message);
        };
    }

    @Bean
    public Function<Message<String>, String> adtRoutedEventsProcessor() {
        return message -> {
            System.out.println("Azure Digital Twins - adtRoutedEventsProcessor: " + message.getPayload());
            String eventType = message.getHeaders().get("ce-type", String.class);
            String eventSource = message.getHeaders().get("ce-source", String.class);
            String twinId = "";
            switch (eventType) {
                case "microsoft.iot.telemetry": {
                    int index = StringUtils.lastIndexOf(eventSource, "/");
                    twinId = eventSource.substring(index+1);
                }break;
                case "Microsoft.DigitalTwins.Twin.Update": {
                    twinId = message.getHeaders().get("ce-subject", String.class);
                }break;
                default: break;
            }
            String newMsg = "{ \"twinId\": \"" + twinId + "\", \"body\": " + message.getPayload() + "}";

            return newMsg;
        };
    }

    @Bean
    public Consumer<Message<String>> adtVisualEventsReceiver() {
        return message -> {
            System.out.println("Azure Digital Twins - adtVisualEventsReceiver: " + message.getPayload());
        };
    }
}
