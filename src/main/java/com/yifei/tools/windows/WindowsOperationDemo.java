package com.yifei.tools.windows;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Scanner;

/**
 * Windows操作工具演示类
 * 展示鼠标操作和Windows系统操作功能
 * 
 * @author yifei
 * @version 1.0
 */
public class WindowsOperationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Windows操作工具演示 ===");
        
        // 检查Robot是否初始化成功
        if (!MouseUtil.isRobotInitialized() || !WindowsUtil.isRobotInitialized()) {
            System.err.println("Robot初始化失败，无法运行演示");
            return;
        }
        
        // 获取屏幕信息
        Dimension screenSize = WindowsUtil.getScreenSize();
        System.out.println("屏幕尺寸: " + screenSize.width + " x " + screenSize.height);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            showMenu();
            System.out.print("请选择操作 (输入数字): ");
            
            try {
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        demonstrateMouseMovement();
                        break;
                    case 2:
                        demonstrateMouseClick();
                        break;
                    case 3:
                        demonstrateMouseDrag();
                        break;
                    case 4:
                        WindowsUtil.showDesktop();
                        break;
                    case 5:
                        WindowsUtil.minimizeAllWindows();
                        break;
                    case 6:
                        WindowsUtil.openFileExplorer();
                        break;
                    case 7:
                        WindowsUtil.openTaskManager();
                        break;
                    case 8:
                        WindowsUtil.switchApplication();
                        break;
                    case 9:
                        demonstrateInteractiveMouseControl(scanner);
                        break;
                    case 10:
                        demonstrateAdvancedOperations();
                        break;
                    case 0:
                        System.out.println("退出演示程序");
                        return;
                    default:
                        System.out.println("无效选择，请重新输入");
                }
                
                System.out.println("\n按回车键继续...");
                scanner.nextLine(); // 消费换行符
                scanner.nextLine(); // 等待用户按回车
                
            } catch (Exception e) {
                System.err.println("输入错误: " + e.getMessage());
                scanner.nextLine(); // 清除错误输入
            }
        }
    }
    
    /**
     * 显示菜单
     */
    private static void showMenu() {
        System.out.println("\n=== 功能菜单 ===");
        System.out.println("1. 鼠标移动演示");
        System.out.println("2. 鼠标点击演示");
        System.out.println("3. 鼠标拖拽演示");
        System.out.println("4. 返回桌面");
        System.out.println("5. 最小化所有窗口");
        System.out.println("6. 打开文件资源管理器");
        System.out.println("7. 打开任务管理器");
        System.out.println("8. 切换应用程序");
        System.out.println("9. 交互式鼠标控制");
        System.out.println("10. 高级操作演示");
        System.out.println("0. 退出");
        System.out.println("================");
    }
    
    /**
     * 演示鼠标移动功能
     */
    private static void demonstrateMouseMovement() {
        System.out.println("\n=== 鼠标移动演示 ===");
        
        Point currentPos = MouseUtil.getCurrentMousePosition();
        System.out.println("当前鼠标位置: " + currentPos);
        
        Dimension screenSize = WindowsUtil.getScreenSize();
        
        // 演示1: 直接移动
        System.out.println("\n1. 直接移动到屏幕中央");
        MouseUtil.moveTo(screenSize.width / 2, screenSize.height / 2);
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 演示2: 平滑移动
        System.out.println("\n2. 平滑移动到屏幕左上角");
        MouseUtil.smoothMoveTo(100, 100);
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 演示3: 贝塞尔曲线移动
        System.out.println("\n3. 贝塞尔曲线移动到屏幕右下角");
        MouseUtil.bezierMoveTo(screenSize.width - 100, screenSize.height - 100);
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 演示4: 画一个正方形轨迹
        System.out.println("\n4. 画正方形轨迹");
        drawSquare();
        
        System.out.println("鼠标移动演示完成");
    }
    
    /**
     * 演示鼠标点击功能
     */
    private static void demonstrateMouseClick() {
        System.out.println("\n=== 鼠标点击演示 ===");
        
        Dimension screenSize = WindowsUtil.getScreenSize();
        
        // 移动到屏幕中央
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        
        System.out.println("移动到屏幕中央并执行各种点击操作");
        MouseUtil.smoothMoveTo(centerX, centerY);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        System.out.println("执行左键单击");
        MouseUtil.leftClick();
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        System.out.println("执行右键单击");
        MouseUtil.rightClick();
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        System.out.println("执行双击");
        MouseUtil.doubleClick();
        
        System.out.println("鼠标点击演示完成");
    }
    
    /**
     * 演示鼠标拖拽功能
     */
    private static void demonstrateMouseDrag() {
        System.out.println("\n=== 鼠标拖拽演示 ===");
        
        Dimension screenSize = WindowsUtil.getScreenSize();
        
        int startX = screenSize.width / 4;
        int startY = screenSize.height / 4;
        int endX = screenSize.width * 3 / 4;
        int endY = screenSize.height * 3 / 4;
        
        System.out.println("执行拖拽操作: 从左上角拖拽到右下角");
        MouseUtil.drag(startX, startY, endX, endY);
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        System.out.println("执行反向拖拽");
        MouseUtil.drag(endX, endY, startX, startY);
        
        System.out.println("鼠标拖拽演示完成");
    }
    
    /**
     * 交互式鼠标控制
     */
    private static void demonstrateInteractiveMouseControl(Scanner scanner) {
        System.out.println("\n=== 交互式鼠标控制 ===");
        System.out.println("输入坐标来控制鼠标移动，输入 'quit' 退出");
        
        while (true) {
            Point currentPos = MouseUtil.getCurrentMousePosition();
            System.out.println("当前位置: (" + currentPos.x + ", " + currentPos.y + ")");
            System.out.print("输入目标坐标 (格式: x,y) 或 'quit': ");
            
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input)) {
                break;
            }
            
            try {
                String[] coords = input.split(",");
                if (coords.length == 2) {
                    int x = Integer.parseInt(coords[0].trim());
                    int y = Integer.parseInt(coords[1].trim());
                    
                    System.out.println("移动到: (" + x + ", " + y + ")");
                    MouseUtil.smoothMoveTo(x, y);
                    
                    System.out.print("是否点击? (y/n): ");
                    String clickChoice = scanner.nextLine().trim();
                    if ("y".equalsIgnoreCase(clickChoice)) {
                        MouseUtil.leftClick();
                        System.out.println("已点击");
                    }
                } else {
                    System.out.println("格式错误，请使用 x,y 格式");
                }
            } catch (NumberFormatException e) {
                System.out.println("坐标格式错误，请输入数字");
            }
        }
        
        System.out.println("退出交互式控制");
    }
    
    /**
     * 高级操作演示
     */
    private static void demonstrateAdvancedOperations() {
        System.out.println("\n=== 高级操作演示 ===");
        
        System.out.println("1. 打开运行对话框");
        WindowsUtil.openRunDialog();
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("2. 按ESC关闭对话框");
        // 这里可以添加按ESC键的代码
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        System.out.println("3. 执行滚轮滚动");
        MouseUtil.scroll(3); // 向下滚动
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        MouseUtil.scroll(-3); // 向上滚动
        
        System.out.println("高级操作演示完成");
    }
    
    /**
     * 画正方形轨迹
     */
    private static void drawSquare() {
        Dimension screenSize = WindowsUtil.getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        int size = 200;
        
        // 正方形的四个顶点
        int[] x = {centerX - size/2, centerX + size/2, centerX + size/2, centerX - size/2, centerX - size/2};
        int[] y = {centerY - size/2, centerY - size/2, centerY + size/2, centerY + size/2, centerY - size/2};
        
        // 移动到起始点
        MouseUtil.moveTo(x[0], y[0]);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // 画正方形
        for (int i = 1; i < x.length; i++) {
            MouseUtil.smoothMoveTo(x[i], y[i], 20, 30);
            try { Thread.sleep(200); } catch (InterruptedException e) {}
        }
    }
}