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
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.py.translate.action.TranslateJob;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.markdown.renderer.MarkdownContentRenderer;
import org.py.translate.util.MarkdownUtil;
import org.py.translate.util.MyCallback;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
        StringBuilder yamlHead = new StringBuilder();
        StringBuilder translateBody = new StringBuilder();

        int endFlag = -1; // -1 -2
        String[] split = selectText.split("\n");
        if (split.length > 0 && "---".equals(split[0])) {
            for (int i = 1; i < split.length; i++) {
                if ("---".equals(split[i])) {
                    endFlag = i;
                    break;
                }
            }
        } else {
//            yamlHead = "";
            endFlag = -2;
        }
        if (-1 == endFlag) {
            return;
        } else if (-2 == endFlag) {
            translateBody.append(selectText);
        } else {
            for (int i = 0; i <= endFlag; i++) {
                yamlHead.append(split[i]).append("\n");
            }
            yamlHead.append("\n");
            for (int i = endFlag + 1; i < split.length; i++) {
                translateBody.append(split[i]).append("\n");
            }

        }

        Parser parser = Parser.builder().build();
        Node node = parser.parse(translateBody.toString());
        MarkdownContentRenderer renderer = MarkdownContentRenderer.builder().build();
        String render = renderer.render(node);
        System.out.println(render);


        //获取文件名称
        File file = new File(filePath);
        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write((yamlHead.append(render).toString() ).getBytes(StandardCharsets.UTF_8) );
        } catch (IOException e) {
            e.printStackTrace();
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
