package com.runbrick.chatroom.controller;

import com.runbrick.chatroom.domain.UserDO;
import com.runbrick.chatroom.util.DateUtil;
import com.runbrick.chatroom.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/hd")
@Component
public class ChatSocketController {


    // 每个用户登录都要存入进去
    private static Map<String, UserDO> onlineSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session toSession) {
        UserDO userDO = new UserDO(StringUtil.randomString(5), toSession.getId(), toSession);
        onlineSessions.put(toSession.getId(), userDO);

        onlineSessions.forEach((id, session) -> {
            try {
                if (!id.equals(toSession.getId())) {
                    session.getSession().getBasicRemote().sendText(String.format("[用户%s]加入了聊天室", session.getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("当前有新用户加入{},加入时间{},当前在线人数{}", toSession.getId(), DateUtil.getTheCurrentTimeString(), onlineSessions.size());
    }


    @OnClose
    public void onClose(Session toSession) {
        UserDO userDO = onlineSessions.get(toSession.getId());
        onlineSessions.remove(toSession.getId());
        onlineSessions.forEach((id, session) -> {
            try {
                if (!id.equals(toSession.getId())) {
                    session.getSession().getBasicRemote().sendText(String.format("[用户%s]退出了聊天室", userDO.getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("有一连接关闭：{}，当前在线人数为：{}", toSession.getId(), onlineSessions.size());
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        this.sendMessage(message, session);
        log.info("服务端收到客户端[{}]的消息:{}", session.getId(), message);
    }

    @OnError
    public void OnError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            onlineSessions.forEach((id, session) -> {
                try {
                    if (!id.equals(toSession.getId())) {
                        session.getSession().getBasicRemote().sendText(String.format("[用户%s]发送消息:%s", session.getName(), message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
//            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }
}
