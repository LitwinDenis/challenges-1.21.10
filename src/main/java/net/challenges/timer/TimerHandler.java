package net.challenges.timer;

import static java.lang.String.format;

public class TimerHandler {
    public static boolean isTimerEnabled = false;
    public static boolean isRunning = false;
    public static long startTime = 0;

    public static String getFormattedTime(){
        if(!isRunning) return "";

        long currentTime = System.currentTimeMillis();
        long elapsedMillis = currentTime -startTime;
        long secondsTotal = elapsedMillis / 1000;

        long days = secondsTotal / 86400;
        long hours = (secondsTotal % 86400 ) / 3600;
        long minutes = (secondsTotal % 3600) / 60;
        long seconds = secondsTotal % 60;

        if (days > 0) {
            return String.format("%dd:%02dh:%02dm:%02ds", days, hours, minutes, seconds);
        }else {
            return String.format("%dh:%02dm:%02ds", hours, minutes, seconds);
        }
    }
}
