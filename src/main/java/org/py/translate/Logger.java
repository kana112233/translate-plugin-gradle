package org.py.translate;

import com.intellij.notification.*;
import org.py.translate.constant.Contstants;

/**
 * Logger类
 * @author tangyouzhi
 * 部分参照EcTranslate实现
 */
public class Logger {

    /**
     * Logger name
     */
    private static String NAME;

    /**
     * Logger Level
     */
    private static int LEVEL;

    /**
     * INFO Level
     */
    private static final int INFO = 1;

    /**
     * Error Level
     */
    private static final int ERROR = 2;

    static {
        LEVEL = 1;
    }

    /**
     * 初始化注册日志函数
     * @param name
     * @param level
     */
    public static void init(final String name, final int level) {
        Logger.NAME = name;
        Logger.LEVEL = level;
        NotificationsConfiguration.getNotificationsConfiguration().register(Logger.NAME, NotificationDisplayType.NONE);
    }

    /**
     * 正常显示
     * @param opeName
     * @param text
     */
    public static void info(final String opeName,final String text) {
        if (Logger.LEVEL == 1) {
            Notifications.Bus.notify(new Notification(Logger.NAME, Logger.NAME+(opeName.equals(Contstants.CN_TO_EN)?" 中译英 ":" 英译中 ")+ " [INFO]", text, NotificationType.INFORMATION));

        }
    }

    /**
     * 错误信息
     * @param text
     */
    public static void error(final String text) {
        if (Logger.LEVEL == 0) {
            Notifications.Bus.notify(new Notification(Logger.NAME, Logger.NAME + " [ERROR]", text, NotificationType.ERROR));
        }
    }

}
