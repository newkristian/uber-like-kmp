package com.example.uberapp_tim9.driver.sockets;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class SocketsConfiguration {

    public StompClient stompClient;
    public SocketsConfiguration() {
        initWebsocket();
    }

    private void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/socket/websocket");
        stompClient.connect();
    }

}
