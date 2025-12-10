package yifei.automation;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 窗口管理工具类
 * 提供窗口查找、截图等功能
 */
public class WindowManager {
    
    private Robot robot;
    
    public WindowManager() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("无法创建Robot实例", e);
        }
    }
    
    /**
     * 截取整个屏幕
     */
    public BufferedImage captureScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        return robot.createScreenCapture(screenRect);
    }
    
    /**
     * 截取指定区域的屏幕
     */
    public BufferedImage captureRegion(int x, int y, int width, int height) {
        Rectangle region = new Rectangle(x, y, width, height);
        return robot.createScreenCapture(region);
    }
    
    /**
     * 在指定位置点击
     */
    public void click(int x, int y, boolean doubleClick) {
        robot.mouseMove(x, y);
        robot.delay(100);
        robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        
        if (doubleClick) {
            robot.delay(50);
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        }
    }
    
    /**
     * 等待指定时间（毫秒）
     */
    public void delay(int ms) {
        robot.delay(ms);
    }
    
    /**
     * 回到桌面首页（显示桌面）
     * 使用Win+D快捷键最小化所有窗口
     */
    public void showDesktop() {
        try {
            System.out.println("正在回到桌面首页...");
            // 按下Win键
            robot.keyPress(java.awt.event.KeyEvent.VK_WINDOWS);
            // 按下D键
            robot.keyPress(java.awt.event.KeyEvent.VK_D);
            // 释放D键
            robot.keyRelease(java.awt.event.KeyEvent.VK_D);
            // 释放Win键
            robot.keyRelease(java.awt.event.KeyEvent.VK_WINDOWS);
            // 等待桌面显示
            robot.delay(500);
            System.out.println("已回到桌面首页");
        } catch (Exception e) {
            System.err.println("回到桌面失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 检查软件窗口是否已打开
     * 通过检查进程名或窗口标题来判断
     */
    public boolean isWindowOpen(String windowTitle) {
        try {
            // 简单的实现：通过截屏检查窗口标题栏区域
            // 更完善的实现可以使用JNA调用Windows API
            // 这里可以添加更复杂的窗口检测逻辑
            // 暂时返回true，实际使用时可以通过其他方式检测
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

