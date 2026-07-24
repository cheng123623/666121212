package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessionMap.put(sid, session);
        log.info("WebSocket连接建立: {}", sid);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        sessionMap.remove(sid);
        log.info("WebSocket连接关闭: {}", sid);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到消息: {} from {}", message, sid);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket错误: {}", error.getMessage());
    }

    public void sendToAllClient(String message) {
        sessionMap.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("WebSocket发送失败: {}", e.getMessage());
            }
        });
    }

    public void sendToOneClient(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("WebSocket发送失败: {}", e.getMessage());
            }
        }
    }
}
