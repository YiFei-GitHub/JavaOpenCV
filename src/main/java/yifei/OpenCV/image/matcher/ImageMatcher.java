package yifei.OpenCV.image.matcher;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import nu.pattern.OpenCV;
import yifei.OpenCV.screenshot.ScreenshotUtil;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 图像识别匹配工具类
 * 提供高精度的图像匹配功能
 * 
 * @author yifei
 * @version 1.0
 */
public class ImageMatcher {
    
    private static boolean opencvLoaded = false;
    
    static {
        try {
            // 加载OpenCV库
            OpenCV.loadShared();
            opencvLoaded = true;
            System.out.println("OpenCV库加载成功");
        } catch (Exception e) {
            System.err.println("OpenCV库加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 使用默认配置匹配图像
     * 
     * @param templatePath 模板图片路径
     * @return 匹配结果
     */
    public static MatchResult matchImage(String templatePath) {
        return matchImage(templatePath, ImageMatchConfig.getDefaultConfig());
    }
    
    /**
     * 匹配屏幕中的图像
     * 
     * @param templatePath 模板图片路径
     * @param config 匹配配置
     * @return 匹配结果
     */
    public static MatchResult matchImage(String templatePath, ImageMatchConfig config) {
        if (!opencvLoaded) {
            MatchResult result = new MatchResult();
            result.setMatched(false);
            System.err.println("OpenCV库未加载，无法进行图像匹配");
            return result;
        }
        
        try {
            // 截取当前屏幕
            String screenshotPath = captureScreenForMatch(config);
            if (screenshotPath == null) {
                MatchResult result = new MatchResult();
                result.setMatched(false);
                return result;
            }
            
            return matchImages(templatePath, screenshotPath, config);
            
        } catch (Exception e) {
            System.err.println("图像匹配过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            MatchResult result = new MatchResult();
            result.setMatched(false);
            return result;
        }
    }
    
    /**
     * 在指定区域匹配图像
     * 
     * @param templatePath 模板图片路径
     * @param searchArea 搜索区域
     * @param config 匹配配置
     * @return 匹配结果
     */
    public static MatchResult matchImageInArea(String templatePath, Rectangle searchArea, ImageMatchConfig config) {
        if (!opencvLoaded) {
            MatchResult result = new MatchResult();
            result.setMatched(false);
            System.err.println("OpenCV库未加载，无法进行图像匹配");
            return result;
        }
        
        try {
            // 截取指定区域
            String screenshotPath = captureAreaForMatch(searchArea, config);
            if (screenshotPath == null) {
                MatchResult result = new MatchResult();
                result.setMatched(false);
                return result;
            }
            
            MatchResult result = matchImages(templatePath, screenshotPath, config);
            
            // 调整坐标到全屏坐标系
            if (result.isMatched() && result.getLocation() != null) {
                java.awt.Point adjustedLocation = new java.awt.Point(
                    result.getLocation().x + searchArea.x,
                    result.getLocation().y + searchArea.y
                );
                result.setLocation(adjustedLocation);
                
                if (result.getMatchArea() != null) {
                    java.awt.Rectangle adjustedArea = new java.awt.Rectangle(
                        result.getMatchArea().x + searchArea.x,
                        result.getMatchArea().y + searchArea.y,
                        result.getMatchArea().width,
                        result.getMatchArea().height
                    );
                    result.setMatchArea(adjustedArea);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("区域图像匹配过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            MatchResult result = new MatchResult();
            result.setMatched(false);
            return result;
        }
    }
    
    /**
     * 匹配两个图像文件
     * 
     * @param templatePath 模板图片路径
     * @param targetPath 目标图片路径
     * @param config 匹配配置
     * @return 匹配结果
     */
    public static MatchResult matchImages(String templatePath, String targetPath, ImageMatchConfig config) {
        MatchResult result = new MatchResult();
        result.setTemplatePath(templatePath);
        result.setScreenshotPath(targetPath);
        
        try {
            // 加载图像
            Mat template = Imgcodecs.imread(templatePath);
            Mat target = Imgcodecs.imread(targetPath);
            
            if (template.empty()) {
                System.err.println("无法加载模板图片: " + templatePath);
                return result;
            }
            
            if (target.empty()) {
                System.err.println("无法加载目标图片: " + targetPath);
                return result;
            }
            
            // 图像预处理
            Mat processedTemplate = preprocessImage(template, config, "template");
            Mat processedTarget = preprocessImage(target, config, "target");
            
            // 执行模板匹配
            Mat matchResult = new Mat();
            Imgproc.matchTemplate(processedTarget, processedTemplate, matchResult, config.getMatchMethod());
            
            // 找到最佳匹配位置
            Core.MinMaxLocResult mmr = Core.minMaxLoc(matchResult);
            
            double confidence;
            org.opencv.core.Point matchLoc;
            
            // 根据匹配方法确定使用最大值还是最小值
            if (config.getMatchMethod() == Imgproc.TM_SQDIFF || config.getMatchMethod() == Imgproc.TM_SQDIFF_NORMED) {
                confidence = 1.0 - mmr.minVal;
                matchLoc = mmr.minLoc;
            } else {
                confidence = mmr.maxVal;
                matchLoc = mmr.maxLoc;
            }
            
            // 设置结果
            result.setConfidence(confidence);
            result.setLocation(new java.awt.Point((int)matchLoc.x, (int)matchLoc.y));
            
            // 设置匹配区域
            java.awt.Rectangle matchArea = new java.awt.Rectangle(
                (int)matchLoc.x, 
                (int)matchLoc.y, 
                processedTemplate.cols(), 
                processedTemplate.rows()
            );
            result.setMatchArea(matchArea);
            
            // 判断是否匹配成功
            boolean matched = confidence >= config.getThreshold();
            result.setMatched(matched);
            
            System.out.println("图像匹配完成 - 置信度: " + String.format("%.4f", confidence) + 
                             ", 阈值: " + config.getThreshold() + 
                             ", 匹配: " + (matched ? "成功" : "失败"));
            
            if (matched) {
                System.out.println("匹配位置: (" + matchLoc.x + ", " + matchLoc.y + ")");
            }
            
            // 保存处理过程图片（如果启用）
            if (config.isSaveProcessImages()) {
                saveProcessImages(processedTemplate, processedTarget, matchResult, config);
            }
            
            // 释放资源
            template.release();
            target.release();
            processedTemplate.release();
            processedTarget.release();
            matchResult.release();
            
            return result;
            
        } catch (Exception e) {
            System.err.println("图像匹配异常: " + e.getMessage());
            e.printStackTrace();
            return result;
        }
    }
    
    /**
     * 图像预处理
     * 
     * @param image 原始图像
     * @param config 配置参数
     * @param imageType 图像类型（用于保存文件名）
     * @return 处理后的图像
     */
    private static Mat preprocessImage(Mat image, ImageMatchConfig config, String imageType) {
        Mat processed = image.clone();
        
        try {
            // 1. 灰度化处理
            if (config.isEnableGrayscale() && processed.channels() > 1) {
                Mat gray = new Mat();
                Imgproc.cvtColor(processed, gray, Imgproc.COLOR_BGR2GRAY);
                processed.release();
                processed = gray;
                System.out.println("应用灰度化处理 - " + imageType);
            }
            
            // 2. 高斯模糊去噪
            if (config.isEnableGaussianBlur()) {
                Mat blurred = new Mat();
                Size kernelSize = new Size(config.getGaussianKernelSize(), config.getGaussianKernelSize());
                Imgproc.GaussianBlur(processed, blurred, kernelSize, 
                                   config.getGaussianSigmaX(), config.getGaussianSigmaY());
                processed.release();
                processed = blurred;
                System.out.println("应用高斯模糊处理 - " + imageType);
            }
            
            // 3. 二值化处理
            if (config.isEnableBinarization()) {
                Mat binary = new Mat();
                Imgproc.threshold(processed, binary, config.getBinaryThreshold(), 
                                config.getBinaryMaxValue(), Imgproc.THRESH_BINARY);
                processed.release();
                processed = binary;
                System.out.println("应用二值化处理 - " + imageType);
            }
            
            // 4. 图像缩放
            if (config.isEnableScaling() && 
                (config.getScaleFactorX() != 1.0 || config.getScaleFactorY() != 1.0)) {
                Mat scaled = new Mat();
                Size newSize = new Size(
                    processed.cols() * config.getScaleFactorX(),
                    processed.rows() * config.getScaleFactorY()
                );
                Imgproc.resize(processed, scaled, newSize);
                processed.release();
                processed = scaled;
                System.out.println("应用缩放处理 - " + imageType + 
                                 " (缩放比例: " + config.getScaleFactorX() + "x" + config.getScaleFactorY() + ")");
            }
            
            return processed;
            
        } catch (Exception e) {
            System.err.println("图像预处理异常: " + e.getMessage());
            e.printStackTrace();
            return processed;
        }
    }
    
    /**
     * 截取屏幕用于匹配
     * 
     * @param config 配置参数
     * @return 截图文件路径
     */
    private static String captureScreenForMatch(ImageMatchConfig config) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String screenshotPath = config.getOutputDir() + "/screen_" + timestamp + ".png";
            
            // 确保输出目录存在
            File outputDir = new File(config.getOutputDir());
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            boolean success = ScreenshotUtil.captureFullScreen(screenshotPath);
            return success ? screenshotPath : null;
            
        } catch (Exception e) {
            System.err.println("截取屏幕失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 截取指定区域用于匹配
     * 
     * @param area 截取区域
     * @param config 配置参数
     * @return 截图文件路径
     */
    private static String captureAreaForMatch(Rectangle area, ImageMatchConfig config) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String screenshotPath = config.getOutputDir() + "/area_" + timestamp + ".png";
            
            // 确保输出目录存在
            File outputDir = new File(config.getOutputDir());
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            boolean success = ScreenshotUtil.captureScreen(area.x, area.y, area.width, area.height, screenshotPath);
            return success ? screenshotPath : null;
            
        } catch (Exception e) {
            System.err.println("截取区域失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 保存处理过程图片
     * 
     * @param template 处理后的模板图像
     * @param target 处理后的目标图像
     * @param matchResult 匹配结果矩阵
     * @param config 配置参数
     */
    private static void saveProcessImages(Mat template, Mat target, Mat matchResult, ImageMatchConfig config) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String outputDir = config.getOutputDir() + "/process_" + timestamp;
            
            File dir = new File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 保存处理后的模板图像
            Imgcodecs.imwrite(outputDir + "/processed_template.png", template);
            
            // 保存处理后的目标图像
            Imgcodecs.imwrite(outputDir + "/processed_target.png", target);
            
            // 保存匹配结果热力图
            Mat normalizedResult = new Mat();
            Core.normalize(matchResult, normalizedResult, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
            Imgcodecs.imwrite(outputDir + "/match_heatmap.png", normalizedResult);
            
            normalizedResult.release();
            
            System.out.println("处理过程图片已保存到: " + outputDir);
            
        } catch (Exception e) {
            System.err.println("保存处理过程图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查OpenCV是否已加载
     * 
     * @return 是否已加载
     */
    public static boolean isOpenCVLoaded() {
        return opencvLoaded;
    }
}