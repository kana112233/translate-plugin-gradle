package org.py.translate.util;

import entity.Code;
import entity.Heading;
import entity.MARK;
import entity.Paragraph;
import org.py.translate.action.TranslateJob;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.py.translate.util.MyUtil.getCorrectString;
import static org.py.translate.util.MyUtil.getNoTranslateFlag;

public class MarkdownUtil {
    public static String parse(String selectText, CallbackMk callbackMk){

        String[] strings = selectText.split("\n\n");
//        StringBuilder translateString = new StringBuilder();
        List tokenlist = new  ArrayList();
        getTranslateString(strings, tokenlist);

        callbackMk.run();
        return "";
    }

    public static void main(String[] args) {

        String selectText = "# 我是井号"+"\n\n";
        selectText += "## 我是井号"+"\n\n";
        selectText += "```"+"\n\n";
        selectText += "xxx112"+"\n\n";
        selectText += "222"+"\n\n";
        selectText += "333"+"\n\n";
        selectText += "```"+"\n\n";
        MarkdownUtil.parse(selectText, new MyCallback());
    }

    //解析
    private static void getTranslateString(String[] strings, List tokenlist) {
//        List tokenlist = new  ArrayList();
//        list.add(new Code().setLang(""));
        int index = 0;
        while (true) {
            if( index >= strings.length ){
                break;
            }
            // 空格
            if(strings[index].length()<=0){
                tokenlist.add(new Paragraph().setText(strings[index++]) );
                continue;
            }
            if(strings[index].startsWith(MARK.H1.getStart())){
                tokenlist.add(new Heading().setDepth(1).setText(strings[index++].substring(MARK.H1.getStart().length()) ) );
                continue;
            }
            if(strings[index].startsWith(MARK.H2.getStart() )){
                tokenlist.add(new Heading().setDepth(2).setText(strings[index++].substring(MARK.H2.getStart().length()) ) );
                continue;
            }
            if(strings[index].startsWith(MARK.H3.getStart())){
                tokenlist.add(new Heading().setDepth(3).setText(strings[index++].substring(MARK.H3.getStart().length()) ) );
                continue;
            }
            if(strings[index].startsWith(MARK.H4.getStart())){
                tokenlist.add(new Heading().setDepth(4).setText(strings[index++].substring(MARK.H4.getStart().length())) );
                continue;
            }
            if(strings[index].startsWith(MARK.H5.getStart())){
                tokenlist.add(new Heading().setDepth(5).setText(strings[index++].substring(MARK.H5.getStart().length())) );
                continue;
            }
            if(strings[index].startsWith(MARK.H6.getStart())){
                tokenlist.add(new Heading().setDepth(6).setText(strings[index++].substring(MARK.H6.getStart().length())) );
                continue;
            }

            //code
            boolean isFind = strings[index].startsWith("```");
            if (isFind) {
                String codeStr = strings[index] + "\n";
                int max = 0;
                while (true){
                    if(max>99) break;
                    max++;
                    index++;

                    boolean sFind = strings[index].startsWith("```");
                    codeStr += strings[index] + "\n";
                    if(sFind){
                        break;
                    }
                }
                tokenlist.add(new Code().setText(codeStr) );
                index++;
                continue;
            }
            //end code

        }
        tokenlist.forEach(item-> System.out.println(item.toString()));
    }

    // 渲染
    private static void  getTranslateString23(String[] strings, StringBuilder translateString) {
        List list = new  ArrayList();
        list.add(new Code().setLang(""));
        boolean isFind = false;
        for (String string : strings) {
            //跳过
            Pattern p = compile("```");
            Matcher m = p.matcher(string.trim());
            int count = 0;
            while(m.find()){
                count++;
            }
            if(count >= 2){
                translateString.append(string).append("\n\n");
                continue;
            }
            if(count == 1){
                //发现```时跳过
                isFind = !isFind;
                translateString.append(string+"\n\n");
                continue;
            }
            if (isFind) {
                //```之间跳过
                translateString.append(string).append("\n\n");
                continue;
            }

            boolean isContinue = false;
            List<String> noTranslateFlag= getNoTranslateFlag();
            for (String flag : noTranslateFlag) {
                Pattern p3 = compile(flag);
                Matcher m3 = p3.matcher(string.trim());
                if(m3.find()){
                    translateString.append(string).append("\n\n");
                    isContinue = true;
                    break;
                }
            }
            if(isContinue){
                continue;
            }

            String translateText = TranslateJob.parseString(null, string)+"\n\n";

            String backString = getCorrectString(translateText);

            translateString.append(backString);
            System.out.println(backString);
        }
    }


}

