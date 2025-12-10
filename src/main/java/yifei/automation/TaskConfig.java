package yifei.automation;

import java.io.IOException;
import java.util.Properties;

/**
 * 任务配置类
 * 用于存储和管理自动化任务的配置信息
 */
public class TaskConfig {
    
    // 定时任务配置
    private String scheduleTime; // 定时执行时间，格式：HH:mm:ss，如 "14:30:00"
    private boolean enableSchedule; // 是否启用定时任务
    
    // 桌面图标配置
    private String desktopIconPath; // 桌面图标图片路径
    private double iconMatchThreshold; // 图标匹配阈值
    
    // 软件窗口配置
    private String windowTitle; // 软件窗口标题（部分匹配）
    private String windowProcess; // 软件进程名（可选）
    private int waitWindowTimeout; // 等待窗口出现的超时时间（秒）
    
    // 目标图片配置
    private String targetImagePath; // 软件内要匹配的目标图片路径
    private double targetMatchThreshold; // 目标图片匹配阈值
    
    // 点击配置
    private int clickDelay; // 点击延迟（毫秒）
    private boolean doubleClick; // 是否双击
    
    private static final String CONFIG_FILE = "/automation_config.properties";
    
    public TaskConfig() {
        // 默认配置
        this.scheduleTime = "09:00:00";
        this.enableSchedule = false;
        this.desktopIconPath = "";
        this.iconMatchThreshold = 0.8;
        this.windowTitle = "";
        this.windowProcess = "";
        this.waitWindowTimeout = 30;
        this.targetImagePath = "";
        this.targetMatchThreshold = 0.8;
        this.clickDelay = 500;
        this.doubleClick = false;
    }
    
    /**
     * 从配置文件加载配置
     * 从resources目录加载配置文件
     */
    public static TaskConfig loadFromFile() {
        TaskConfig config = new TaskConfig();
        Properties props = new Properties();
        
        System.out.println("正在从resources目录加载配置文件...");
        
        // 直接从resources目录加载配置文件
        try (java.io.InputStream is = TaskConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                props.load(is);
                System.out.println("✓ 成功从resources目录加载配置文件");
            } else {
                System.err.println("✗ 无法找到resources目录下的配置文件: " + CONFIG_FILE);
                System.err.println("  请确保配置文件存在于: src/main/resources/automation_config.properties");
            }
        } catch (IOException e) {
            System.err.println("✗ 加载配置文件失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 解析配置
        String scheduleTimeProp = props.getProperty("schedule.time");
        String enableScheduleProp = props.getProperty("schedule.enable");
        
        // 如果读取到的值为null，使用默认值
        if (scheduleTimeProp == null || scheduleTimeProp.trim().isEmpty()) {
            scheduleTimeProp = config.scheduleTime;
        } else {
            // Properties可能会转义冒号，需要处理
            scheduleTimeProp = scheduleTimeProp.replace("\\:", ":");
        }
        
        config.scheduleTime = scheduleTimeProp;
        config.enableSchedule = enableScheduleProp != null ? Boolean.parseBoolean(enableScheduleProp) : false;
        config.desktopIconPath = props.getProperty("desktop.icon.path", "");
        config.iconMatchThreshold = Double.parseDouble(props.getProperty("desktop.icon.threshold", "0.8"));
        config.windowTitle = props.getProperty("window.title", "");
        config.windowProcess = props.getProperty("window.process", "");
        config.waitWindowTimeout = Integer.parseInt(props.getProperty("window.timeout", "30"));
        config.targetImagePath = props.getProperty("target.image.path", "");
        config.targetMatchThreshold = Double.parseDouble(props.getProperty("target.image.threshold", "0.8"));
        config.clickDelay = Integer.parseInt(props.getProperty("click.delay", "500"));
        config.doubleClick = Boolean.parseBoolean(props.getProperty("click.double", "false"));
        
        // 显示实际读取到的关键配置
        System.out.println("\n读取到的配置值:");
        System.out.println("  定时任务启用: " + config.enableSchedule);
        System.out.println("  执行时间: " + config.scheduleTime);
        System.out.println("  目标图片路径: " + (config.targetImagePath.isEmpty() ? "(未配置)" : config.targetImagePath));
        
        return config;
    }
    
    
    // Getters and Setters
    public String getScheduleTime() {
        return scheduleTime;
    }
    
    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    
    public boolean isEnableSchedule() {
        return enableSchedule;
    }
    
    public void setEnableSchedule(boolean enableSchedule) {
        this.enableSchedule = enableSchedule;
    }
    
    public String getDesktopIconPath() {
        return desktopIconPath;
    }
    
    public void setDesktopIconPath(String desktopIconPath) {
        this.desktopIconPath = desktopIconPath;
    }
    
    public double getIconMatchThreshold() {
        return iconMatchThreshold;
    }
    
    public void setIconMatchThreshold(double iconMatchThreshold) {
        this.iconMatchThreshold = iconMatchThreshold;
    }
    
    public String getWindowTitle() {
        return windowTitle;
    }
    
    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
    
    public String getWindowProcess() {
        return windowProcess;
    }
    
    public void setWindowProcess(String windowProcess) {
        this.windowProcess = windowProcess;
    }
    
    public int getWaitWindowTimeout() {
        return waitWindowTimeout;
    }
    
    public void setWaitWindowTimeout(int waitWindowTimeout) {
        this.waitWindowTimeout = waitWindowTimeout;
    }
    
    public String getTargetImagePath() {
        return targetImagePath;
    }
    
    public void setTargetImagePath(String targetImagePath) {
        this.targetImagePath = targetImagePath;
    }
    
    public double getTargetMatchThreshold() {
        return targetMatchThreshold;
    }
    
    public void setTargetMatchThreshold(double targetMatchThreshold) {
        this.targetMatchThreshold = targetMatchThreshold;
    }
    
    public int getClickDelay() {
        return clickDelay;
    }
    
    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
    }
    
    public boolean isDoubleClick() {
        return doubleClick;
    }
    
    public void setDoubleClick(boolean doubleClick) {
        this.doubleClick = doubleClick;
    }
}

