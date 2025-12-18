package com.yifei.tools.app.TengXunHuiYi;

import com.yifei.tools.image.matcher.ImageMatchConfig;
import com.yifei.tools.image.matcher.ImageMatcher;
import com.yifei.tools.image.matcher.MatchResult;
import com.yifei.tools.scheduler.SimpleScheduler;
import com.yifei.tools.utils.Logger;
import com.yifei.tools.windows.MouseUtil;

import java.awt.Point;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 腾讯会议自动入会工具
 * 每天早上6:55定时执行，自动匹配并点击入会按钮
 * 
 * @author yifei
 * @version 1.0
 */
public class TengXunHuiYiAutoJoin {
    
    // 模板图片路径
    private static final String TENGXUN_HUIYI_TEMPLATE = "src/main/resources/TengXunHuiYi/TengXunHuiYi.png";
    private static final String RUHUI_BUTTON_TEMPLATE = "src/main/resources/TengXunHuiYi/TengXunHuiYi-RuHui.png";
    
    // 配置参数
    private static final int WAIT_SECONDS_BETWEEN_MATCH = 3; // 两次匹配之间等待秒数
    private static final int MAX_RETRY_TIMES = 5; // 最大重试次数
    private static final double MATCH_THRESHOLD = 0.8; // 匹配阈值
    
