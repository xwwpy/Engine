package com.xww.Engine.Utils;

import java.nio.file.Path;

public class StringUtils {
    public static String getTargetAccuracy(double dot, int accuracy) {
        String res = dot + "";
        int index = res.indexOf(".");
        return res.substring(0, Math.min(index + accuracy + 1, res.length()));
    }

    public static String format(String template, Object... args){
        return template.replace("%d", args[0].toString());
    }

    /**
     * 移除指定的后缀 当不包括后缀时返回原字符串
     * @param str 需要进行操作的字符串
     * @param fileSuffix 后缀
     * @return 移除后缀后的字符串
     */
    public static String removeSuffix(String str, String fileSuffix) {
        if (!str.endsWith(fileSuffix)) {
            return str;
        } else return str.substring(0, str.length() - fileSuffix.length());
    }

    /**
     *
     * @return 获取最后层文件夹名称
     */
    public static String getDictionaryName(Path filePath) {
        String[] split = filePath.toString().split("/");
        return split[split.length - 2];
    }
}
