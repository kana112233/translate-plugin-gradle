package org.py.translate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author hj
 * @date 2019/5/2
 */
public class TranslateFolderAction extends AnAction {

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    TranslateJob translateJob = new TranslateJob();

    @Override
    public void actionPerformed(AnActionEvent event) {
        if (GlobalConfig.isRunning()) {
            return;
        }
        GlobalConfig.setRunning(true);

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Project project = event.getProject();
                System.out.println(project.getBasePath() );

                // 获取当前选择的文件或文件夹路径
                //多选文件、文件夹
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
                startMultiThreadTranslate(fileList, 10);

                GlobalConfig.setRunning(false);
            }
        });
    }
    //多线程
    private void startMultiThreadTranslate(List<File> fileList, int n){
        int count = fileList.size() / n + 1;
        for (int i = 0; i < n; i++) {
            int finalI = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    int endCount = count*(finalI+1) - 1;
                    int end = fileList.size() <= endCount ? fileList.size() : endCount;
                    for (int j = count* finalI; j <= end ; j++) {
                        File translateFile = fileList.get(j);
                        System.err.println(Thread.currentThread().getName() + "正在执行。。。");
                        System.err.println("count "+j);
                        doTranslate(translateFile);

                    }
                }
            });
        }
    }

    private void doTranslate(File translateFile) {
        String filePath = translateFile.getAbsolutePath();
        System.out.println("翻译结果路径" + filePath);
        String selectText = ReadFile.readFile(filePath);
        String[] strings = selectText.split("\n\n");
        StringBuilder translateString = new StringBuilder();
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
                translateString.append(string+"\n\n");
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
                translateString.append(string+"\n\n");
                continue;
            }

            boolean isContinue = false;
            List<String> noTranslateFlag= getNoTranslateFlag();
            for (String flag : noTranslateFlag) {
                Pattern p3 = compile(flag);
                Matcher m3 = p3.matcher(string.trim());
                if(m3.find()){
                    translateString.append(string+"\n\n");
                    isContinue = true;
                    break;
                }
            }
            if(isContinue){
                continue;
            }

            String translateText = translateJob.parseString(null, string)+"\n\n";

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

            translateString.append(backString);
            System.out.println(backString);
        }
        //获取文件名称
        File file = new File(filePath);
        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write(translateString.toString().getBytes("UTF-8") );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getNoTranslateFlag(){
        List<String> noTranslateFlag = new ArrayList();
        //noTranslateFlag.add("```");
        noTranslateFlag.add("\\|");
        noTranslateFlag.add("---");
        //noTranslateFlag.add("\\[.*]\\(.*\\)");
        return noTranslateFlag;
    }

}