    public static void main(String[] args) {
        Logger.separator("腾讯会议自动入会工具");
        
        // 检查必要的组件是否可用
        if (!checkPrerequisites()) {
            Logger.error("系统检查失败，程序退出");
            return;
        }
        
        Logger.info("系统检查通过，开始设置定时任务...");
        Logger.info("定时任务：每天早上6:55执行腾讯会议自动入会");
        Logger.info("当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Logger.info("日志文件: " + Logger.getCurrentLogFile());
        
        // 创建自动入会任务
        Runnable autoJoinTask = () -> {
            try {
                executeAutoJoinProcess();
            } catch (Exception e) {
                Logger.error("自动入会任务执行异常: " + e.getMessage(), e);
            }
        };
        
        // 设置每天6:55执行的定时任务
        SimpleScheduler.runDailyAtTime(autoJoinTask, 6, 55, -1); // -1表示无限执行
    }
    
    /**
     * 检查系统前置条件
     * 
     * @return 是否满足运行条件
     */
    private static boolean checkPrerequisites() {
        Logger.info("正在检查系统前置条件...");
        
        // 检查OpenCV是否加载
        if (!ImageMatcher.isOpenCVLoaded()) {
            Logger.error("OpenCV库未加载");
            return false;
        }
        Logger.success("OpenCV库已加载");
        
        // 检查Robot是否初始化
        if (!MouseUtil.isRobotInitialized()) {
            Logger.error("Robot未初始化，无法执行鼠标操作");
            return false;
        }
        Logger.success("鼠标操作组件已初始化");
        
        // 检查模板图片是否存在
        File tengxunTemplate = new File(TENGXUN_HUIYI_TEMPLATE);
        if (!tengxunTemplate.exists()) {
            Logger.error("腾讯会议模板图片不存在: " + TENGXUN_HUIYI_TEMPLATE);
            return false;
        }
        Logger.success("腾讯会议模板图片存在");
        
        File ruhuiTemplate = new File(RUHUI_BUTTON_TEMPLATE);
        if (!ruhuiTemplate.exists()) {
            Logger.error("入会按钮模板图片不存在: " + RUHUI_BUTTON_TEMPLATE);
            return false;
        }
        Logger.success("入会按钮模板图片存在");
        
        return true;
    }
    
    /**
     * 执行自动入会流程
     */
    private static void executeAutoJoinProcess() {
        Logger.taskStart("腾讯会议自动入会任务");
        
        boolean taskSuccess = false;
        
        try {
            // 第一步：查找腾讯会议图标并打开软件
            Logger.info("第一步：查找腾讯会议图标...");
            boolean iconFound = findAndOpenTengXunHuiYi();
            
            if (!iconFound) {
                Logger.failure("未找到腾讯会议图标，任务失败");
                return;
            }
            
            // 等待软件启动
            Logger.info("等待腾讯会议软件启动...");
            Thread.sleep(10000); // 等待10秒让软件完全启动
            
            // 第二步：等待软件界面稳定
            Logger.info("第二步：等待软件界面稳定...");
            Thread.sleep(WAIT_SECONDS_BETWEEN_MATCH * 1000);
            
            // 第三步：匹配并点击入会按钮
            Logger.info("第三步：查找并点击入会按钮...");
            boolean clickSuccess = findAndClickRuHuiButton();
            
            if (clickSuccess) {
                Logger.success("腾讯会议自动入会任务执行成功！");
                Logger.info("已成功打开软件并点击入会按钮");
                taskSuccess = true;
            } else {
                Logger.failure("腾讯会议自动入会任务执行失败");
                Logger.error("未能找到或点击入会按钮");
            }
            
        } catch (InterruptedException e) {
            Logger.error("任务被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Logger.error("任务执行异常: " + e.getMessage(), e);
        } finally {
            Logger.taskEnd("腾讯会议自动入会任务", taskSuccess);
        }
    }
    
    /**
     * 等待图像匹配成功
     * 
     * @param templatePath 模板图片路径
     * @param description 描述信息
     * @param maxRetries 最大重试次数
     * @return 是否匹配成功
     */
    private static boolean waitForImageMatch(String templatePath, String description, int maxRetries) {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("尝试匹配 " + description + " (第" + attempt + "/" + maxRetries + "次)");
            
            MatchResult result = ImageMatcher.matchImage(templatePath, config);
            
            if (result.isMatched()) {
                System.out.println("✓ 成功找到 " + description);
                System.out.println("  位置: (" + result.getLocation().x + ", " + result.getLocation().y + ")");
                System.out.println("  置信度: " + String.format("%.4f", result.getConfidence()));
                return true;
            } else {
                System.out.println("✗ 未找到 " + description + " (置信度: " + 
                                 String.format("%.4f", result.getConfidence()) + ")");
                
                if (attempt < maxRetries) {
                    try {
                        System.out.println("等待2秒后重试...");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 查找并打开腾讯会议软件
     * 
     * @return 是否成功打开
     */
    private static boolean findAndOpenTengXunHuiYi() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= MAX_RETRY_TIMES; attempt++) {
            System.out.println("尝试查找腾讯会议图标 (第" + attempt + "/" + MAX_RETRY_TIMES + "次)");
            
            MatchResult result = ImageMatcher.matchImage(TENGXUN_HUIYI_TEMPLATE, config);
            
            if (result.isMatched()) {
                System.out.println("✓ 成功找到腾讯会议图标");
                System.out.println("  位置: (" + result.getLocation().x + ", " + result.getLocation().y + ")");
                System.out.println("  置信度: " + String.format("%.4f", result.getConfidence()));
                
                // 获取图标中心点
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    System.out.println("  中心点: (" + centerPoint.x + ", " + centerPoint.y + ")");
                    
                    // 平滑移动鼠标到图标位置
                    System.out.println("正在移动鼠标到腾讯会议图标...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    // 短暂延迟确保鼠标到位
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // 双击打开软件
                    System.out.println("正在双击打开腾讯会议软件...");
                    MouseUtil.doubleClick();
                    
                    System.out.println("✓ 已成功双击腾讯会议图标");
                    return true;
                } else {
                    System.err.println("✗ 无法获取图标中心点");
                }
            } else {
                System.out.println("✗ 未找到腾讯会议图标 (置信度: " + 
                                 String.format("%.4f", result.getConfidence()) + ")");
                
                if (attempt < MAX_RETRY_TIMES) {
                    try {
                        System.out.println("等待2秒后重试...");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 查找并点击入会按钮
     * 
     * @return 是否成功点击
     */
    private static boolean findAndClickRuHuiButton() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= MAX_RETRY_TIMES; attempt++) {
            System.out.println("尝试查找入会按钮 (第" + attempt + "/" + MAX_RETRY_TIMES + "次)");
            
            MatchResult result = ImageMatcher.matchImage(RUHUI_BUTTON_TEMPLATE, config);
            
            if (result.isMatched()) {
                System.out.println("✓ 成功找到入会按钮");
                System.out.println("  位置: (" + result.getLocation().x + ", " + result.getLocation().y + ")");
                System.out.println("  置信度: " + String.format("%.4f", result.getConfidence()));
                
                // 获取按钮中心点
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    System.out.println("  中心点: (" + centerPoint.x + ", " + centerPoint.y + ")");
                    
                    // 平滑移动鼠标到按钮中心并点击
                    System.out.println("正在移动鼠标到按钮位置...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    // 短暂延迟确保鼠标到位
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // 执行点击
                    System.out.println("正在点击入会按钮...");
                    MouseUtil.leftClick();
                    
                    System.out.println("✓ 已成功点击入会按钮");
                    return true;
                } else {
                    System.err.println("✗ 无法获取按钮中心点");
                }
            } else {
                System.out.println("✗ 未找到入会按钮 (置信度: " + 
                                 String.format("%.4f", result.getConfidence()) + ")");
                
                if (attempt < MAX_RETRY_TIMES) {
                    try {
                        System.out.println("等待2秒后重试...");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 创建图像匹配配置
     * 
     * @return 匹配配置
     */
    private static ImageMatchConfig createMatchConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        config.setThreshold(MATCH_THRESHOLD);
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(true);
        config.setGaussianKernelSize(3);
        config.setGaussianSigmaX(0.8);
        config.setGaussianSigmaY(0.8);
        
        // 可选：保存匹配过程图片用于调试
        config.setSaveProcessImages(false);
        config.setOutputDir("tengxun_match_results");
        
        return config;
    }
}