package org.py.translate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hj
 * @date 2019/5/2
 */
public class TranslateFolderAction extends AnAction {

    TranslateJob translateJob = new TranslateJob();

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        System.out.println(project.getBasePath() );

        // 获取当前选择的文件或文件夹路径
        VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        if(virtualFile == null) {
            return;
        }
        // 获取翻译路径
        String pathRoot = virtualFile.getPath();
        List<File> fileList = new ArrayList<>();
        ReadFile.getFileList(fileList, pathRoot);

        fileList.forEach(translateFile -> {
            String filePath = translateFile.getAbsolutePath();
            System.out.println("翻译结果路径" + filePath);
            String selectText = ReadFile.readFile(filePath);
            String[] strings = selectText.split("\n\n");
            StringBuilder translateString = new StringBuilder();
            for (String string : strings) {
                String translateText = translateJob.parseString(null, string)+"\n\n";
                System.err.println(translateText);
                translateString.append(translateText);
            }
            //修正文本
            //1 批量修改中文括号到英文
            String backString = translateString.toString().replaceAll("（","(");
            backString = backString.replaceAll("）",")");

            //获取文件名称
            int index = filePath.lastIndexOf(".");
            String filePathName = filePath.substring(0, index);
                File file = new File(filePathName+".hj");
                try {
                    FileOutputStream os = new FileOutputStream(file);
                    os.write(backString.getBytes("UTF-8") );
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });

    }


}
