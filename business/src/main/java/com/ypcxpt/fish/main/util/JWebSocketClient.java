package com.ypcxpt.fish.main.util;

import com.ypcxpt.fish.library.util.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClient extends WebSocketClient {

    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.e("JWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Logger.e("JWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.e("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Logger.e("JWebSocketClient", "onError()");
    }
}
