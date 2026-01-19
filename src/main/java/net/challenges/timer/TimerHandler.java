package net.challenges.timer;

public class TimerHandler {
    public static boolean isTimerEnabled = false;
    public static boolean isRunning = false;

    public static long totalTicks = 0;

    public static void start(){
        isRunning = true;
        totalTicks = 0;
    }
    public static void update(boolean isGamePaused){
        if (!isRunning) return;
        long now = System.currentTimeMillis();
        if(!isGamePaused) {
            totalTicks++;
        }
    }

    public static String getFormattedTime(){
        if(!isRunning) return "";

        long secondsTotal = totalTicks / 20;

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
