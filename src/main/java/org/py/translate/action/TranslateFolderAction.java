package org.py.translate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.project.Project;
import org.py.translate.constant.GlobalConfig;
import org.py.translate.task.MyTask;

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
