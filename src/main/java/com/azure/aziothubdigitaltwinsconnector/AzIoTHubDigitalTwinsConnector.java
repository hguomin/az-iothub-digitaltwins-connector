
// Copyright (c) Guomin Huang. All rights reserved.
// Licensed under the MIT License.
// 09/29/2021

package com.azure.aziothubdigitaltwinsconnector;

import com.azure.core.models.JsonPatchDocument;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.azure.digitaltwins.core.DigitalTwinsClient;

@SpringBootApplication
public class AzIoTHubDigitalTwinsConnector {
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
            ReadContext msgCtx = JsonPath.parse(message);
            String twinIdQueryString = this.mapping.read("$.twinId");
            String twinId = (String) this.queryDataFromJson(msgCtx, twinIdQueryString);
            if (StringUtils.isNotEmpty(twinId)) {
                Map<String, Object> mappings = this.mapping.read("$.mappings");
                if (null != mappings) {
                    mappings.forEach((k,v) -> {
                        HashMap<String, Object> mapping = (HashMap<String, Object>)v;
                        if (StringUtils.equals(k, "Telemetry")) {

                            HashMap<String, Object> telemetryPayload = new HashMap<>();
                            mapping.forEach((mk, mv) -> {
                                Object data = this.queryDataFromJson(msgCtx, (String) mv);
                                telemetryPayload.put(mk, data);
                            });

                            digitalTwinsClient.publishTelemetry(twinId, null, telemetryPayload);
                        }
                        else if (StringUtils.equals(k, "Property")) {
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

            System.out.println("Azure IoT Hub: " + message);
        };
    }
}
