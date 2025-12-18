package com.yifei.restful.opencv.service;

import com.yifei.tools.image.matcher.ImageMatcher;
import com.yifei.tools.image.matcher.ImageMatchConfig;
import com.yifei.tools.image.matcher.MatchResult;
import com.yifei.tools.windows.MouseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.Point;
import java.io.File;

/**
 * 腾讯会议自动入会服务
 * 使用SpringBoot的定时任务功能，每天早上6:55自动执行
 * 
 * @author yifei
 * @version 1.0
 */
@Service
@ConditionalOnProperty(name = "opencv.schedule.enabled", havingValue = "true", matchIfMissing = true)
public class TengXunHuiYiService {
    
    private static final Logger logger = LoggerFactory.getLogger(TengXunHuiYiService.class);
    
    @Value("${opencv.templates.tengxun-huiyi}")
    private String tengxunHuiyiTemplate;
    
    @Value("${opencv.templates.ruhui-button}")
    private String ruhuiButtonTemplate;
    
    @Value("${opencv.match.threshold:0.8}")
    private double matchThreshold;
    
    @Value("${opencv.match.max-retry-times:5}")
    private int maxRetryTimes;
    
    @Value("${opencv.match.wait-seconds:3}")
    private int waitSeconds;
    
    /**
     * 服务初始化检查
     */
    @PostConstruct
    public void init() {
        logger.info("=== 腾讯会议自动入会服务初始化 ===");
        
        if (!checkPrerequisites()) {
            logger.error("系统检查失败，服务将不可用");
            return;
        }
        
        logger.info("系统检查通过，服务已就绪");
        logger.info("定时任务配置: 每天早上6:55执行");
        logger.info("匹配阈值: {}", matchThreshold);
        logger.info("最大重试次数: {}", maxRetryTimes);
    }
    
    /**
     * 定时任务：每天早上6:55执行腾讯会议自动入会
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "${opencv.schedule.cron:0 55 6 * * ?}")
    public void executeAutoJoin() {
        logger.info("==================== 腾讯会议自动入会任务开始 ====================");
        
        boolean taskSuccess = false;
        
        try {
            // 第一步：查找腾讯会议图标并打开软件
            logger.info("第一步：查找腾讯会议图标...");
            boolean iconFound = findAndOpenTengXunHuiYi();
            
            if (!iconFound) {
                logger.error("未找到腾讯会议图标，任务失败");
                return;
            }
            
            // 等待软件启动
            logger.info("等待腾讯会议软件启动...");
            Thread.sleep(10000); // 等待10秒让软件完全启动
            
            // 第二步：等待软件界面稳定
            logger.info("第二步：等待软件界面稳定...");
            Thread.sleep(waitSeconds * 1000);
            
            // 第三步：匹配并点击入会按钮
            logger.info("第三步：查找并点击入会按钮...");
            boolean clickSuccess = findAndClickRuHuiButton();
            
            if (clickSuccess) {
                logger.info("✓ 腾讯会议自动入会任务执行成功！");
                logger.info("已成功打开软件并点击入会按钮");
                taskSuccess = true;
            } else {
                logger.error("✗ 腾讯会议自动入会任务执行失败");
                logger.error("未能找到或点击入会按钮");
            }
            
        } catch (InterruptedException e) {
            logger.error("任务被中断: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("任务执行异常: {}", e.getMessage(), e);
        } finally {
            if (taskSuccess) {
                logger.info("==================== 腾讯会议自动入会任务成功结束 ====================");
            } else {
                logger.error("==================== 腾讯会议自动入会任务失败结束 ====================");
            }
        }
    }
    
    /**
     * 手动执行自动入会任务（用于测试）
     */
    public void manualExecute() {
        logger.info("手动触发腾讯会议自动入会任务");
        executeAutoJoin();
    }
    
