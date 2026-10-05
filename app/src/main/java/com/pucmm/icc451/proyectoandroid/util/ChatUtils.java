package com.pucmm.icc451.proyectoandroid.util;

public class ChatUtils {

    public static String getChatId(String id1, String id2) {
        if (id1.compareTo(id2) < 0) return id1 + "_" + id2;
        return id2 + "_" + id1;
    }

}
