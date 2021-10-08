## Use MQTT.js to connect IoT Hub and simulate device in browser environment  
[MQTT.js](https://www.npmjs.com/package/mqtt) is javascript library that implements MQTT protocol client function and can
be used in browsers and Node.js environments. 

Through this way, we can use it to connect to Azure IoT Hub in MQTT protocol natively, then simulate iot device in browser.

<big>**How to:**</big>

1. Import and use MQTT.js in html page  
    ```html
    <script type="text/javascript" src="https://unpkg.com/mqtt/dist/mqtt.min.js"></script> 
    <script type="text/javascript">
        console.log(mqtt);
    </script>
    ```
2. IoT Hub MQTT connect setup  
Execute below cli command to generate a SAS token for the device and note down it 
    ```bash
    $ az iot hub generate-sas-token --connection-string "[device connection string] --duration [duration in seconds]"
    ```
    MQTT connect parameters, reference [here](https://github.com/Azure/azure-iot-sdk-java/blob/main/device/iot-device-client/src/main/java/com/microsoft/azure/sdk/iot/device/transport/mqtt/MqttIotHubConnection.java)

    ```javascript
   const clientId = "[device id]";
   const deviceSasToken = "[device sas token]";
   const host = "wss://[iot hub hostname]/$iothub/websocket?iothub-no-client-cert=true";
   const options = {
       clientId: "[device id]",
       username: "[iot hub hostname]/[device id]/?api-version=2018-06-30",
       password: deviceSasToken,
   
       keepalive: 230,
       clean: false,
   };
    
   console.log("Connecting to Azure IoT Hub...");
   const client = mqtt.connect(host, options);

   //Subscribe and receive c2d message
   client.on("connect", options => {
       console.log("Device connected: ", deviceId);
       client.subscribe(C2D_MSG_TOPIC, {qos: 0});
   });
   client.on("message", (topic, message, packet) => {
       console.log("Received Message: " + message.toString() + "\nOn topic: " + topic);
   });
   
   //Send device to cloud telemetry messages
   setInterval((client) => {
   client.publish(D2C_MSG_TOPIC, "{\"data\": hello}", {qos: 0, retain: false});
   }, 2000, client);
   
   client.on("error", (err) => {
       console.log("Connection error: ", err);
       client.end();
   });

   client.on("reconnect", (err) => {
       console.log("Reconnecting...");
   });
    ```
3. Monitor IoT Hub events
   ```bash
   az iot hub monitor-events --output table --hub-name {iot hub name}
   ```
4. xx

## Reference
* [MQTT.js browser example](https://www.emqx.com/en/blog/connect-to-mqtt-broker-with-websocket)
* [MQTT Client Tool Online](https://www.emqx.com/en/mqtt/mqtt-websocket-toolkit)  

* Use npm javascript package in browser

    npm packages, for example the azure-iot-device, can not be used directly in browser, in order to do that, we can use
    [Browserify](https://github.com/browserify/browserify) to convert it to a bundled javascript and then use it in browser.
    
    below use azure-iot-device package as an example:
    
    STEP1: Install Browserify
    ```bash
    $ sudo npm install -g browserify
    ```
    
    SETP2: Download and unzip azure-iot-device from [here](https://registry.npmjs.org/azure-iot-device/-/azure-iot-device-1.17.6.tgz),
    then go to the package folder
    ```bash
    $ cd package
    $ browserify device.js > azure-iot-device.bundle.js
    ```
    
    STEP3: Use the bundled `azure-iot-device.bundle.js` in browser javascript code
    
    For example, place the bundled js file to `static/js` folder under `resources` folder in Spring boot project, then reference
    it in thymeleaf page as below
    ```html
    <script type="text/javascript" th:src="@{/js/azure-iot-device.bundle.js}"></script>
    ```
* [Browserify: Load Node.js module in browser](https://javascript.ruanyifeng.com/tool/browserify.html)