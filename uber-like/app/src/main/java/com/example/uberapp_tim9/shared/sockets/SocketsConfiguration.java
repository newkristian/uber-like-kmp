package com.example.uberapp_tim9.shared.sockets;

import android.util.Log;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class SocketsConfiguration {

    public StompClient stompClient;
    public SocketsConfiguration() {
        initWebsocket();
    }

    private void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.9.107.231:8080/ws/websocket");
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("Websocket", "Opened");
                    break;
                case ERROR:
                    Log.d("Websocket", "Error");
                    break;
                case CLOSED:
                    Log.d("Websocket", "Closed");
                    break;
            }
        });

        stompClient.connect();
    }

}
