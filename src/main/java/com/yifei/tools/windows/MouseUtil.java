package com.yifei.tools.windows;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * 鼠标操作工具类
 * 提供鼠标移动、点击等操作功能
 * 
 * @author yifei
 * @version 1.0
 */
public class MouseUtil {
    
    private static Robot robot;
    
    static {
        try {
            robot = new Robot();
            robot.setAutoDelay(10); // 设置操作间隔
        } catch (AWTException e) {
            System.err.println("初始化Robot失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取当前鼠标位置
     * 
     * @return 鼠标位置
     */
    public static Point getCurrentMousePosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
    
    /**
     * 直接移动鼠标到指定位置（无轨迹）
     * 
     * @param x 目标x坐标
     * @param y 目标y坐标
     */
    public static void moveTo(int x, int y) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法移动鼠标");
            return;
        }
        
        try {
            robot.mouseMove(x, y);
            System.out.println("鼠标移动到: (" + x + ", " + y + ")");
        } catch (Exception e) {
            System.err.println("鼠标移动失败: " + e.getMessage());
        }
    }
    
    /**
     * 平滑移动鼠标到指定位置（带轨迹）
     * 
     * @param targetX 目标x坐标
     * @param targetY 目标y坐标
     */
    public static void smoothMoveTo(int targetX, int targetY) {
        smoothMoveTo(targetX, targetY, 20, 50);
    }
    
    /**
     * 平滑移动鼠标到指定位置（带轨迹，可自定义参数）
     * 
     * @param targetX 目标x坐标
     * @param targetY 目标y坐标
     * @param steps 移动步数（步数越多越平滑）
     * @param delayMs 每步间隔毫秒数
     */
    public static void smoothMoveTo(int targetX, int targetY, int steps, int delayMs) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法移动鼠标");
            return;
        }
        
        try {
            Point currentPos = getCurrentMousePosition();
            int startX = currentPos.x;
            int startY = currentPos.y;
            
            System.out.println("平滑移动鼠标: (" + startX + ", " + startY + ") -> (" + targetX + ", " + targetY + ")");
            
            // 计算每步的移动距离
            double deltaX = (double)(targetX - startX) / steps;
            double deltaY = (double)(targetY - startY) / steps;
            
            for (int i = 1; i <= steps; i++) {
                int newX = (int)(startX + deltaX * i);
                int newY = (int)(startY + deltaY * i);
                
                robot.mouseMove(newX, newY);
                
                // 添加延迟以产生平滑效果
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            // 确保最终到达目标位置
            robot.mouseMove(targetX, targetY);
            System.out.println("鼠标平滑移动完成");
            
        } catch (Exception e) {
            System.err.println("鼠标平滑移动失败: " + e.getMessage());
        }
    }
    
