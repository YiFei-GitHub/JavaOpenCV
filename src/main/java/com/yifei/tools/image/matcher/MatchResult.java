package com.yifei.tools.image.matcher;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 图像匹配结果类
 * 包含匹配的详细信息
 * 
 * @author yifei
 * @version 1.0
 */
public class MatchResult {
    
    private boolean matched;
    private Point location;
    private Rectangle matchArea;
    private double confidence;
    private String templatePath;
    private String screenshotPath;
    private long matchTime;
    
    public MatchResult() {
        this.matched = false;
        this.confidence = 0.0;
        this.matchTime = System.currentTimeMillis();
    }
    
    public MatchResult(boolean matched, Point location, double confidence) {
        this.matched = matched;
        this.location = location;
        this.confidence = confidence;
        this.matchTime = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public boolean isMatched() {
        return matched;
    }
    
    public void setMatched(boolean matched) {
        this.matched = matched;
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(Point location) {
        this.location = location;
    }
    
    public Rectangle getMatchArea() {
        return matchArea;
    }
    
    public void setMatchArea(Rectangle matchArea) {
        this.matchArea = matchArea;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public String getTemplatePath() {
        return templatePath;
    }
    
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
    
    public String getScreenshotPath() {
        return screenshotPath;
    }
    
    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }
    
    public long getMatchTime() {
        return matchTime;
    }
    
    public void setMatchTime(long matchTime) {
        this.matchTime = matchTime;
    }
    
    /**
     * 获取匹配区域的中心点
     * 
     * @return 中心点坐标
     */
    public Point getCenterPoint() {
        if (matchArea != null) {
            return new java.awt.Point(
                matchArea.x + matchArea.width / 2,
                matchArea.y + matchArea.height / 2
            );
        } else if (location != null) {
            return new java.awt.Point(location.x, location.y);
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MatchResult{");
        sb.append("matched=").append(matched);
        sb.append(", confidence=").append(String.format("%.4f", confidence));
        
        if (location != null) {
            sb.append(", location=(").append(location.x).append(",").append(location.y).append(")");
        }
        
        if (matchArea != null) {
            sb.append(", area=(").append(matchArea.x).append(",").append(matchArea.y)
              .append(",").append(matchArea.width).append("x").append(matchArea.height).append(")");
        }
        
        Point center = getCenterPoint();
        if (center != null) {
            sb.append(", center=(").append(center.x).append(",").append(center.y).append(")");
        }
        
        sb.append(", matchTime=").append(matchTime);
        sb.append('}');
        
        return sb.toString();
    }
}