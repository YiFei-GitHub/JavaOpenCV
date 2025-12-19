package com.yifei.restful.opencv.tengxunhuiyi.service;

import com.yifei.restful.opencv.tengxunhuiyi.config.ResourceConfig;
import com.yifei.tools.image.matcher.ImageMatcher;
import com.yifei.tools.image.matcher.ImageMatchConfig;
import com.yifei.tools.image.matcher.MatchResult;
import com.yifei.tools.windows.MouseUtil;
import com.yifei.tools.windows.WindowsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private ResourceConfig resourceConfig;
    
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
        logger.info("定时任务配置: {}", resourceConfig.getSchedule().getCron());
        logger.info("定时任务启用状态: {}", resourceConfig.getSchedule().isEnabled());
        logger.info("匹配阈值: {}", resourceConfig.getMatch().getThreshold());
        logger.info("最大重试次数: {}", resourceConfig.getMatch().getMaxRetryTimes());
        
        // 显示定时任务信息
        logger.info("=== 定时任务调度信息 ===");
        logger.info("Cron表达式: {}", resourceConfig.getSchedule().getCron());
        logger.info("执行时间: 每天早上6:55");
        logger.info("注意: 定时任务将在指定时间自动执行");
        logger.info("可通过 /api/tengxun/manual-join 接口手动触发测试");
        
        logger.info("=== SpringBoot定时任务已配置 ===");
        logger.info("✓ 使用SpringBoot @Scheduled注解，每天6:55自动执行");
    }
    
    /**
     * 测试定时任务：每分钟执行一次，用于验证调度器是否正常工作
     * 可以通过日志查看是否按时执行
     */
    @Scheduled(cron = "0 * * * * ?")
    public void testScheduler() {
        logger.info("🔔 定时任务调度器正常工作 - 当前时间: {}", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * 定时任务：每天早上6:55执行腾讯会议自动入会
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "#{@resourceConfig.schedule.cron}")
    public void executeAutoJoin() {
        logger.info("SpringBoot @Scheduled 触发的腾讯会议自动入会任务");
        executeAutoJoinProcess();
    }

    /**
     * 手动执行自动入会任务（用于测试）
     */
    public void manualExecute() {
        logger.info("手动触发腾讯会议自动入会任务");
        executeAutoJoinProcess();
    }

    /**
     * 执行自动入会流程的核心方法
     * 可被SpringBoot定时任务和SimpleScheduler调用
     */
    public void executeAutoJoinProcess() {
        logger.info("==================== 腾讯会议自动入会任务开始 ====================");
        
        boolean taskSuccess = false;
        
        try {
            // 第零步：回到Windows桌面
            logger.info("第零步：回到Windows桌面...");
            WindowsUtil.showDesktop();
            Thread.sleep(2000); // 等待2秒确保桌面显示完成
            
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
            Thread.sleep(resourceConfig.getMatch().getWaitSeconds() * 1000);
            
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
     * 检查系统前置条件
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
        File tengxunTemplate = new File(resourceConfig.getTemplates().getTengxunHuiyi());
        if (!tengxunTemplate.exists()) {
            logger.error("腾讯会议模板图片不存在: {}", resourceConfig.getTemplates().getTengxunHuiyi());
            return false;
        }
        logger.info("✓ 腾讯会议模板图片存在: {}", resourceConfig.getTemplates().getTengxunHuiyi());
        
        File ruhuiTemplate = new File(resourceConfig.getTemplates().getRuhuiButton());
        if (!ruhuiTemplate.exists()) {
            logger.error("入会按钮模板图片不存在: {}", resourceConfig.getTemplates().getRuhuiButton());
            return false;
        }
        logger.info("✓ 入会按钮模板图片存在: {}", resourceConfig.getTemplates().getRuhuiButton());
        
        return true;
    }
    
    /**
     * 查找并打开腾讯会议软件
     * 
     * @return 是否成功打开
     */
    private boolean findAndOpenTengXunHuiYi() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= resourceConfig.getMatch().getMaxRetryTimes(); attempt++) {
            logger.info("尝试查找腾讯会议图标 (第{}/{}次)", attempt, resourceConfig.getMatch().getMaxRetryTimes());
            
            MatchResult result = ImageMatcher.matchImage(resourceConfig.getTemplates().getTengxunHuiyi(), config);
            
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
                
                if (attempt < resourceConfig.getMatch().getMaxRetryTimes()) {
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
        
        for (int attempt = 1; attempt <= resourceConfig.getMatch().getMaxRetryTimes(); attempt++) {
            logger.info("尝试查找入会按钮 (第{}/{}次)", attempt, resourceConfig.getMatch().getMaxRetryTimes());
            
            MatchResult result = ImageMatcher.matchImage(resourceConfig.getTemplates().getRuhuiButton(), config);
            
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
                
                if (attempt < resourceConfig.getMatch().getMaxRetryTimes()) {
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
        config.setThreshold(resourceConfig.getMatch().getThreshold());
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(true);
        config.setGaussianKernelSize(3);
        config.setGaussianSigmaX(0.8);
        config.setGaussianSigmaY(0.8);
        
        // 可选：保存匹配过程图片用于调试
        config.setSaveProcessImages(true);
        config.setOutputDir(resourceConfig.getOutput().getMatchResultDir());
        
        return config;
    }
}