    /**
     * 贝塞尔曲线移动鼠标（更自然的轨迹）
     * 
     * @param targetX 目标x坐标
     * @param targetY 目标y坐标
     */
    public static void bezierMoveTo(int targetX, int targetY) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法移动鼠标");
            return;
        }
        
        try {
            Point currentPos = getCurrentMousePosition();
            int startX = currentPos.x;
            int startY = currentPos.y;
            
            // 计算控制点（在起点和终点之间偏移）
            int controlX1 = startX + (targetX - startX) / 3 + (int)(Math.random() * 100 - 50);
            int controlY1 = startY + (targetY - startY) / 3 + (int)(Math.random() * 100 - 50);
            int controlX2 = startX + 2 * (targetX - startX) / 3 + (int)(Math.random() * 100 - 50);
            int controlY2 = startY + 2 * (targetY - startY) / 3 + (int)(Math.random() * 100 - 50);
            
            System.out.println("贝塞尔曲线移动鼠标: (" + startX + ", " + startY + ") -> (" + targetX + ", " + targetY + ")");
            
            int steps = 30;
            for (int i = 0; i <= steps; i++) {
                double t = (double)i / steps;
                
                // 三次贝塞尔曲线公式
                double x = Math.pow(1-t, 3) * startX + 
                          3 * Math.pow(1-t, 2) * t * controlX1 + 
                          3 * (1-t) * Math.pow(t, 2) * controlX2 + 
                          Math.pow(t, 3) * targetX;
                          
                double y = Math.pow(1-t, 3) * startY + 
                          3 * Math.pow(1-t, 2) * t * controlY1 + 
                          3 * (1-t) * Math.pow(t, 2) * controlY2 + 
                          Math.pow(t, 3) * targetY;
                
                robot.mouseMove((int)x, (int)y);
                
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            System.out.println("贝塞尔曲线移动完成");
            
        } catch (Exception e) {
            System.err.println("贝塞尔曲线移动失败: " + e.getMessage());
        }
    }
    
    /**
     * 鼠标左键单击
     */
    public static void leftClick() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行点击");
            return;
        }
        
        try {
            Point pos = getCurrentMousePosition();
            System.out.println("在位置 (" + pos.x + ", " + pos.y + ") 执行左键单击");
            
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
        } catch (Exception e) {
            System.err.println("左键单击失败: " + e.getMessage());
        }
    }
    
    /**
     * 鼠标右键单击
     */
    public static void rightClick() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行点击");
            return;
        }
        
        try {
            Point pos = getCurrentMousePosition();
            System.out.println("在位置 (" + pos.x + ", " + pos.y + ") 执行右键单击");
            
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            
        } catch (Exception e) {
            System.err.println("右键单击失败: " + e.getMessage());
        }
    }
    
    /**
     * 鼠标双击
     */
    public static void doubleClick() {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行双击");
            return;
        }
        
        try {
            Point pos = getCurrentMousePosition();
            System.out.println("在位置 (" + pos.x + ", " + pos.y + ") 执行双击");
            
            // 第一次点击
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            // 短暂延迟
            robot.delay(100);
            
            // 第二次点击
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
        } catch (Exception e) {
            System.err.println("双击失败: " + e.getMessage());
        }
    }
    
    /**
     * 在指定位置执行左键单击
     * 
     * @param x x坐标
     * @param y y坐标
     */
    public static void clickAt(int x, int y) {
        moveTo(x, y);
        try {
            Thread.sleep(100); // 短暂延迟确保鼠标到位
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        leftClick();
    }
    
    /**
     * 在指定位置执行平滑移动后点击
     * 
     * @param x x坐标
     * @param y y坐标
     */
    public static void smoothClickAt(int x, int y) {
        smoothMoveTo(x, y);
        try {
            Thread.sleep(200); // 延迟确保移动完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        leftClick();
    }
    
    /**
     * 鼠标拖拽操作
     * 
     * @param startX 起始x坐标
     * @param startY 起始y坐标
     * @param endX 结束x坐标
     * @param endY 结束y坐标
     */
    public static void drag(int startX, int startY, int endX, int endY) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行拖拽");
            return;
        }
        
        try {
            System.out.println("执行拖拽操作: (" + startX + ", " + startY + ") -> (" + endX + ", " + endY + ")");
            
            // 移动到起始位置
            moveTo(startX, startY);
            Thread.sleep(100);
            
            // 按下鼠标左键
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            
            // 平滑移动到结束位置
            smoothMoveTo(endX, endY, 15, 30);
            
            // 释放鼠标左键
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            System.out.println("拖拽操作完成");
            
        } catch (Exception e) {
            System.err.println("拖拽操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 鼠标滚轮滚动
     * 
     * @param wheelRotation 滚动量，正数向下滚动，负数向上滚动
     */
    public static void scroll(int wheelRotation) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法执行滚动");
            return;
        }
        
        try {
            Point pos = getCurrentMousePosition();
            System.out.println("在位置 (" + pos.x + ", " + pos.y + ") 执行滚轮滚动: " + wheelRotation);
            
            robot.mouseWheel(wheelRotation);
            
        } catch (Exception e) {
            System.err.println("滚轮滚动失败: " + e.getMessage());
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
}