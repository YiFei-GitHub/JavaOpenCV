package yifei.automation;

/**
 * 自动化任务主程序
 * 从配置文件读取配置并执行自动化任务
 */
public class AutomationMain {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    自动化任务管理系统");
        System.out.println("========================================");
        
        // 加载配置
        TaskConfig config = TaskConfig.loadFromFile();
        
        // 验证配置
        if (!validateConfig(config)) {
            System.out.println("\n配置验证失败，请检查配置文件！");
            System.out.println("配置文件路径: " + new java.io.File("automation_config.properties").getAbsolutePath());
            return;
        }
        
        // 显示配置信息
        printConfig(config);
        
        // 创建调度管理器
        ScheduleManager scheduleManager = new ScheduleManager(config);
        
        // 启动每分钟输出时间的任务（无论什么情况都运行）
        System.out.println("\n已启动时间输出任务，每分钟输出当前时间");
        scheduleManager.startTimeOutputTask();
        
        // 检查命令行参数
        if (args.length > 0 && args[0].equals("--now")) {
            // 立即执行
            System.out.println("\n立即执行任务...");
            scheduleManager.executeNow();
            
            // 保持程序运行以便时间输出任务继续工作
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println("\n程序被中断，正在退出...");
            } finally {
                scheduleManager.stop();
            }
        } else if (config.isEnableSchedule()) {
            // 启动定时任务
            System.out.println("\n定时任务已启用，将在每天 " + config.getScheduleTime() + " 执行");
            System.out.println("程序将保持运行，按 Ctrl+C 退出");
            scheduleManager.start();
            
            // 保持程序运行
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println("\n程序被中断，正在退出...");
            } finally {
                scheduleManager.stop();
            }
        } else {
            // 定时任务未启用，立即执行一次
            System.out.println("\n定时任务未启用，立即执行一次任务...");
            scheduleManager.executeNow();
            
            // 保持程序运行以便时间输出任务继续工作
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println("\n程序被中断，正在退出...");
            } finally {
                scheduleManager.stop();
            }
        }
    }
    
    /**
     * 验证配置是否完整
     */
    private static boolean validateConfig(TaskConfig config) {
        boolean valid = true;
        
        if (config.getTargetImagePath() == null || config.getTargetImagePath().trim().isEmpty()) {
            System.err.println("错误: 未配置目标图片路径 (target.image.path)");
            valid = false;
        } else {
            java.io.File targetFile = new java.io.File(config.getTargetImagePath());
            if (!targetFile.exists()) {
                System.err.println("错误: 目标图片文件不存在: " + config.getTargetImagePath());
                valid = false;
            }
        }
        
        // 如果配置了桌面图标，检查图标文件是否存在
        if (config.getDesktopIconPath() != null && !config.getDesktopIconPath().trim().isEmpty()) {
            java.io.File iconFile = new java.io.File(config.getDesktopIconPath());
            if (!iconFile.exists()) {
                System.err.println("警告: 桌面图标文件不存在: " + config.getDesktopIconPath());
                System.err.println("将跳过桌面图标匹配步骤");
            }
        }
        
        return valid;
    }
    
    /**
     * 打印当前配置
     */
    private static void printConfig(TaskConfig config) {
        System.out.println("\n========== 当前配置 ==========");
        System.out.println("定时任务:");
        System.out.println("  启用: " + (config.isEnableSchedule() ? "是" : "否"));
        System.out.println("  执行时间: " + config.getScheduleTime());
        System.out.println("桌面图标:");
        System.out.println("  路径: " + (config.getDesktopIconPath().isEmpty() ? "未配置（跳过）" : config.getDesktopIconPath()));
        System.out.println("  匹配阈值: " + config.getIconMatchThreshold());
        System.out.println("软件窗口:");
        System.out.println("  窗口标题: " + (config.getWindowTitle().isEmpty() ? "未配置" : config.getWindowTitle()));
        System.out.println("  进程名: " + (config.getWindowProcess().isEmpty() ? "未配置" : config.getWindowProcess()));
        System.out.println("  超时时间: " + config.getWaitWindowTimeout() + "秒");
        System.out.println("目标图片:");
        System.out.println("  路径: " + config.getTargetImagePath());
        System.out.println("  匹配阈值: " + config.getTargetMatchThreshold());
        System.out.println("点击选项:");
        System.out.println("  双击: " + (config.isDoubleClick() ? "是" : "否"));
        System.out.println("  延迟: " + config.getClickDelay() + "毫秒");
        System.out.println("===========================");
    }
}
