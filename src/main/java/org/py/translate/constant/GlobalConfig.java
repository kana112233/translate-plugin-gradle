package org.py.translate.constant;

/**
 * @author hj
 * @date 2019/5/4
 */
public class GlobalConfig {
    private static boolean isRunning = false;

    public static void setRunning(boolean running) {
        isRunning = running;
    }
    public static boolean isRunning(){
        return isRunning;
    }
}
