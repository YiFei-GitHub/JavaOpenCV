package com.yifei.restful.opencv.tengxunhuiyi.controller;

import com.yifei.restful.opencv.tengxunhuiyi.service.TengXunHuiYiService;
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

}