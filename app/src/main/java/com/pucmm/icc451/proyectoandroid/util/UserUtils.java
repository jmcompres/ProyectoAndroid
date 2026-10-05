package com.pucmm.icc451.proyectoandroid.util;

public class UserUtils {

    public static String getInitials (String name) {
        if (name==null) return "";
        String initials = "";
        String[] parts = name.split(" ");
        int nParts = parts.length;
        if (nParts > 0 && !parts[0].isEmpty()) initials += parts[0].substring(0, 1).toUpperCase();
        if (nParts > 1 && !parts[nParts-1].isEmpty()) initials += parts[nParts-1].substring(0, 1).toUpperCase();
        return initials;
    }

}
