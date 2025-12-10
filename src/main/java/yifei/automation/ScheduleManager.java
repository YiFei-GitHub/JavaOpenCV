package yifei.automation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务管理器
 * 负责调度和执行定时任务
 */
public class ScheduleManager {
    
    private ScheduledExecutorService scheduler;
    private TaskConfig config;
    private AutomationTask task;
    
    public ScheduleManager(TaskConfig config) {
        this.config = config;
        this.task = new AutomationTask(config);
        this.scheduler = Executors.newScheduledThreadPool(2); // 增加线程池大小以支持多个定时任务
    }
    
    /**
     * 启动定时任务
     */
    public void start() {
        if (!config.isEnableSchedule()) {
            System.out.println("定时任务未启用");
            return;
        }
        
        String timeStr = config.getScheduleTime();
        LocalTime targetTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        System.out.println("定时任务已启动，将在每天 " + timeStr + " 执行");
        
        // 计算首次执行延迟
        long initialDelay = calculateInitialDelay(targetTime);
        
        // 每天执行一次
        scheduler.scheduleAtFixedRate(
            () -> {
                System.out.println("定时任务触发，开始执行...");
                task.execute();
            },
            initialDelay,
            24 * 60 * 60, // 24小时
            TimeUnit.SECONDS
        );
    }
    
    /**
     * 启动每分钟输出当前时间的任务
     * 这个方法应该在程序启动时调用，无论是否启用定时任务
     */
    public void startTimeOutputTask() {
        // 计算到下一分钟的延迟（秒）
        LocalDateTime now = LocalDateTime.now();
        int currentSecond = now.getSecond();
        long delayToNextMinute = 60 - currentSecond;
        
        // 立即输出一次当前时间
        System.out.println("当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 每分钟执行一次
        scheduler.scheduleAtFixedRate(
            () -> {
                LocalDateTime currentTime = LocalDateTime.now();
                System.out.println("当前时间: " + currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            },
            delayToNextMinute, // 延迟到下一分钟
            60, // 每60秒执行一次
            TimeUnit.SECONDS
        );
    }
    
    
    /**
     * 计算首次执行延迟（秒）
     */
    private long calculateInitialDelay(LocalTime targetTime) {
        LocalTime now = LocalTime.now();
        
        if (now.isBefore(targetTime)) {
            // 今天执行
            return java.time.Duration.between(now, targetTime).getSeconds();
        } else {
            // 明天执行
            return java.time.Duration.between(now, targetTime).getSeconds() + 24 * 60 * 60;
        }
    }
    
    /**
     * 立即执行一次任务（不等待定时）
     */
    public void executeNow() {
        System.out.println("立即执行任务...");
        task.execute();
    }
    
    /**
     * 停止定时任务
     */
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("定时任务已停止");
        }
    }
}

