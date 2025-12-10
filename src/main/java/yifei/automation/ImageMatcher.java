package yifei.automation;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

/**
 * 图像匹配工具类
 * 提供图像模板匹配功能
 */
public class ImageMatcher {
    
    static {
        nu.pattern.OpenCV.loadLocally();
    }
    
    /**
     * 在屏幕图像中查找模板图像
     * @param screenImage 屏幕截图
     * @param templatePath 模板图像路径
     * @param threshold 匹配阈值
     * @return 匹配结果，包含位置和置信度，如果未找到返回null
     */
    public static MatchResult findImage(BufferedImage screenImage, String templatePath, double threshold) {
        // 提取图片文件名
        String imageName = getImageName(templatePath);
        
        try {
            // 加载模板图像
            Mat template = Imgcodecs.imread(templatePath);
            if (template.empty()) {
                System.err.println("✗ 无法加载模板图像 [" + imageName + "]: " + templatePath);
                return null;
            }
            
            // 将屏幕图像转换为Mat格式
            Mat screenMat = bufferedImageToMat(screenImage);
            
            // 执行模板匹配
            Mat result = new Mat();
            Imgproc.matchTemplate(screenMat, template, result, Imgproc.TM_CCOEFF_NORMED);
            
            // 查找最佳匹配位置
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            
            // 输出匹配信息用于调试
            System.out.println("  匹配信息 [" + imageName + "]: 最大置信度=" + String.format("%.4f", mmr.maxVal) + 
                             ", 阈值=" + threshold + 
                             ", 模板大小=" + template.cols() + "x" + template.rows() +
                             ", 屏幕大小=" + screenMat.cols() + "x" + screenMat.rows());
            
            if (mmr.maxVal >= threshold) {
                Point matchLoc = mmr.maxLoc;
                int centerX = (int)(matchLoc.x + template.cols() / 2);
                int centerY = (int)(matchLoc.y + template.rows() / 2);
                
                System.out.println("  ✓ 找到图片 [" + imageName + "]: 位置=(" + centerX + ", " + centerY + "), 置信度=" + String.format("%.4f", mmr.maxVal));
                return new MatchResult(centerX, centerY, mmr.maxVal);
            } else {
                System.out.println("  ✗ 未找到图片 [" + imageName + "]: 置信度 " + String.format("%.4f", mmr.maxVal) + 
                                 " 低于阈值 " + threshold);
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("✗ 图像匹配失败 [" + imageName + "]: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 从文件路径中提取文件名
     */
    private static String getImageName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "未知图片";
        }
        int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSeparator >= 0 && lastSeparator < filePath.length() - 1) {
            return filePath.substring(lastSeparator + 1);
        }
        return filePath;
    }
    
    /**
     * 将BufferedImage转换为OpenCV Mat格式
     */
    private static Mat bufferedImageToMat(BufferedImage bi) {
        if (bi.getType() == BufferedImage.TYPE_INT_RGB ||
                bi.getType() == BufferedImage.TYPE_INT_ARGB) {
            return convertIntBufferedImageToMat(bi);
        } else {
            return convertByteBufferedImageToMat(bi);
        }
    }
    
    /**
     * 处理INT_RGB/INT_ARGB格式的BufferedImage
     */
    private static Mat convertIntBufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = new byte[bi.getWidth() * bi.getHeight() * 3];
        
        int[] pixelData = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                int pixel = pixelData[y * bi.getWidth() + x];
                data[(y * bi.getWidth() + x) * 3] = (byte)((pixel >> 16) & 0xFF);     // B
                data[(y * bi.getWidth() + x) * 3 + 1] = (byte)((pixel >> 8) & 0xFF);  // G
                data[(y * bi.getWidth() + x) * 3 + 2] = (byte)(pixel & 0xFF);          // R
            }
        }
        mat.put(0, 0, data);
        return mat;
    }
    
    /**
     * 处理TYPE_3BYTE_BGR格式的BufferedImage
     */
    private static Mat convertByteBufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
    
    /**
     * 匹配结果类
     */
    public static class MatchResult {
        private final int x;
        private final int y;
        private final double confidence;
        
        public MatchResult(int x, int y, double confidence) {
            this.x = x;
            this.y = y;
            this.confidence = confidence;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public double getConfidence() {
            return confidence;
        }
        
        @Override
        public String toString() {
            return String.format("位置: (%d, %d), 置信度: %.2f", x, y, confidence);
        }
    }
}

