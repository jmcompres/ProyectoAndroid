package com.pucmm.icc451.proyectoandroid.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String messageId;
    private String chatId;
    private String text;
    private String senderId;
    private String senderName;
    private long timestamp;
    private String imageUrl;
}
