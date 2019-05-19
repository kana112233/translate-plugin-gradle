package org.py.translate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.task.MyTask;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.py.translate.util.MyUtil.getCorrectString;
import static org.py.translate.util.MyUtil.getNoTranslateFlag;

/**
 * @author hj
 * @date 2019/5/2
 */
public class TranslateFolderAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
       /* if (GlobalConfig.isRunning()) {
            return;
        }
        GlobalConfig.setRunning(true);*/

        Project project = event.getProject();
        //            assert project != null;
        System.out.println(project.getBasePath() );

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

        if (fileList.size() <= 10) {
            new BackgroundTaskQueue(project, "myTaskName")
                    .run(new MyTask(project, event, "myTaskName",fileList,0, fileList.size()-1 ));
        }else {
            //12/5 +1 = 3
            int n = 5;
            //15 5 = 3
            int count = fileList.size() % n ==0? (fileList.size() / n) : (fileList.size() / n + 1);
            for (int i = 0; i < n; i++) {
                int endCount = count * (i + 1) - 1;
                //3*1-1=2   0
                //3*2-1=5   3 3
                // 3*3-1=8  6 3
                // 3*4-1=11  9 3
                // 3*5-1=14   12  12
                int end = fileList.size()-1 <= endCount ? fileList.size()-1 : endCount;
             /*   System.out.println("begin"+ count * i);
                System.out.println("end"+ end);*/
                new BackgroundTaskQueue(project, "myTaskName")
                        .run(new MyTask(project, event, "翻译任务"+(i+1),fileList,count* i, end));
            }
        }
    }
}