    /**
     * 检查系统前置条件
     * 
     * @return 是否满足运行条件
     */
    private boolean checkPrerequisites() {
        logger.info("正在检查系统前置条件...");
        
        // 检查OpenCV是否加载
        if (!ImageMatcher.isOpenCVLoaded()) {
            logger.error("OpenCV库未加载");
            return false;
        }
        logger.info("✓ OpenCV库已加载");
        
        // 检查Robot是否初始化
        if (!MouseUtil.isRobotInitialized()) {
            logger.error("Robot未初始化，无法执行鼠标操作");
            return false;
        }
        logger.info("✓ 鼠标操作组件已初始化");
        
        // 检查模板图片是否存在
        File tengxunTemplate = new File(tengxunHuiyiTemplate);
        if (!tengxunTemplate.exists()) {
            logger.error("腾讯会议模板图片不存在: {}", tengxunHuiyiTemplate);
            return false;
        }
        logger.info("✓ 腾讯会议模板图片存在: {}", tengxunHuiyiTemplate);
        
        File ruhuiTemplate = new File(ruhuiButtonTemplate);
        if (!ruhuiTemplate.exists()) {
            logger.error("入会按钮模板图片不存在: {}", ruhuiButtonTemplate);
            return false;
        }
        logger.info("✓ 入会按钮模板图片存在: {}", ruhuiButtonTemplate);
        
        return true;
    }
    
    /**
     * 查找并打开腾讯会议软件
     * 
     * @return 是否成功打开
     */
    private boolean findAndOpenTengXunHuiYi() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= maxRetryTimes; attempt++) {
            logger.info("尝试查找腾讯会议图标 (第{}/{}次)", attempt, maxRetryTimes);
            
            MatchResult result = ImageMatcher.matchImage(tengxunHuiyiTemplate, config);
            
            if (result.isMatched()) {
                logger.info("✓ 成功找到腾讯会议图标");
                logger.info("  位置: ({}, {})", result.getLocation().x, result.getLocation().y);
                logger.info("  置信度: {}", String.format("%.4f", result.getConfidence()));
                
                // 获取图标中心点
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    logger.info("  中心点: ({}, {})", centerPoint.x, centerPoint.y);
                    
                    // 平滑移动鼠标到图标位置
                    logger.info("正在移动鼠标到腾讯会议图标...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    // 短暂延迟确保鼠标到位
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // 双击打开软件
                    logger.info("正在双击打开腾讯会议软件...");
                    MouseUtil.doubleClick();
                    
                    logger.info("✓ 已成功双击腾讯会议图标");
                    return true;
                } else {
                    logger.error("✗ 无法获取图标中心点");
                }
            } else {
                logger.warn("✗ 未找到腾讯会议图标 (置信度: {})", 
                           String.format("%.4f", result.getConfidence()));
                
                if (attempt < maxRetryTimes) {
                    try {
                        logger.info("等待2秒后重试...");
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
    private boolean findAndClickRuHuiButton() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= maxRetryTimes; attempt++) {
            logger.info("尝试查找入会按钮 (第{}/{}次)", attempt, maxRetryTimes);
            
            MatchResult result = ImageMatcher.matchImage(ruhuiButtonTemplate, config);
            
            if (result.isMatched()) {
                logger.info("✓ 成功找到入会按钮");
                logger.info("  位置: ({}, {})", result.getLocation().x, result.getLocation().y);
                logger.info("  置信度: {}", String.format("%.4f", result.getConfidence()));
                
                // 获取按钮中心点
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    logger.info("  中心点: ({}, {})", centerPoint.x, centerPoint.y);
                    
                    // 平滑移动鼠标到按钮中心并点击
                    logger.info("正在移动鼠标到按钮位置...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    // 短暂延迟确保鼠标到位
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // 执行点击
                    logger.info("正在点击入会按钮...");
                    MouseUtil.leftClick();
                    
                    logger.info("✓ 已成功点击入会按钮");
                    return true;
                } else {
                    logger.error("✗ 无法获取按钮中心点");
                }
            } else {
                logger.warn("✗ 未找到入会按钮 (置信度: {})", 
                           String.format("%.4f", result.getConfidence()));
                
                if (attempt < maxRetryTimes) {
                    try {
                        logger.info("等待2秒后重试...");
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
    private ImageMatchConfig createMatchConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        config.setThreshold(matchThreshold);
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