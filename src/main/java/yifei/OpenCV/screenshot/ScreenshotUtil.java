package yifei.OpenCV.screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 截图工具类
 * 功能：从屏幕指定位置截图并保存为文件
 * 
 * @author yifei
 * @version 1.0
 */
public class ScreenshotUtil {
    
    private static Robot robot;
    
    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("初始化Robot失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 截取屏幕指定区域并保存为图片文件
     * 
     * @param x 截图区域左上角x坐标
     * @param y 截图区域左上角y坐标
     * @param width 截图区域宽度
     * @param height 截图区域高度
     * @param outputPath 输出文件路径（包含文件名和扩展名）
     * @return 是否截图成功
     */
    public static boolean captureScreen(int x, int y, int width, int height, String outputPath) {
        if (robot == null) {
            System.err.println("Robot未初始化，无法进行截图");
            return false;
        }
        
        try {
            // 验证输入参数
            if (width <= 0 || height <= 0) {
                System.err.println("宽度和高度必须大于0");
                return false;
            }
            
            if (outputPath == null || outputPath.trim().isEmpty()) {
                System.err.println("输出路径不能为空");
                return false;
            }
            
            // 创建截图区域
            Rectangle screenRect = new Rectangle(x, y, width, height);
            
            // 截取屏幕
            BufferedImage screenImage = robot.createScreenCapture(screenRect);
            
            // 确保输出目录存在
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // 获取文件格式
            String format = getImageFormat(outputPath);
            
            // 保存图片
            boolean success = ImageIO.write(screenImage, format, outputFile);
            
            if (success) {
                System.out.println("截图成功保存到: " + outputPath);
            } else {
                System.err.println("保存截图失败");
            }
            
            return success;
            
        } catch (IOException e) {
            System.err.println("保存截图时发生IO异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("截图过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 截取全屏并保存为图片文件
     * 
     * @param outputPath 输出文件路径
     * @return 是否截图成功
     */
    public static boolean captureFullScreen(String outputPath) {
        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return captureScreen(0, 0, screenSize.width, screenSize.height, outputPath);
    }
    
    /**
     * 根据文件路径获取图片格式
     * 
     * @param filePath 文件路径
     * @return 图片格式（默认为png）
     */
    private static String getImageFormat(String filePath) {
        String format = "png"; // 默认格式
        
        if (filePath != null) {
            int lastDotIndex = filePath.lastIndexOf('.');
            if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
                String extension = filePath.substring(lastDotIndex + 1).toLowerCase();
                // 支持常见的图片格式
                if ("jpg".equals(extension) || "jpeg".equals(extension) || 
                    "png".equals(extension) || "bmp".equals(extension) || 
                    "gif".equals(extension)) {
                    format = extension;
                }
            }
        }
        
        return format;
    }
    
    /**
     * 获取屏幕尺寸信息
     * 
     * @return 屏幕尺寸
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    /**
     * 延迟截图（给用户准备时间）
     * 
     * @param x 截图区域左上角x坐标
     * @param y 截图区域左上角y坐标
     * @param width 截图区域宽度
     * @param height 截图区域高度
     * @param outputPath 输出文件路径
     * @param delaySeconds 延迟秒数
     * @return 是否截图成功
     */
    public static boolean captureScreenWithDelay(int x, int y, int width, int height, 
                                               String outputPath, int delaySeconds) {
        try {
            System.out.println("将在 " + delaySeconds + " 秒后开始截图...");
            Thread.sleep(delaySeconds * 1000);
            return captureScreen(x, y, width, height, outputPath);
        } catch (InterruptedException e) {
            System.err.println("延迟截图被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }
}