package com.yifei.restful.opencv;

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
public class OpenCVApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenCVApplication.class, args);
    }
}