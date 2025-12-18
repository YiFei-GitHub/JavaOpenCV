package com.yifei.tools.windows;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Windows操作工具类
 * 提供Windows系统相关的操作功能
 * 
 * @author yifei
 * @version 1.0
 */
public class WindowsUtil {
    
    private static Robot robot;
    
    static {
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
        } catch (AWTException e) {
            System.err.println("初始化Robot失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 返回桌面（显示桌面）
     * 使用Windows + D快捷键
     */
    public static void showDesktop() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行返回桌面操作");
            return;
        }
        
        try {
            System.out.println("执行返回桌面操作...");
            
            // 按下 Windows + D
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            
            System.out.println("返回桌面操作完成");
            
        } catch (Exception e) {
            System.err.println("返回桌面操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 最小化所有窗口
     * 使用Windows + M快捷键
     */
    public static void minimizeAllWindows() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行最小化操作");
            return;
        }
        
        try {
            System.out.println("执行最小化所有窗口操作...");
            
            // 按下 Windows + M
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_M);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_M);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            
            System.out.println("最小化所有窗口操作完成");
            
        } catch (Exception e) {
            System.err.println("最小化所有窗口操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 打开任务管理器
     * 使用Ctrl + Shift + Esc快捷键
     */
    public static void openTaskManager() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法打开任务管理器");
            return;
        }
        
        try {
            System.out.println("打开任务管理器...");
            
            // 按下 Ctrl + Shift + Esc
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            
            System.out.println("任务管理器已打开");
            
        } catch (Exception e) {
            System.err.println("打开任务管理器失败: " + e.getMessage());
        }
    }
    
    /**
     * 打开运行对话框
     * 使用Windows + R快捷键
     */
    public static void openRunDialog() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法打开运行对话框");
            return;
        }
        
        try {
            System.out.println("打开运行对话框...");
            
            // 按下 Windows + R
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_R);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_R);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            
            System.out.println("运行对话框已打开");
            
        } catch (Exception e) {
            System.err.println("打开运行对话框失败: " + e.getMessage());
        }
    }
    
    /**
     * 锁定计算机
     * 使用Windows + L快捷键
     */
    public static void lockComputer() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法锁定计算机");
            return;
        }
        
        try {
            System.out.println("锁定计算机...");
            
            // 按下 Windows + L
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_L);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            
            System.out.println("计算机已锁定");
            
        } catch (Exception e) {
            System.err.println("锁定计算机失败: " + e.getMessage());
        }
    }
    
    /**
     * 打开文件资源管理器
     * 使用Windows + E快捷键
     */
    public static void openFileExplorer() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法打开文件资源管理器");
            return;
        }
        
        try {
            System.out.println("打开文件资源管理器...");
            
            // 按下 Windows + E
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_E);
            
            // 释放按键
            robot.keyRelease(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            
            System.out.println("文件资源管理器已打开");
            
        } catch (Exception e) {
            System.err.println("打开文件资源管理器失败: " + e.getMessage());
        }
    }
    
    /**
     * 切换应用程序
     * 使用Alt + Tab快捷键
     */
    public static void switchApplication() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法切换应用程序");
            return;
        }
        
        try {
            System.out.println("切换应用程序...");
            
            // 按下 Alt + Tab
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_TAB);
            
            // 释放Tab键，保持Alt键按下一段时间以显示切换界面
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(500);
            robot.keyRelease(KeyEvent.VK_ALT);
            
            System.out.println("应用程序切换完成");
            
        } catch (Exception e) {
            System.err.println("切换应用程序失败: " + e.getMessage());
        }
    }
    
    /**
     * 截图并保存
     * 使用PrintScreen键
     */
    public static void takeScreenshot() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法截图");
            return;
        }
        
        try {
            System.out.println("执行系统截图...");
            
            // 按下 PrintScreen
            robot.keyPress(KeyEvent.VK_PRINTSCREEN);
            robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
            
            System.out.println("截图已保存到剪贴板");
            
        } catch (Exception e) {
            System.err.println("截图失败: " + e.getMessage());
        }
    }
    
    /**
     * 使用CMD命令执行系统操作
     * 
     * @param command 要执行的命令
     * @return 是否执行成功
     */
    public static boolean executeCommand(String command) {
        try {
            System.out.println("执行系统命令: " + command);
            
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
            Process process = processBuilder.start();
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("命令执行成功");
                return true;
            } else {
                System.err.println("命令执行失败，退出代码: " + exitCode);
                return false;
            }
            
        } catch (IOException | InterruptedException e) {
            System.err.println("执行命令异常: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 通过CMD命令返回桌面（备用方法）
     */
    public static void showDesktopByCommand() {
        try {
            System.out.println("通过命令返回桌面...");
            
            // 使用PowerShell命令最小化所有窗口
            String command = "powershell -Command \"(New-Object -ComObject Shell.Application).MinimizeAll()\"";
            executeCommand(command);
            
        } catch (Exception e) {
            System.err.println("通过命令返回桌面失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查Robot是否已初始化
     * 
     * @return 是否已初始化
     */
    public static boolean isRobotInitialized() {
        return robot != null;
    }
    
    /**
     * 获取屏幕尺寸
     * 
     * @return 屏幕尺寸
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}