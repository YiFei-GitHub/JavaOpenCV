package com.yifei.restful.opencv.tengxunhuiyi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 资源配置类
 * 统一管理所有静态资源路径配置
 * 
 * 配置来源：src/main/resources/application.yml
 * 配置前缀：opencv
 * 
 * 注意：此类不设置默认值，所有配置必须在application.yml中定义
 * 
 * @author yifei
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "opencv")
public class ResourceConfig {
    
    private Templates templates = new Templates();
    private Match match = new Match();
    private Schedule schedule = new Schedule();
    private Output output = new Output();
    
    /**
     * 模板图片路径配置
     * 所有值从application.yml中读取，不设置默认值
     */
    public static class Templates {
        private String tengxunHuiyi;
        private String ruhuiButton;
        
        public String getTengxunHuiyi() {
            return tengxunHuiyi;
        }
        
        public void setTengxunHuiyi(String tengxunHuiyi) {
            this.tengxunHuiyi = tengxunHuiyi;
        }
        
        public String getRuhuiButton() {
            return ruhuiButton;
        }
        
        public void setRuhuiButton(String ruhuiButton) {
            this.ruhuiButton = ruhuiButton;
        }
    }
    
    /**
     * 匹配配置
     * 所有值从application.yml中读取，不设置默认值
     */
    public static class Match {
        private double threshold;
        private int maxRetryTimes;
        private int waitSeconds;
        
        public double getThreshold() {
            return threshold;
        }
        
        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }
        
        public int getMaxRetryTimes() {
            return maxRetryTimes;
        }
        
        public void setMaxRetryTimes(int maxRetryTimes) {
            this.maxRetryTimes = maxRetryTimes;
        }
        
        public int getWaitSeconds() {
            return waitSeconds;
        }
        
        public void setWaitSeconds(int waitSeconds) {
            this.waitSeconds = waitSeconds;
        }
    }
    
    /**
     * 定时任务配置
     * 所有值从application.yml中读取，不设置默认值
     */
    public static class Schedule {
        private String cron;
        private boolean enabled;
        
        public String getCron() {
            return cron;
        }
        
        public void setCron(String cron) {
            this.cron = cron;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    
    public Templates getTemplates() {
        return templates;
    }
    
    public void setTemplates(Templates templates) {
        this.templates = templates;
    }
    
    public Match getMatch() {
        return match;
    }
    
    public void setMatch(Match match) {
        this.match = match;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
    public Output getOutput() {
        return output;
    }
    
    public void setOutput(Output output) {
        this.output = output;
    }
    
    /**
     * 输出目录配置
     * 所有值从application.yml中读取，不设置默认值
     */
    public static class Output {
        private String screenshotDir;
        private String matchResultDir;
        
        public String getScreenshotDir() {
            return screenshotDir;
        }
        
        public void setScreenshotDir(String screenshotDir) {
            this.screenshotDir = screenshotDir;
        }
        
        public String getMatchResultDir() {
            return matchResultDir;
        }
        
        public void setMatchResultDir(String matchResultDir) {
            this.matchResultDir = matchResultDir;
        }
    }
}
