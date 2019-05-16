package org.py.translate.util;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hj
 * @date 2019/5/16
 */
public class MyUtil {

    public static List<String> getNoTranslateFlag(){

        List<String>  noTranslateFlag =  new ArrayList<>();
        //noTranslateFlag.add("```");
        noTranslateFlag.add("\\|");
        noTranslateFlag.add("---");
        //noTranslateFlag.add("\\[.*]\\(.*\\)");
        return noTranslateFlag;
    }

    @NotNull
    public static String getCorrectString(String translateText) {
        String backString = translateText;
        //修正文本
        backString = backString.replaceAll("<! -","<!--");
        backString = backString.replaceAll("- >","-->");
        backString = backString.replaceAll("？","?");
        backString = backString.replaceAll("/\n","/");
        backString = backString.replaceAll("＃","#");
        backString = backString.replaceAll(" #","#");
        backString = backString.replaceAll(" /","/");
        backString = backString.replaceAll("：",":");
        backString = backString.replaceAll("！","!");
        backString = backString.replaceAll("。",".");
        backString = backString.replaceAll("，",",");
        backString = backString.replaceAll("\\（","\\(");
        backString = backString.replaceAll("\\）","\\)");

        backString = backString.replaceAll("（","(");
        backString = backString.replaceAll("）",")");

        backString = backString.replaceAll("\\ \\/\\ ","/");
        backString = backString.replaceAll("\\/\\ ","/");
        return backString;
    }

}
