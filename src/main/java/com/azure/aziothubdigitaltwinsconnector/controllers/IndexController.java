package com.azure.aziothubdigitaltwinsconnector.controllers;

import com.azure.aziothubdigitaltwinsconnector.AzIothubDigitaltwinsConnectorApplication;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.digitaltwins.core.DigitalTwinsServiceVersion;
import com.azure.digitaltwins.core.models.DigitalTwinsModelData;

import com.azure.identity.ClientSecretCredentialBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azure.digitaltwins.core.DigitalTwinsClient;

@Controller
@RequestMapping("/")
public class IndexController {

    final DigitalTwinsClient digitalTwinsClient;

    @Autowired
    public IndexController(DigitalTwinsClient digitalTwinsClient) {
        this.digitalTwinsClient = digitalTwinsClient;
    }

    @GetMapping
    public String index() {
        try{
            DigitalTwinsModelData model = digitalTwinsClient.getModel("dtmi:example:Room;1");

            System.out.println(model.getModelId());
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return "index";
    }

    //For test
    @GetMapping("/restart")
    public String restart() {
        AzIothubDigitaltwinsConnectorApplication.restart();
        return "index";
    }
}
