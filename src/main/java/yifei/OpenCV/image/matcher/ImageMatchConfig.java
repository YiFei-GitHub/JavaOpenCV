package yifei.OpenCV.image.matcher;

/**
 * 图像匹配配置类
 * 用于配置匹配参数和预处理选项
 * 
 * @author yifei
 * @version 1.0
 */
public class ImageMatchConfig {
    
    // 匹配阈值
    private double threshold = 0.9;
    
    // 预处理选项
    private boolean enableGrayscale = true;
    private boolean enableGaussianBlur = true;
    private boolean enableBinarization = false;
    private boolean enableScaling = false;
    
    // 高斯模糊参数
    private int gaussianKernelSize = 5;
    private double gaussianSigmaX = 1.0;
    private double gaussianSigmaY = 1.0;
    
    // 二值化参数
    private double binaryThreshold = 127.0;
    private double binaryMaxValue = 255.0;
    
    // 缩放参数
    private double scaleFactorX = 1.0;
    private double scaleFactorY = 1.0;
    
    // 匹配方法
    private int matchMethod = 5; // CV_TM_CCOEFF_NORMED
    
    // 是否保存匹配过程图片
    private boolean saveProcessImages = false;
    private String outputDir = "match_results";
    
    // 默认配置
    public static ImageMatchConfig getDefaultConfig() {
        return new ImageMatchConfig();
    }
    
    // 高精度配置
    public static ImageMatchConfig getHighPrecisionConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        config.setThreshold(0.95);
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(true);
        config.setGaussianKernelSize(3);
        config.setGaussianSigmaX(0.8);
        config.setGaussianSigmaY(0.8);
        return config;
    }
    
    // 快速匹配配置
    public static ImageMatchConfig getFastMatchConfig() {
        ImageMatchConfig config = new ImageMatchConfig();
        config.setThreshold(0.8);
        config.setEnableGrayscale(true);
        config.setEnableGaussianBlur(false);
        return config;
    }
    
    // Getters and Setters
    public double getThreshold() {
        return threshold;
    }
    
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public boolean isEnableGrayscale() {
        return enableGrayscale;
    }
    
    public void setEnableGrayscale(boolean enableGrayscale) {
        this.enableGrayscale = enableGrayscale;
    }
    
    public boolean isEnableGaussianBlur() {
        return enableGaussianBlur;
    }
    
    public void setEnableGaussianBlur(boolean enableGaussianBlur) {
        this.enableGaussianBlur = enableGaussianBlur;
    }
    
    public boolean isEnableBinarization() {
        return enableBinarization;
    }
    
    public void setEnableBinarization(boolean enableBinarization) {
        this.enableBinarization = enableBinarization;
    }
    
    public boolean isEnableScaling() {
        return enableScaling;
    }
    
    public void setEnableScaling(boolean enableScaling) {
        this.enableScaling = enableScaling;
    }
    
    public int getGaussianKernelSize() {
        return gaussianKernelSize;
    }
    
    public void setGaussianKernelSize(int gaussianKernelSize) {
        this.gaussianKernelSize = gaussianKernelSize;
    }
    
    public double getGaussianSigmaX() {
        return gaussianSigmaX;
    }
    
    public void setGaussianSigmaX(double gaussianSigmaX) {
        this.gaussianSigmaX = gaussianSigmaX;
    }
    
    public double getGaussianSigmaY() {
        return gaussianSigmaY;
    }
    
    public void setGaussianSigmaY(double gaussianSigmaY) {
        this.gaussianSigmaY = gaussianSigmaY;
    }
    
    public double getBinaryThreshold() {
        return binaryThreshold;
    }
    
    public void setBinaryThreshold(double binaryThreshold) {
        this.binaryThreshold = binaryThreshold;
    }
    
    public double getBinaryMaxValue() {
        return binaryMaxValue;
    }
    
    public void setBinaryMaxValue(double binaryMaxValue) {
        this.binaryMaxValue = binaryMaxValue;
    }
    
    public double getScaleFactorX() {
        return scaleFactorX;
    }
    
    public void setScaleFactorX(double scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }
    
    public double getScaleFactorY() {
        return scaleFactorY;
    }
    
    public void setScaleFactorY(double scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }
    
    public int getMatchMethod() {
        return matchMethod;
    }
    
    public void setMatchMethod(int matchMethod) {
        this.matchMethod = matchMethod;
    }
    
    public boolean isSaveProcessImages() {
        return saveProcessImages;
    }
    
    public void setSaveProcessImages(boolean saveProcessImages) {
        this.saveProcessImages = saveProcessImages;
    }
    
    public String getOutputDir() {
        return outputDir;
    }
    
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}