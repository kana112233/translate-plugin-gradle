package org.py.translate.task;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.py.translate.action.TranslateJob;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;
import static java.util.regex.Pattern.compile;
import static org.py.translate.util.MyUtil.getCorrectString;
import static org.py.translate.util.MyUtil.getNoTranslateFlag;

/**
 * @author hj
 * @date 2019/5/16
 */
public class MyTask extends Task.Backgroundable {

    private AnActionEvent event;
    private Project project;
    private ProgressIndicator progressIndicator;

    private List<File> fileList;
    private int begin;
    private int end;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public MyTask(@Nullable Project project, @Nullable AnActionEvent event, @Nls @NotNull String title
            ,List<File> fileList, int begin, int end) {
        super(project, title);
        this.event = event;
        this.project = project;

        this.fileList = fileList;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run(@NotNull ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
        progressIndicator.setText("开始翻译");
        progressIndicator.setIndeterminate(true);
        progressIndicator.setFraction(0.001);
        //3 0 1 2/3
        for (int j = this.begin; j <= this.end ; j++) {
            File translateFile = fileList.get(j);
            doTranslate(translateFile);
            int kk = j + 1 - begin;
            System.err.println("kk: "+kk);
            int gg = end - begin + 1;
            System.err.println("gg: "+gg);
            double fraction = new BigDecimal((float)  kk/gg ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.err.println("fraction: " + this.begin + "value: " + fraction);
            progressIndicator.setFraction( fraction );
            progressIndicator.setText("正在翻译");
        }
        showBar();
    }


    private void doTranslate(File translateFile) {
        String filePath = translateFile.getAbsolutePath();
        System.out.println("翻译结果路径" + filePath);
        String selectText = ReadFile.readFile(filePath);
        String[] strings = selectText.split("\n\n");
        StringBuilder translateString = new StringBuilder();
        getTranslateString(strings, translateString);
        //获取文件名称
        File file = new File(filePath);
        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write(translateString.toString().getBytes("UTF-8") );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void getTranslateString(String[] strings, StringBuilder translateString) {
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


    private void showBar() {
        progressIndicator.setFraction(1.0);
        progressIndicator.setText("翻译完成");
        progressIndicator.setIndeterminate(false);

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(this.project);
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("翻译完成啦一个"
                , MessageType.INFO, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

}
