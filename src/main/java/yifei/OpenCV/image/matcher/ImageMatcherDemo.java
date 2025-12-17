package yifei.OpenCV.image.matcher;

import yifei.OpenCV.screenshot.ScreenshotUtil;
import java.awt.Rectangle;
import java.io.File;

/**
 * 图像匹配工具演示类
 * 展示图像匹配的完整功能
 * 
 * @author yifei
 * @version 1.0
 */
public class ImageMatcherDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 图像识别匹配工具演示 ===");
        
        // 检查OpenCV是否加载成功
        if (!ImageMatcher.isOpenCVLoaded()) {
            System.err.println("OpenCV库未加载，无法运行演示");
            return;
        }
        
        try {
            // 使用腾讯会议图片作为模板
            String templatePath = "src/main/resources/TengXunHuiYi/TengXunHuiYi.png";
            
            // 检查模板文件是否存在
            File templateFile = new File(templatePath);
            if (!templateFile.exists()) {
                System.err.println("模板图片不存在: " + templatePath);
                System.out.println("正在创建示例模板图片...");
                
                // 如果腾讯会议图片不存在，则创建示例模板
                templatePath = createExampleTemplate();
                if (templatePath == null) {
                    System.err.println("无法创建示例模板，演示终止");
                    return;
                }
            } else {
                System.out.println("使用模板图片: " + templatePath);
            }
            
            // === 演示1: 基础图像匹配 ===
//            System.out.println("\n1. 基础图像匹配（使用默认配置）");
//            MatchResult result1 = ImageMatcher.matchImage(templatePath);
//            printMatchResult("基础匹配", result1);
            
            // === 演示2: 高精度匹配 ===
//            System.out.println("\n2. 高精度图像匹配");
//            ImageMatchConfig highPrecisionConfig = ImageMatchConfig.getHighPrecisionConfig();
//            highPrecisionConfig.setSaveProcessImages(true); // 保存处理过程图片
//            MatchResult result2 = ImageMatcher.matchImage(templatePath, highPrecisionConfig);
//            printMatchResult("高精度匹配", result2);
//
//            // === 演示3: 快速匹配 ===
            System.out.println("\n3. 快速图像匹配");
            ImageMatchConfig fastConfig = ImageMatchConfig.getFastMatchConfig();
            MatchResult result3 = ImageMatcher.matchImage(templatePath, fastConfig);
            printMatchResult("快速匹配", result3);
//
//            // === 演示4: 区域匹配 ===
//            System.out.println("\n4. 指定区域图像匹配（屏幕左上角 600x400 区域）");
//            Rectangle searchArea = new Rectangle(0, 0, 600, 400);
//            MatchResult result4 = ImageMatcher.matchImageInArea(templatePath, searchArea,
//                                                              ImageMatchConfig.getDefaultConfig());
//            printMatchResult("区域匹配", result4);
//
//            // === 演示5: 自定义配置匹配 ===
//            System.out.println("\n5. 自定义配置图像匹配");
//            ImageMatchConfig customConfig = createCustomConfig();
//            MatchResult result5 = ImageMatcher.matchImage(templatePath, customConfig);
//            printMatchResult("自定义配置匹配", result5);
//
//            // === 演示6: 不同匹配方法对比 ===
//            System.out.println("\n6. 不同匹配方法效果对比");
//            demonstrateMatchMethods(templatePath);
//
//            // === 演示7: 实际应用示例 ===
//            System.out.println("\n7. 实际应用示例");
//            demonstrateRealWorldUsage(templatePath);
            
            System.out.println("\n=== 演示完成 ===");
            System.out.println("匹配结果图片保存在 match_results 目录中");
            
        } catch (Exception e) {
            System.err.println("演示过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 创建示例模板图片
     * 
     * @return 模板图片路径
     */
    private static String createExampleTemplate() {
        try {
            // 确保输出目录存在
            File outputDir = new File("match_results");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            String templatePath = "match_results/example_template.png";
            
            // 截取屏幕左上角作为模板
            boolean success = ScreenshotUtil.captureScreen(50, 50, 150, 100, templatePath);
            
            if (success) {
                System.out.println("示例模板已创建: " + templatePath);
                System.out.println("模板尺寸: 150x100 像素，位置: (50, 50)");
                return templatePath;
            } else {
                System.err.println("创建示例模板失败");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("创建模板异常: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 创建自定义配置
     * 
     * @return 自定义配置
     */
    private static ImageMatchConfig createCustomConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        
        // 设置匹配阈值
        config.setThreshold(0.85);
        
        // 启用预处理
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(true);
        config.setEnableBinarization(false);
        
        // 高斯模糊参数
        config.setGaussianKernelSize(3);
        config.setGaussianSigmaX(0.8);
        config.setGaussianSigmaY(0.8);
        
        // 保存处理过程图片
        config.setSaveProcessImages(true);
        
        return config;
    }
    
    /**
     * 打印匹配结果
     * 
     * @param title 标题
     * @param result 匹配结果
     */
    private static void printMatchResult(String title, MatchResult result) {
        System.out.println("--- " + title + " ---");
        System.out.println("匹配成功: " + (result.isMatched() ? "是" : "否"));
        System.out.println("置信度: " + String.format("%.4f", result.getConfidence()));
        
        if (result.isMatched()) {
            if (result.getLocation() != null) {
                System.out.println("匹配位置: (" + result.getLocation().x + ", " + result.getLocation().y + ")");
            }
            
            if (result.getMatchArea() != null) {
                Rectangle area = result.getMatchArea();
                System.out.println("匹配区域: (" + area.x + ", " + area.y + 
                                 ", " + area.width + "x" + area.height + ")");
            }
            
            if (result.getCenterPoint() != null) {
                System.out.println("中心点: (" + result.getCenterPoint().x + ", " + result.getCenterPoint().y + ")");
            }
        }
        
        System.out.println();
    }
    
    /**
     * 演示不同匹配方法
     * 
     * @param templatePath 模板图片路径
     */
    private static void demonstrateMatchMethods(String templatePath) {
        String[] methodNames = {
            "TM_SQDIFF", "TM_SQDIFF_NORMED", "TM_CCORR", 
            "TM_CCORR_NORMED", "TM_CCOEFF", "TM_CCOEFF_NORMED"
        };
        
        int[] methods = {0, 1, 2, 3, 4, 5};
        
        for (int i = 0; i < methods.length; i++) {
            ImageMatchConfig config = ImageMatchConfig.getDefaultConfig();
            config.setMatchMethod(methods[i]);
            config.setThreshold(0.8); // 降低阈值以便观察不同方法的效果
            
            MatchResult result = ImageMatcher.matchImage(templatePath, config);
            
            System.out.println(methodNames[i] + ": 置信度=" + String.format("%.4f", result.getConfidence()) + 
                             ", 匹配=" + (result.isMatched() ? "成功" : "失败"));
        }
    }
    
    /**
     * 演示实际应用场景
     * 
     * @param templatePath 模板路径
     */
    private static void demonstrateRealWorldUsage(String templatePath) {
        System.out.println("实际应用场景示例:");
        System.out.println();
        
        // 场景1: 桌面图标检测
        System.out.println("场景1: 桌面图标检测");
        ImageMatchConfig iconConfig = new ImageMatchConfig();
        iconConfig.setThreshold(0.9);
        iconConfig.setEnableGrayscale(true);
        
        MatchResult iconResult = ImageMatcher.matchImage(templatePath, iconConfig);
        if (iconResult.isMatched()) {
            System.out.println("  找到桌面图标，位置: " + iconResult.getLocation());
            System.out.println("  可以执行点击操作");
        } else {
            System.out.println("  未找到指定图标");
        }
        
        // 场景2: UI自动化测试
        System.out.println("\n场景2: UI自动化测试");
        ImageMatchConfig uiConfig = ImageMatchConfig.getHighPrecisionConfig();
        uiConfig.setThreshold(0.95);
        
        MatchResult uiResult = ImageMatcher.matchImage(templatePath, uiConfig);
        if (uiResult.isMatched()) {
            System.out.println("  UI元素验证通过");
            System.out.println("  测试用例: PASS");
        } else {
            System.out.println("  UI元素未找到");
            System.out.println("  测试用例: FAIL");
        }
        
        // 场景3: 游戏辅助
        System.out.println("\n场景3: 游戏辅助应用");
        Rectangle gameArea = new Rectangle(100, 100, 800, 600);
        ImageMatchConfig gameConfig = new ImageMatchConfig();
        gameConfig.setThreshold(0.88);
        gameConfig.setEnableGrayscale(true);
        gameConfig.setEnableGaussianBlur(true);
        
        MatchResult gameResult = ImageMatcher.matchImageInArea(templatePath, gameArea, gameConfig);
        if (gameResult.isMatched()) {
            System.out.println("  游戏目标检测成功");
            System.out.println("  可以执行游戏操作");
        } else {
            System.out.println("  游戏目标未出现");
        }
        
        System.out.println("\n使用建议:");
        System.out.println("1. 选择具有独特特征的图像作为模板");
        System.out.println("2. 根据应用场景调整匹配阈值");
        System.out.println("3. 使用区域匹配提高性能");
        System.out.println("4. 启用适当的预处理方法");
        System.out.println("5. 保存处理过程图片用于调试");
    }
}