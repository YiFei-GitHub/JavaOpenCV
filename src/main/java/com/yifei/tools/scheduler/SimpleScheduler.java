package com.yifei.tools.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 简单定时任务工具类
 * 单线程执行，支持固定间隔和固定时间执行
 * 
 * @author yifei
 * @version 1.0
 */
public class SimpleScheduler {
    
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 按固定间隔执行任务
     * 
     * @param task 要执行的任务
     * @param intervalSeconds 间隔秒数
     * @param maxExecutions 最大执行次数，-1表示无限执行
     */
    public static void runWithInterval(Runnable task, int intervalSeconds, int maxExecutions) {
        System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 开始定时任务，间隔: " + intervalSeconds + "秒");
        
        int executionCount = 0;
        
        while (maxExecutions == -1 || executionCount < maxExecutions) {
            try {
                System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 执行任务 #" + (executionCount + 1));
                task.run();
                executionCount++;
                
                if (maxExecutions != -1 && executionCount >= maxExecutions) {
                    break;
                }
                
                // 等待指定间隔
                Thread.sleep(intervalSeconds * 1000L);
                
            } catch (InterruptedException e) {
                System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 任务被中断");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 任务执行失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 定时任务结束，共执行 " + executionCount + " 次");
    }
    
    /**
     * 等待到指定时间执行任务
     * 
     * @param task 要执行的任务
     * @param targetHour 目标小时 (0-23)
     * @param targetMinute 目标分钟 (0-59)
     */
    public static void runAtTime(Runnable task, int targetHour, int targetMinute) {
        if (targetHour < 0 || targetHour > 23) {
            throw new IllegalArgumentException("小时必须在0-23之间");
        }
        if (targetMinute < 0 || targetMinute > 59) {
            throw new IllegalArgumentException("分钟必须在0-59之间");
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.withHour(targetHour).withMinute(targetMinute).withSecond(0).withNano(0);
        
        // 如果目标时间已过，设置为明天
        if (targetTime.isBefore(now) || targetTime.isEqual(now)) {
            targetTime = targetTime.plusDays(1);
        }
        
        long waitMillis = java.time.Duration.between(now, targetTime).toMillis();
        
        System.out.println("[" + now.format(TIME_FORMAT) + "] 等待到 " + 
                          targetTime.format(TIME_FORMAT) + " 执行任务");
        System.out.println("需要等待: " + formatDuration(waitMillis));
        
        try {
            Thread.sleep(waitMillis);
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 开始执行定时任务");
            task.run();
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 定时任务执行完成");
            
        } catch (InterruptedException e) {
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 等待被中断");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 任务执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 每天在指定时间执行任务
     * 
     * @param task 要执行的任务
     * @param targetHour 目标小时 (0-23)
     * @param targetMinute 目标分钟 (0-59)
     * @param maxDays 最大执行天数，-1表示无限执行
     */
    public static void runDailyAtTime(Runnable task, int targetHour, int targetMinute, int maxDays) {
        if (targetHour < 0 || targetHour > 23) {
            throw new IllegalArgumentException("小时必须在0-23之间");
        }
        if (targetMinute < 0 || targetMinute > 59) {
            throw new IllegalArgumentException("分钟必须在0-59之间");
        }
        
        System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 开始每日定时任务，执行时间: " + 
                          String.format("%02d:%02d", targetHour, targetMinute));
        
        int dayCount = 0;
        
        while (maxDays == -1 || dayCount < maxDays) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime targetTime = now.withHour(targetHour).withMinute(targetMinute).withSecond(0).withNano(0);
            
            // 如果今天的目标时间已过，设置为明天
            if (targetTime.isBefore(now) || targetTime.isEqual(now)) {
                targetTime = targetTime.plusDays(1);
            }
            
            long waitMillis = java.time.Duration.between(now, targetTime).toMillis();
            
            System.out.println("[" + now.format(TIME_FORMAT) + "] 等待到 " + 
                              targetTime.format(TIME_FORMAT) + " 执行任务 (第" + (dayCount + 1) + "天)");
            
            try {
                Thread.sleep(waitMillis);
                System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 执行每日任务 #" + (dayCount + 1));
                task.run();
                dayCount++;
                
            } catch (InterruptedException e) {
                System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 每日任务被中断");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 每日任务执行失败: " + e.getMessage());
                e.printStackTrace();
                dayCount++; // 即使失败也计入天数
            }
        }
        
        System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 每日定时任务结束，共执行 " + dayCount + " 天");
    }
    
    /**
     * 延迟执行任务
     * 
     * @param task 要执行的任务
     * @param delaySeconds 延迟秒数
     */
    public static void runAfterDelay(Runnable task, int delaySeconds) {
        System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 将在 " + delaySeconds + " 秒后执行任务");
        
        try {
            Thread.sleep(delaySeconds * 1000L);
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 开始执行延迟任务");
            task.run();
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 延迟任务执行完成");
            
        } catch (InterruptedException e) {
            System.out.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 延迟任务被中断");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[" + LocalDateTime.now().format(TIME_FORMAT) + "] 延迟任务执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 格式化持续时间
     * 
     * @param millis 毫秒数
     * @return 格式化的时间字符串
     */
    private static String formatDuration(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        
        if (hours > 0) {
            return String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d分钟%d秒", minutes, seconds);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}