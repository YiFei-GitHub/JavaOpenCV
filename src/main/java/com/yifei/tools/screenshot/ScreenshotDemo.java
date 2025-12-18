package com.yifei.tools.screenshot;

import java.awt.Dimension;

/**
 * 截图工具演示类
 * 展示如何使用ScreenshotUtil进行截图操作
 * 
 * @author yifei
 * @version 1.0
 */
public class ScreenshotDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 截图工具演示 ===");
        
        // 获取屏幕尺寸
        Dimension screenSize = ScreenshotUtil.getScreenSize();
        System.out.println("屏幕尺寸: " + screenSize.width + " x " + screenSize.height);
        
        // 创建输出目录
        String outputDir = "screenshots/";
        
        // 示例1: 截取屏幕左上角 400x300 区域
        System.out.println("\n1. 截取屏幕左上角区域...");
        boolean success1 = ScreenshotUtil.captureScreen(18, 835, 40, 40,
                                                       outputDir + "TengXunHuiYi.png");
        System.out.println("截图结果: " + (success1 ? "成功" : "失败"));
        
        // 示例2: 截取屏幕中央区域
//        System.out.println("\n2. 截取屏幕中央区域...");
//        int centerX = (screenSize.width - 600) / 2;
//        int centerY = (screenSize.height - 400) / 2;
//        boolean success2 = ScreenshotUtil.captureScreen(centerX, centerY, 600, 400,
//                                                       outputDir + "center_screenshot.jpg");
//        System.out.println("截图结果: " + (success2 ? "成功" : "失败"));
        
        // 示例3: 全屏截图
//        System.out.println("\n3. 全屏截图...");
//        boolean success3 = ScreenshotUtil.captureFullScreen(outputDir + "fullscreen_screenshot.png");
//        System.out.println("截图结果: " + (success3 ? "成功" : "失败"));
//
        // 示例4: 延迟截图（给用户5秒准备时间）
//        System.out.println("\n4. 延迟截图演示（5秒后截图）...");
//        boolean success4 = ScreenshotUtil.captureScreenWithDelay(100, 100, 500, 300,
//                                                               outputDir + "delayed_screenshot.png", 5);
//        System.out.println("延迟截图结果: " + (success4 ? "成功" : "失败"));
//
//        System.out.println("\n=== 演示完成 ===");
    }
}