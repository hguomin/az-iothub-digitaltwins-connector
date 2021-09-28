package com.azure.aziothubdigitaltwinsconnector.controllers;

import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.digitaltwins.core.DigitalTwinsServiceVersion;
import com.azure.digitaltwins.core.models.DigitalTwinsModelData;

import com.azure.identity.ClientSecretCredentialBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azure.digitaltwins.core.DigitalTwinsClient;

@Controller
@RequestMapping("/")
public class IndexController {

    private DigitalTwinsClient digitalTwinsClient; 

    @GetMapping
    public String index() {
        String tenantId = "c8006b8a-cb84-4197-bdfa-ddce719bd36d";
        String clientId = "65379042-8ac0-4ef5-badf-951d09f4f8df";
        String clientSecret = "VhA7Q~HSonaOfJreZDUSYY4OTKNNc6Zb~XZO2";
        String adtEndpoint = "https://gmadt.api.sea.digitaltwins.azure.net";

        digitalTwinsClient = new DigitalTwinsClientBuilder()
                .credential(new ClientSecretCredentialBuilder()
                        .tenantId(tenantId)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build())
                .endpoint(adtEndpoint)
                .buildClient();
        DigitalTwinsServiceVersion adtVersion = digitalTwinsClient.getServiceVersion();
        System.out.println(adtVersion.getVersion());

        try{
            DigitalTwinsModelData model = digitalTwinsClient.getModel("dtmi:example:Room;1");
            System.out.println(model.getModelId());
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return "index";
    }
}
