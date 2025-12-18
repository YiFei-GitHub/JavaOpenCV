package com.yifei.tools.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志工具类
 * 将日志同时输出到控制台和文件
 * 
 * @author yifei
 * @version 1.0
 */
public class Logger {
    
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "tengxun_huiyi_";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter LOG_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static PrintWriter fileWriter;
    private static String currentLogFile;
    
    static {
        initializeLogger();
    }
    
    /**
     * 初始化日志系统
     */
    private static void initializeLogger() {
        try {
            // 创建日志目录
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // 生成日志文件名
            String dateStr = LocalDateTime.now().format(FILE_DATE_FORMAT);
            currentLogFile = LOG_DIR + "/" + LOG_FILE_PREFIX + dateStr + ".log";
            
            // 创建文件写入器
            fileWriter = new PrintWriter(new FileWriter(currentLogFile, true));
            
            // 写入启动标记
            String startTime = LocalDateTime.now().format(LOG_TIME_FORMAT);
            fileWriter.println("============================================================");
            fileWriter.println("日志系统启动时间: " + startTime);
            fileWriter.println("============================================================");
            fileWriter.flush();
            
        } catch (IOException e) {
            System.err.println("初始化日志系统失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 输出普通日志信息
     * 
     * @param message 日志消息
     */
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] " + message;
        
        // 输出到控制台
        System.out.println(logMessage);
        
        // 输出到文件
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出信息级别日志
     * 
     * @param message 日志消息
     */
    public static void info(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [INFO] " + message;
        
        System.out.println(logMessage);
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出警告级别日志
     * 
     * @param message 日志消息
     */
    public static void warn(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [WARN] " + message;
        
        System.out.println(logMessage);
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出错误级别日志
     * 
     * @param message 日志消息
     */
    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [ERROR] " + message;
        
        System.err.println(logMessage);
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出错误级别日志（带异常信息）
     * 
     * @param message 日志消息
     * @param throwable 异常对象
     */
    public static void error(String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [ERROR] " + message;
        
        System.err.println(logMessage);
        if (throwable != null) {
            throwable.printStackTrace();
        }
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            if (throwable != null) {
                throwable.printStackTrace(fileWriter);
            }
            fileWriter.flush();
        }
    }
    
    /**
     * 输出成功日志
     * 
     * @param message 日志消息
     */
    public static void success(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [SUCCESS] ✓ " + message;
        
        System.out.println(logMessage);
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出失败日志
     * 
     * @param message 日志消息
     */
    public static void failure(String message) {
        String timestamp = LocalDateTime.now().format(LOG_TIME_FORMAT);
        String logMessage = "[" + timestamp + "] [FAILURE] ✗ " + message;
        
        System.out.println(logMessage);
        
        if (fileWriter != null) {
            fileWriter.println(logMessage);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出分隔线
     */
    public static void separator() {
        String separator = "============================================================";
        
        System.out.println(separator);
        
        if (fileWriter != null) {
            fileWriter.println(separator);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出分隔线（带标题）
     * 
     * @param title 标题
     */
    public static void separator(String title) {
        String separator = "==================== " + title + " ====================";
        
        System.out.println(separator);
        
        if (fileWriter != null) {
            fileWriter.println(separator);
            fileWriter.flush();
        }
    }
    
    /**
     * 输出任务开始日志
     * 
     * @param taskName 任务名称
     */
    public static void taskStart(String taskName) {
        separator();
        info("开始执行任务: " + taskName);
        info("执行时间: " + LocalDateTime.now().format(LOG_TIME_FORMAT));
        separator();
    }
    
    /**
     * 输出任务结束日志
     * 
     * @param taskName 任务名称
     * @param success 是否成功
     */
    public static void taskEnd(String taskName, boolean success) {
        separator();
        if (success) {
            success("任务执行成功: " + taskName);
        } else {
            failure("任务执行失败: " + taskName);
        }
        info("结束时间: " + LocalDateTime.now().format(LOG_TIME_FORMAT));
        separator();
    }
    
    /**
     * 获取当前日志文件路径
     * 
     * @return 日志文件路径
     */
    public static String getCurrentLogFile() {
        return currentLogFile;
    }
    
    /**
     * 关闭日志系统
     */
    public static void close() {
        if (fileWriter != null) {
            String endTime = LocalDateTime.now().format(LOG_TIME_FORMAT);
            fileWriter.println("============================================================");
            fileWriter.println("日志系统关闭时间: " + endTime);
            fileWriter.println("============================================================");
            fileWriter.close();
        }
    }
    
    /**
     * 添加JVM关闭钩子，确保日志正常关闭
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            close();
        }));
    }
}