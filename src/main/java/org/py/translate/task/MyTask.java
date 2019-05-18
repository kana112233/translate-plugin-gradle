package org.py.translate.task;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.py.translate.action.TranslateJob;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.py.translate.util.MyUtil.getCorrectString;
import static org.py.translate.util.MyUtil.getNoTranslateFlag;

/**
 * @author hj
 * @date 2019/5/16
 */
public class MyTask extends Task.Backgroundable {

    private AnActionEvent event;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public MyTask(@Nullable Project project, @Nullable AnActionEvent event, @Nls @NotNull String title) {
        super(project, title);
        this.event = event;
    }

    @Override
    public void run(@NotNull ProgressIndicator progressIndicator) {
//        ProgressManager.getInstance().executeNonCancelableSection(() -> {
            // 获取当前选择的文件或文件夹路径、多选文件、文件夹
            VirtualFile[] virtualFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(event.getDataContext());
            if(virtualFiles == null || virtualFiles.length == 0 ) {
                return;
            }
            List<File> fileList = new ArrayList<>();
            for (VirtualFile virtualFile : virtualFiles) {
                // 获取翻译路径
                String pathRoot = virtualFile.getPath();
                ReadFile.getFileList(fileList, pathRoot);
            }
            startMultiThreadTranslate(fileList, 5);

            GlobalConfig.setRunning(false);
//        });
    }

    //多线程
    private void startMultiThreadTranslate(List<File> fileList, int n){
        if (fileList.size() <= 10) {
            fixedThreadPool.execute(() -> {
                int end = fileList.size();
                for (int j = 0; j < end ; j++) {
                    File translateFile = fileList.get(j);
                    System.err.println(Thread.currentThread().getName() + "正在执行。。。");
                    System.err.println("count "+j);

                    doTranslate(translateFile);

                }
            });
        }else{
            int count = fileList.size() / n + 1; //12/5 +1 = 3
            for (int i = 0; i < n; i++) {
                int finalI = i;
                fixedThreadPool.execute(() -> {
                    int endCount = count * (finalI + 1) - 1;
                    //3*1-1=2   0
                    //3*2-1=5   3
                    // 3*3-1=8  6
                    // 3*4-1=11  9
                    // 3*5-1=14   12  12
                    int end = fileList.size()-1 <= endCount ? fileList.size()-1 : endCount;
                    for (int j = count* finalI; j <= end ; j++) {
                        File translateFile = fileList.get(j);
                        System.err.println(Thread.currentThread().getName() + "正在执行。。。");
                        System.err.println("count "+j);

                        doTranslate(translateFile);

                    }
                });
            }
        }

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
}
