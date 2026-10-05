package com.pucmm.icc451.proyectoandroid.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chat {
    private String chatId;
    private List<String> participantIds;
    private Map<String, String> participantNames;
    private String lastMessageText;
    private String lastMessageUserName;
    private long lastMessageTimestamp;
    private int unreadCount;
}