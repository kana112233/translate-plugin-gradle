package org.py.translate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.py.translate.task.MyTask;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.ui.SettingDialog;
import org.py.translate.util.ReadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
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
        if (GlobalConfig.isRunning()) {
            return;
        }
        GlobalConfig.setRunning(true);

        Project project = event.getProject();
        //            assert project != null;
        System.out.println(project.getBasePath() );

        new BackgroundTaskQueue(project, "myTaskName")
                .run(new MyTask(project, event, "myTaskName"));


    }




}
