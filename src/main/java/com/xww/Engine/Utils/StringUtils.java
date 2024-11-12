package com.xww.Engine.Utils;

public class StringUtils {
    public static String getTargetAccuracy(double dot, int accuracy) {
        String res = dot + "";
        int index = res.indexOf(".");
        return res.substring(0, Math.min(index + accuracy + 1, res.length()));
    }

    public static String format(String template, Object... args){
        return template.replace("%d", args[0].toString());
    }
}
