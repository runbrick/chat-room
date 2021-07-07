package com.runbrick.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;

/**
 * 用户
 */
@Data
@AllArgsConstructor
public class UserDO {

    private String name;
    private String id;
    private Session session;
}
