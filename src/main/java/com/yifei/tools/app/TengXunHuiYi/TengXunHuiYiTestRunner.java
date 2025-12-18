package com.yifei.tools.app.TengXunHuiYi;

import com.yifei.tools.image.matcher.ImageMatchConfig;
import com.yifei.tools.image.matcher.ImageMatcher;
import com.yifei.tools.image.matcher.MatchResult;
import com.yifei.tools.utils.Logger;
import com.yifei.tools.windows.MouseUtil;

import java.awt.Point;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * 腾讯会议自动入会测试工具
 * 用于测试自动入会功能，无需等待定时任务
 * 
 * @author yifei
 * @version 1.0
 */
public class TengXunHuiYiTestRunner {
    
    // 模板图片路径
    private static final String TENGXUN_HUIYI_TEMPLATE = "src/main/resources/TengXunHuiYi/TengXunHuiYi.png";
    private static final String RUHUI_BUTTON_TEMPLATE = "src/main/resources/TengXunHuiYi/TengXunHuiYi-RuHui.png";
    
    // 配置参数
    private static final int WAIT_SECONDS_BETWEEN_MATCH = 3;
    private static final int MAX_RETRY_TIMES = 10;
    private static final double MATCH_THRESHOLD = 0.8;
    
    public static void main(String[] args) {
        Logger.separator("腾讯会议自动入会测试工具");
        
        // 检查必要的组件是否可用
        if (!checkPrerequisites()) {
            Logger.error("系统检查失败，程序退出");
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            showMenu();
            System.out.print("请选择操作: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符
                
                switch (choice) {
                    case 1:
                        testTengXunHuiYiMatch();
                        break;
                    case 2:
                        testRuHuiButtonMatch();
                        break;
                    case 3:
                        testOpenTengXunHuiYi();
                        break;
                    case 4:
                        executeFullAutoJoinProcess();
                        break;
                    case 5:
                        testMouseMovement();
                        break;
                    case 6:
                        showSystemInfo();
                        break;
                    case 0:
                        System.out.println("退出测试程序");
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
                
                System.out.println("\n按回车键继续...");
                scanner.nextLine();
                
            } catch (Exception e) {
                System.err.println("输入错误: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }
    
    /**
     * 显示菜单
     */
    private static void showMenu() {
        System.out.println("\n=== 测试菜单 ===");
        System.out.println("1. 测试腾讯会议图标匹配");
        System.out.println("2. 测试入会按钮匹配");
        System.out.println("3. 测试打开腾讯会议软件");
        System.out.println("4. 执行完整自动入会流程");
        System.out.println("5. 测试鼠标移动");
        System.out.println("6. 显示系统信息");
        System.out.println("0. 退出");
        System.out.println("================");
    }
    
    /**
     * 检查系统前置条件
     */
    private static boolean checkPrerequisites() {
        Logger.info("正在检查系统前置条件...");
        
        if (!ImageMatcher.isOpenCVLoaded()) {
            Logger.error("OpenCV库未加载");
            return false;
        }
        Logger.success("OpenCV库已加载");
        
        if (!MouseUtil.isRobotInitialized()) {
            Logger.error("Robot未初始化");
            return false;
        }
        Logger.success("鼠标操作组件已初始化");
        
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
     * 测试腾讯会议图标匹配
     */
    private static void testTengXunHuiYiMatch() {
        System.out.println("\n=== 测试腾讯会议图标匹配 ===");
        
        ImageMatchConfig config = createMatchConfig();
        config.setSaveProcessImages(true); // 保存处理过程图片
        
        System.out.println("正在匹配腾讯会议图标...");
        MatchResult result = ImageMatcher.matchImage(TENGXUN_HUIYI_TEMPLATE, config);
        
        printMatchResult("腾讯会议图标", result);
        
        if (result.isMatched()) {
            Point centerPoint = result.getCenterPoint();
            if (centerPoint != null) {
                System.out.println("图标中心点: (" + centerPoint.x + ", " + centerPoint.y + ")");
                
                System.out.print("是否移动鼠标到图标位置? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine().trim();
                
                if ("y".equalsIgnoreCase(input)) {
                    System.out.println("移动鼠标到图标位置...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                }
            }
        }
    }
    
    /**
     * 测试入会按钮匹配
     */
    private static void testRuHuiButtonMatch() {
        System.out.println("\n=== 测试入会按钮匹配 ===");
        
        ImageMatchConfig config = createMatchConfig();
        config.setSaveProcessImages(true);
        
        System.out.println("正在匹配入会按钮...");
        MatchResult result = ImageMatcher.matchImage(RUHUI_BUTTON_TEMPLATE, config);
        
        printMatchResult("入会按钮", result);
        
        if (result.isMatched()) {
            Point centerPoint = result.getCenterPoint();
            if (centerPoint != null) {
                System.out.println("按钮中心点: (" + centerPoint.x + ", " + centerPoint.y + ")");
                
                System.out.print("是否移动鼠标到按钮位置? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine().trim();
                
                if ("y".equalsIgnoreCase(input)) {
                    System.out.println("移动鼠标到按钮位置...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    System.out.print("是否点击按钮? (y/n): ");
                    input = scanner.nextLine().trim();
                    
                    if ("y".equalsIgnoreCase(input)) {
                        System.out.println("点击按钮...");
                        MouseUtil.leftClick();
                        System.out.println("已点击按钮");
                    }
                }
            }
        }
    }
    
    /**
     * 测试打开腾讯会议软件
     */
    private static void testOpenTengXunHuiYi() {
        System.out.println("\n=== 测试打开腾讯会议软件 ===");
        System.out.println("警告：此操作将双击腾讯会议图标打开软件");
        System.out.print("确认执行? (y/n): ");
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        
        if (!"y".equalsIgnoreCase(input)) {
            System.out.println("已取消执行");
            return;
        }
        
        boolean success = findAndOpenTengXunHuiYi();
        if (success) {
            System.out.println("✓ 腾讯会议软件打开成功");
        } else {
            System.out.println("✗ 腾讯会议软件打开失败");
        }
    }
    
    /**
     * 执行完整自动入会流程
     */
    private static void executeFullAutoJoinProcess() {
        System.out.println("\n=== 执行完整自动入会流程 ===");
        System.out.println("警告：此操作将自动执行鼠标点击，请确保准备就绪");
        System.out.print("确认执行? (y/n): ");
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        
        if (!"y".equalsIgnoreCase(input)) {
            System.out.println("已取消执行");
            return;
        }
        
        executeAutoJoinProcess();
    }
    
    /**
     * 测试鼠标移动
     */
    private static void testMouseMovement() {
        System.out.println("\n=== 测试鼠标移动 ===");
        
        Point currentPos = MouseUtil.getCurrentMousePosition();
        System.out.println("当前鼠标位置: (" + currentPos.x + ", " + currentPos.y + ")");
        
        System.out.println("测试平滑移动到屏幕中央...");
        MouseUtil.smoothMoveTo(800, 400);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("测试贝塞尔曲线移动...");
        MouseUtil.bezierMoveTo(currentPos.x, currentPos.y);
        
        System.out.println("鼠标移动测试完成");
    }
    
    /**
     * 显示系统信息
     */
    private static void showSystemInfo() {
        System.out.println("\n=== 系统信息 ===");
        System.out.println("当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("OpenCV状态: " + (ImageMatcher.isOpenCVLoaded() ? "已加载" : "未加载"));
        System.out.println("Robot状态: " + (MouseUtil.isRobotInitialized() ? "已初始化" : "未初始化"));
        
        Point mousePos = MouseUtil.getCurrentMousePosition();
        System.out.println("当前鼠标位置: (" + mousePos.x + ", " + mousePos.y + ")");
        
        System.out.println("腾讯会议模板: " + (new File(TENGXUN_HUIYI_TEMPLATE).exists() ? "存在" : "不存在"));
        System.out.println("入会按钮模板: " + (new File(RUHUI_BUTTON_TEMPLATE).exists() ? "存在" : "不存在"));
    }
    
    /**
     * 执行自动入会流程（与主程序相同的逻辑）
     */
    private static void executeAutoJoinProcess() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("\n==================================================");
        System.out.println("开始执行腾讯会议自动入会测试");
        System.out.println("执行时间: " + timestamp);
        System.out.println("==================================================");
        
        try {
            // 第一步：查找腾讯会议图标并打开软件
            System.out.println("\n第一步：查找腾讯会议图标并打开软件...");
            boolean iconFound = findAndOpenTengXunHuiYi();
            
            if (!iconFound) {
                System.err.println("未找到腾讯会议图标，测试失败");
                return;
            }
            
            // 等待软件启动
            System.out.println("\n等待腾讯会议软件启动...");
            Thread.sleep(5000); // 等待5秒让软件完全启动
            
            // 第二步：等待软件界面稳定
            System.out.println("\n第二步：等待软件界面稳定...");
            Thread.sleep(WAIT_SECONDS_BETWEEN_MATCH * 1000);
            
            // 第三步：匹配并点击入会按钮
            System.out.println("\n第三步：查找并点击入会按钮...");
            boolean clickSuccess = findAndClickRuHuiButton();
            
            if (clickSuccess) {
                System.out.println("\n✓ 腾讯会议自动入会测试成功！");
            } else {
                System.err.println("\n✗ 腾讯会议自动入会测试失败");
            }
            
        } catch (Exception e) {
            System.err.println("测试执行异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("==================================================");
        System.out.println("腾讯会议自动入会测试结束");
        System.out.println("==================================================");
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
                printMatchResult("腾讯会议图标", result);
                
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    System.out.println("正在移动鼠标到腾讯会议图标...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("正在双击打开腾讯会议软件...");
                    MouseUtil.doubleClick();
                    
                    System.out.println("✓ 已成功双击腾讯会议图标");
                    return true;
                }
            } else {
                System.out.println("✗ 未找到腾讯会议图标 (置信度: " + 
                                 String.format("%.4f", result.getConfidence()) + ")");
                
                if (attempt < MAX_RETRY_TIMES) {
                    try {
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
     * 等待图像匹配成功
     */
    private static boolean waitForImageMatch(String templatePath, String description, int maxRetries) {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("尝试匹配 " + description + " (第" + attempt + "/" + maxRetries + "次)");
            
            MatchResult result = ImageMatcher.matchImage(templatePath, config);
            
            if (result.isMatched()) {
                System.out.println("✓ 成功找到 " + description);
                printMatchResult(description, result);
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
     * 查找并点击入会按钮
     */
    private static boolean findAndClickRuHuiButton() {
        ImageMatchConfig config = createMatchConfig();
        
        for (int attempt = 1; attempt <= MAX_RETRY_TIMES; attempt++) {
            System.out.println("尝试查找入会按钮 (第" + attempt + "/" + MAX_RETRY_TIMES + "次)");
            
            MatchResult result = ImageMatcher.matchImage(RUHUI_BUTTON_TEMPLATE, config);
            
            if (result.isMatched()) {
                System.out.println("✓ 成功找到入会按钮");
                printMatchResult("入会按钮", result);
                
                Point centerPoint = result.getCenterPoint();
                if (centerPoint != null) {
                    System.out.println("正在移动鼠标到按钮位置...");
                    MouseUtil.smoothMoveTo(centerPoint.x, centerPoint.y);
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("正在点击入会按钮...");
                    MouseUtil.leftClick();
                    
                    System.out.println("✓ 已成功点击入会按钮");
                    return true;
                }
            } else {
                System.out.println("✗ 未找到入会按钮 (置信度: " + 
                                 String.format("%.4f", result.getConfidence()) + ")");
                
                if (attempt < MAX_RETRY_TIMES) {
                    try {
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
     */
    private static ImageMatchConfig createMatchConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        config.setThreshold(MATCH_THRESHOLD);
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(true);
        config.setGaussianKernelSize(3);
        config.setGaussianSigmaX(0.8);
        config.setGaussianSigmaY(0.8);
        config.setOutputDir("tengxun_test_results");
        return config;
    }
    
    /**
     * 打印匹配结果
     */
    private static void printMatchResult(String name, MatchResult result) {
        System.out.println("--- " + name + " 匹配结果 ---");
        System.out.println("匹配成功: " + (result.isMatched() ? "是" : "否"));
        System.out.println("置信度: " + String.format("%.4f", result.getConfidence()));
        
        if (result.isMatched()) {
            if (result.getLocation() != null) {
                System.out.println("位置: (" + result.getLocation().x + ", " + result.getLocation().y + ")");
            }
            if (result.getCenterPoint() != null) {
                System.out.println("中心点: (" + result.getCenterPoint().x + ", " + result.getCenterPoint().y + ")");
            }
        }
        System.out.println();
    }
}