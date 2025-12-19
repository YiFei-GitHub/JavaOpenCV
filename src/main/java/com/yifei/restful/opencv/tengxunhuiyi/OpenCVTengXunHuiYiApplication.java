package com.yifei.restful.opencv.tengxunhuiyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * OpenCV自动化工具主启动类
 * 
 * @author yifei
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
public class OpenCVTengXunHuiYiApplication {

    public static void main(String[] args) {
        // 设置系统属性，禁用headless模式，允许GUI操作
        System.setProperty("java.awt.headless", "false");
        
        SpringApplication.run(OpenCVTengXunHuiYiApplication.class, args);
    }
}