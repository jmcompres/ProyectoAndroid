package com.pucmm.icc451.proyectoandroid.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatUtils {

    public static String getChatId(String id1, String id2) {
        if (id1.compareTo(id2) < 0) return id1 + "_" + id2;
        return id2 + "_" + id1;
    }

    public static String formatTimeStamp(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timeStamp));
    }

}
