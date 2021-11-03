package com.azure.aziothubdigitaltwinsconnector.messaging.websocket;

public interface WebSocketHub {
    public void sendEvent(String message);
}
