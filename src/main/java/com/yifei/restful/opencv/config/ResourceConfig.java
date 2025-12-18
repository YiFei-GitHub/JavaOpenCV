package com.yifei.restful.opencv.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 资源配置类
 * 统一管理所有静态资源路径配置
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
    
    /**
     * 模板图片路径配置
     */
    public static class Templates {
        private String tengxunHuiyi = "src/main/resources/templates/TengXunHuiYi/TengXunHuiYi.png";
        private String ruhuiButton = "src/main/resources/templates/TengXunHuiYi/TengXunHuiYi-RuHui.png";
        
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
     */
    public static class Match {
        private double threshold = 0.8;
        private int maxRetryTimes = 5;
        private int waitSeconds = 3;
        
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
     */
    public static class Schedule {
        private String cron = "0 55 6 * * ?";
        private boolean enabled = true;
        
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
}
