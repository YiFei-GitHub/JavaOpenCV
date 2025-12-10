package yifei.automation;

import java.awt.image.BufferedImage;

/**
 * 自动化任务执行类
 * 负责执行完整的自动化流程：匹配桌面图标 -> 打开软件 -> 匹配图片 -> 点击
 */
public class AutomationTask {
    
    private TaskConfig config;
    private WindowManager windowManager;
    
    public AutomationTask(TaskConfig config) {
        this.config = config;
        this.windowManager = new WindowManager();
    }
    
    /**
     * 执行自动化任务
     * @return 是否执行成功
     */
    public boolean execute() {
        System.out.println("========== 开始执行自动化任务 ==========");
        
        try {
            // 步骤1: 匹配桌面图标并打开软件
            if (!openApplication()) {
                System.err.println("打开软件失败");
                return false;
            }
            
            // 等待软件启动
            System.out.println("等待软件启动...");
            windowManager.delay(3000);
            
            // 步骤2: 在软件窗口中匹配目标图片
            ImageMatcher.MatchResult result = findTargetInWindow();
            if (result == null) {
                String targetImageName = getImageName(config.getTargetImagePath());
                System.err.println("未找到目标图片 [" + targetImageName + "]");
                return false;
            }
            
            // 步骤3: 点击匹配到的位置
            String targetImageName = getImageName(config.getTargetImagePath());
            System.out.println("找到目标图片 [" + targetImageName + "]: " + result);
            windowManager.click(result.getX(), result.getY(), config.isDoubleClick());
            windowManager.delay(config.getClickDelay());
            
            System.out.println("========== 任务执行完成 ==========");
            return true;
            
        } catch (Exception e) {
            System.err.println("执行任务时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 步骤1: 在桌面匹配图标并打开软件
     */
    private boolean openApplication() {
        if (config.getDesktopIconPath() == null || config.getDesktopIconPath().isEmpty()) {
            System.out.println("未配置桌面图标路径，跳过图标匹配步骤");
            return true; // 如果没有配置图标，假设软件已经打开
        }
        
        // 检查图标文件是否存在
        java.io.File iconFile = new java.io.File(config.getDesktopIconPath());
        String iconName = getImageName(config.getDesktopIconPath());
        
        if (!iconFile.exists()) {
            System.err.println("错误: 图标文件不存在 [" + iconName + "]: " + config.getDesktopIconPath());
            return false;
        }
        System.out.println("图标文件存在 [" + iconName + "]: " + config.getDesktopIconPath());
        
        // 先回到桌面首页
        windowManager.showDesktop();
        
        // 等待桌面完全显示
        windowManager.delay(1000);
        
        System.out.println("正在匹配桌面图标 [" + iconName + "]");
        System.out.println("当前匹配阈值: " + config.getIconMatchThreshold());
        
        // 尝试多次匹配，每次降低阈值
        int maxRetries = 3;
        double[] thresholds = {
            config.getIconMatchThreshold(),
            config.getIconMatchThreshold() - 0.1,
            config.getIconMatchThreshold() - 0.2
        };
        
        for (int i = 0; i < maxRetries; i++) {
            // 截取桌面
            BufferedImage desktop = windowManager.captureScreen();
            
            // 使用当前阈值匹配图标
            double currentThreshold = thresholds[i];
            System.out.println("尝试匹配 (第" + (i + 1) + "次, 阈值=" + currentThreshold + ")...");
            
            ImageMatcher.MatchResult iconResult = ImageMatcher.findImage(
                desktop, 
                config.getDesktopIconPath(), 
                currentThreshold
            );
            
            if (iconResult != null) {
                System.out.println("✓ 找到桌面图标 [" + iconName + "]: " + iconResult);
                
                // 双击图标打开软件
                windowManager.click(iconResult.getX(), iconResult.getY(), true);
                System.out.println("已点击桌面图标 [" + iconName + "]，正在打开软件...");
                
                return true;
            }
            
            // 如果没找到，等待一下再重试
            if (i < maxRetries - 1) {
                System.out.println("未找到图标 [" + iconName + "]，等待1秒后重试...");
                windowManager.delay(1000);
            }
        }
        
        System.err.println("✗ 未找到桌面图标 [" + iconName + "]，已尝试" + maxRetries + "次");
        System.err.println("提示: 请检查:");
        System.err.println("  1. 图标图片是否与桌面上的图标一致");
        System.err.println("  2. 图标是否在桌面上可见（未被遮挡）");
        System.err.println("  3. 尝试降低匹配阈值（当前: " + config.getIconMatchThreshold() + "）");
        
        return false;
    }
    
    /**
     * 步骤2: 在软件窗口中查找目标图片
     */
    private ImageMatcher.MatchResult findTargetInWindow() {
        if (config.getTargetImagePath() == null || config.getTargetImagePath().isEmpty()) {
            System.err.println("未配置目标图片路径");
            return null;
        }
        
        String targetImageName = getImageName(config.getTargetImagePath());
        System.out.println("正在在软件窗口中查找目标图片 [" + targetImageName + "]: " + config.getTargetImagePath());
        
        // 等待窗口出现
        int timeout = config.getWaitWindowTimeout() * 1000; // 转换为毫秒
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < timeout) {
            // 截取整个屏幕（假设软件窗口已经打开）
            BufferedImage screen = windowManager.captureScreen();
            
            // 匹配目标图片
            ImageMatcher.MatchResult result = ImageMatcher.findImage(
                screen, 
                config.getTargetImagePath(), 
                config.getTargetMatchThreshold()
            );
            
            if (result != null) {
                System.out.println("✓ 找到目标图片 [" + targetImageName + "]: " + result);
                return result;
            }
            
            // 等待一段时间后重试
            windowManager.delay(1000);
            System.out.println("未找到目标图片 [" + targetImageName + "]，继续搜索...");
        }
        
        System.err.println("✗ 未找到目标图片 [" + targetImageName + "]，超时");
        return null;
    }
    
    /**
     * 从文件路径中提取文件名
     */
    private String getImageName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "未知图片";
        }
        int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSeparator >= 0 && lastSeparator < filePath.length() - 1) {
            return filePath.substring(lastSeparator + 1);
        }
        return filePath;
    }
}

