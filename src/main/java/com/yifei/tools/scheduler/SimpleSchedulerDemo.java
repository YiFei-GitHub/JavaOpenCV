package com.yifei.tools.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 简单定时任务演示类
 * 
 * @author yifei
 * @version 1.0
 */
public class SimpleSchedulerDemo {
    
    public static void main(String[] args) {

//        // 示例1: 延迟执行任务
//        System.out.println("\n1. 延迟执行演示（5秒后执行）");
//        Runnable delayTask = () -> {
//            System.out.println("延迟任务执行了！当前时间: " +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        };
//        SimpleScheduler.runAfterDelay(delayTask, 5);
//
//        // 示例2: 固定间隔执行任务
//        System.out.println("\n2. 固定间隔执行演示（每3秒执行一次，共执行5次）");
//        Runnable intervalTask = () -> {
//            System.out.println("间隔任务执行了！当前时间: " +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        };
//        SimpleScheduler.runWithInterval(intervalTask, 3, 5);
//
//        // 示例3: 截图任务演示
//        System.out.println("\n3. 定时截图演示（每5秒截图一次，共3次）");
//        Runnable screenshotTask = () -> {
//            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//            String filename = "screenshots/simple_" + timestamp + ".png";
//            boolean success = ScreenshotUtil.captureFullScreen(filename);
//            System.out.println("截图" + (success ? "成功" : "失败") + ": " + filename);
//        };
//        SimpleScheduler.runWithInterval(screenshotTask, 5, 3);
//
//        // 示例4: 指定时间执行（演示用，设置为当前时间1分钟后）
//        LocalDateTime now = LocalDateTime.now();
//        int targetHour = now.getHour();
//        int targetMinute = now.getMinute() + 1;
//        if (targetMinute >= 60) {
//            targetMinute = 0;
//            targetHour = (targetHour + 1) % 24;
//        }
//
//        System.out.println("\n4. 指定时间执行演示（" +
//            String.format("%02d:%02d", targetHour, targetMinute) + "执行）");
//        Runnable timeTask = () -> {
//            System.out.println("定时任务执行了！预定时间到达！");
//            System.out.println("当前时间: " +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        };
//        SimpleScheduler.runAtTime(timeTask, targetHour, targetMinute);
//
        // 示例5: 每天7点执行（演示用，只执行1天）
        Runnable dailyTask = () -> {
            System.out.println("每日任务执行了！");
            System.out.println("执行时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // 可以在这里添加具体的业务逻辑，比如：
        };
         SimpleScheduler.runDailyAtTime(dailyTask, 16, 04, -1); // 每天定点执行，-1每天都执行
        
    }
}