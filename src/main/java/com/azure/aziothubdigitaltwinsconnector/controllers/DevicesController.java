package com.azure.aziothubdigitaltwinsconnector.controllers;

import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.IotHubConnectionString;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Controller
@RequestMapping("/devices")
public class DevicesController {
    private final RegistryManager deviceRegistry;
    private final IotHubConnectionString iotHubConnectionString;
    @Autowired
    public DevicesController(RegistryManager deviceRegistry, IotHubConnectionString iotHubConnectionString) {
        this.deviceRegistry = deviceRegistry;
        this.iotHubConnectionString = iotHubConnectionString;
    }

    @GetMapping()
    public String index() {
        return "devices/index";
    }

    @MessageMapping("/ws-login")
    @SendTo("/topic/message")
    public String message(String msg) throws Exception {
        return "Hello " + msg;
    }

    @RequestMapping(value = "/{deviceId}/telemetry", method = RequestMethod.GET)
    public String telemety(Model model, @PathVariable("deviceId")String deviceId) {
        return "devices/telemetry";
    }

    @RequestMapping(value = "/{deviceId}/simulate", method = RequestMethod.GET)
    public String simulate(Model model, @PathVariable("deviceId") String deviceId) {
        Device device = null;
        try {
            device = this.deviceRegistry.getDevice(deviceId);
            String deviceSasToken = generateIoTHubDeviceSasToken(this.iotHubConnectionString.getHostName(), deviceId, device.getPrimaryKey());
            model.addAttribute("deviceId", deviceId);
            model.addAttribute("devicePrimaryKey", device.getPrimaryKey());
            model.addAttribute("deviceSasToken", deviceSasToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "devices/simulate";
    }

    public static String generateIoTHubDeviceSasToken(String iothubHostName, String deviceId, String deviceKey) throws Exception {
        String resourceUri = iothubHostName + "/devices/" + deviceId;
        // Token will expire in one hour
        long expiry = Instant.now().getEpochSecond() + 3600;

        String stringToSign = URLEncoder.encode(resourceUri, StandardCharsets.UTF_8.toString()) + "\n" + expiry;
        byte[] decodedKey = Base64.getDecoder().decode(deviceKey);

        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
        sha256HMAC.init(secretKey);
        Base64.Encoder encoder = Base64.getEncoder();

        String signature = new String(encoder.encode(
                sha256HMAC.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);

        String token = "SharedAccessSignature sr=" + URLEncoder.encode(resourceUri, StandardCharsets.UTF_8.toString())
                + "&sig=" + URLEncoder.encode(signature, StandardCharsets.UTF_8.name()) + "&se=" + expiry;

        return token;
    }
}
