package org.py.translate.action;

import com.intellij.openapi.editor.Editor;
import org.py.translate.Logger;
import org.py.translate.constant.Contstants;
import org.py.translate.util.GoogleTranslateUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author hj
 * @date 2019/5/3
 */
public class TranslateJob {

    public static String parseString(Editor mEditor, String selectText) {
        //  \[.*]\(.*\)  当匹配到这个正则就截取，保存 以后在拼接
        if (null != selectText && !"".equals(selectText.trim())) {
            selectText = selectText.trim();
            if (selectText.trim().length() <= Contstants.MAX_FANYI_SIZE) {
                try {
                    Pattern p = compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(selectText.trim());
                    String opeName = m.find() ? Contstants.CN_TO_EN : Contstants.EN_TO_CN;
                    Thread.sleep(1L);
                    String result = GoogleTranslateUtil.translate(selectText, opeName, mEditor);
                    if (null == result) {
                        Logger.error("翻译错误,请重试!");
                        return "";
                    }
                    //Logger.info(opeName, result);
                    return result;
                } catch (Exception e) {
                    Logger.error("程序异常,请重试!");
                }
            } else {
                Logger.error("单次翻译长度不可超过" + Contstants.MAX_FANYI_SIZE + ".");
            }
        }
        return "";
    }

}
