package com.pucmm.icc451.proyectoandroid.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private String id;
    private String text;
    private String senderId;
    private long timestamp;
}
