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
        String pathString = filePath.toString();
        String[] split = filePath.toString().split("/");
        // 得到文件夹名称
        if (filePath.toFile().isDirectory() && !pathString.endsWith("/")){
            // 当是文件夹且不以 / 作为结尾时 返回此结果
            return split[split.length - 1];
        } else return split[split.length - 2];
    }

    public static boolean suitTemplate(String str, String template) {
        int strIndex = 0;
        int templateIndex = 0;
        while (strIndex < str.length() && templateIndex < template.length()) {
            if (str.charAt(strIndex) == template.charAt(templateIndex)) {
                strIndex++;
                templateIndex++;
            } else {
                if (template.charAt(templateIndex) == '%'){
                    templateIndex++;
                    if (templateIndex < template.length()){
                        switch (template.charAt(templateIndex)){
                            // TODO 目前仅支持%d作为占位符
                            case 'd':
                                while (strIndex < str.length() && str.charAt(strIndex) >= '0' && str.charAt(strIndex) <= '9'){
                                    strIndex++;
                                }
                                templateIndex++;
                                break;
                            default:
                                return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
