package com.yifei.restful.opencv.controller;

import com.yifei.restful.opencv.service.TengXunHuiYiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯会议控制器
 * 提供手动触发和状态查询接口
 * 
 * @author yifei
 * @version 1.0
 */
@RestController
@RequestMapping("/api/tengxun")
public class TengXunHuiYiController {
    
    private static final Logger logger = LoggerFactory.getLogger(TengXunHuiYiController.class);
    
    @Autowired
    private TengXunHuiYiService tengXunHuiYiService;
    
    /**
     * 手动触发腾讯会议自动入会任务
     * 
     * @return 执行结果
     */
    @GetMapping("/manual-join")
    public Map<String, Object> manualJoin() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.info("收到手动触发腾讯会议自动入会请求");
            tengXunHuiYiService.manualExecute();
            
            result.put("success", true);
            result.put("message", "腾讯会议自动入会任务已手动触发");
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            logger.error("手动触发腾讯会议自动入会任务失败: {}", e.getMessage(), e);
            
            result.put("success", false);
            result.put("message", "任务触发失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * 获取服务状态
     * 
     * @return 服务状态信息
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        
        status.put("service", "TengXunHuiYiService");
        status.put("status", "running");
        status.put("description", "腾讯会议自动入会服务");
        status.put("schedule", "每天早上6:55执行");
        status.put("timestamp", System.currentTimeMillis());
        
        return status;
    }
    
    /**
     * 健康检查接口
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        
        return health;
    }
}