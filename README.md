# az-iothub-digitaltwins-connector

A demo project that shows how to build a Azure IoT and Digital Twins connected solution with open source frameworks like Spring Cloud Stream, MQTT.js etc.

## Build from source
### Clone source code from this repo  
```bash 
git clone https://github.com/hguomin/az-iothub-digitaltwins-connector.git
```

### Make your application settings
Setup your application settings in the file `src/main/resources/application.yaml`.

### Package
Go to the root folder of this projectfollow below steps to build from source:
* On Linux
    ```bash
    $ ./mvnw clean package -DskipTests
    ```
* On Windows
    ```bash
    mvnw.cmd clean package -DskipTests
    ```

### Install
Go to the root folder of this projectfollow below steps to build from source:  
* On Linux
    ```bash
    $ ./mvnw clean install -DskipTests
    ```
* On Windows
    ```bash
    mvnw.cmd clean install -DskipTests
    ```
## Run the application
* On Linux
    ```bash
    $ ./mvnw spring-boot:run
    ```
* On Windows
    ```bash
    mvnw.cmd spring-boot:run
    ```
