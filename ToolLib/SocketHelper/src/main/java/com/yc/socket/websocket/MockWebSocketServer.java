package com.yc.socket.websocket;



import com.yc.toolutils.system.AppLogUtils;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;

public class MockWebSocketServer {

    private static MockWebServer mockWebServer;

    public static void mockWebSocket() {
        if (mockWebServer != null) {
            return;
        }
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                AppLogUtils.i("MockWebSocketServer", "onOpen:  " + response.toString());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                AppLogUtils.i("MockWebSocketServer", "onMessage:  " + text);
                webSocket.send("你好客户端： " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                AppLogUtils.i("MockWebSocketServer", "onMessage:  " + bytes.size());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                AppLogUtils.i("MockWebSocketServer", "onClosing:  " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                AppLogUtils.i("MockWebSocketServer", "onClosed:  " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                AppLogUtils.i("MockWebSocketServer", "onFailure:  " + response + "Throwable: " + webSocket);
            }
        }));

    }

    public static String getWsUrl() {
        String url = "ws:" + mockWebServer.getHostName() + ":" + mockWebServer.getPort();
        AppLogUtils.i("MockWebSocketServer", url);
        return url;
    }

}
