package yifei.game;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Test {

    public static void main(String[] args) {
        try {
            // 创建Robot实例
            Robot robot = new Robot();

            // 设置延迟，以便你能看到鼠标移动
            robot.setAutoDelay(500);

            // 获取屏幕尺寸
            int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

            System.out.println("屏幕尺寸: " + screenWidth + "x" + screenHeight);

            // 示例1: 移动鼠标到屏幕中央并点击
            moveAndClick(robot, screenWidth/2, screenHeight/2);

            // 示例2: 画一个正方形
//            drawSquare(robot, 200, 200, 100);

            // 示例3: 双击操作
            doubleClick(robot, 300, 300);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动鼠标到指定位置并点击
     */
    public static void moveAndClick(Robot robot, int x, int y) {
        System.out.println("移动并点击: (" + x + ", " + y + ")");

        // 移动鼠标
        robot.mouseMove(x, y);

        // 按下左键
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        // 释放左键
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * 在屏幕上画一个正方形
     */
    public static void drawSquare(Robot robot, int startX, int startY, int size) {
        System.out.println("开始画正方形...");

        // 移动鼠标到起点
        robot.mouseMove(startX, startY);

        // 按下左键开始拖动
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        // 向右移动
        robot.mouseMove(startX + size, startY);

        // 向下移动
        robot.mouseMove(startX + size, startY + size);

        // 向左移动
        robot.mouseMove(startX, startY + size);

        // 向上移动
        robot.mouseMove(startX, startY);

        // 释放左键
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        System.out.println("正方形完成");
    }

    /**
     * 在指定位置双击
     */
    public static void doubleClick(Robot robot, int x, int y) {
        System.out.println("在 (" + x + ", " + y + ") 双击");

        // 移动鼠标
        robot.mouseMove(x, y);

        // 第一次点击
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        // 快速第二次点击
//        robot.delay(50); // 短暂延迟
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}