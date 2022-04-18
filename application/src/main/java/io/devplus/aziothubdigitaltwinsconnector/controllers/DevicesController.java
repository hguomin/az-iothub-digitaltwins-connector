package io.devplus.aziothubdigitaltwinsconnector.controllers;

import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.IotHubConnectionString;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceMethod;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.devicetwin.MethodResult;
import com.microsoft.azure.sdk.iot.service.devicetwin.Query;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/devices")
public class DevicesController {
    private final IotHubConnectionString iotHubConnectionString;
    private final RegistryManager deviceRegistry;
    private final DeviceTwin deviceTwin;
    private final DeviceMethod deviceMethod;

    @Autowired
    public DevicesController(IotHubConnectionString iotHubConnectionString, RegistryManager deviceRegistry, DeviceTwin deviceTwin, DeviceMethod deviceMethod) {
        this.iotHubConnectionString = iotHubConnectionString;
        this.deviceRegistry = deviceRegistry;
        this.deviceTwin = deviceTwin;
        this.deviceMethod = deviceMethod;
    }

    @GetMapping()
    public String index(Model model) throws Exception {
        List<DeviceTwinDevice> devices = new ArrayList<DeviceTwinDevice>();
        Query query = this.deviceTwin.queryTwin("SELECT * FROM devices", 10);
        while (this.deviceTwin.hasNextDeviceTwin(query)) {
            DeviceTwinDevice device = this.deviceTwin.getNextDeviceTwin(query);
            devices.add(device);
        }
        model.addAttribute("deviceList", devices);
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

    @RequestMapping(value = "/{deviceId}/method", method = RequestMethod.GET) 
    public String invokeDeviceMethod(@PathVariable("deviceId")String deviceId) {
        Map<String, Object> payload = new HashMap<String, Object>()
        {
            {
                put("key", "value");
            }
        };

        try {
                MethodResult result = this.deviceMethod.invoke(deviceId, "stop", TimeUnit.SECONDS.toSeconds(200), TimeUnit.SECONDS.toSeconds(5), payload);
        } catch ( IotHubException | IOException e) {
            e.printStackTrace();
        }

        return "devices/index";
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
