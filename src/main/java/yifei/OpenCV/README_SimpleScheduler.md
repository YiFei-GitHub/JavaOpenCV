# 简单定时任务工具使用说明

## 功能概述
`SimpleScheduler` 是一个简单的单线程定时任务工具类，提供基础的定时执行功能，无需复杂的多线程管理。

## 核心方法

### 1. 延迟执行
```java
// 5秒后执行任务
SimpleScheduler.runAfterDelay(() -> {
    System.out.println("延迟任务执行了！");
}, 5);
```

### 2. 固定间隔执行
```java
// 每3秒执行一次，共执行5次
SimpleScheduler.runWithInterval(() -> {
    System.out.println("间隔任务执行了！");
}, 3, 5);

// 无限执行（maxExecutions设为-1）
SimpleScheduler.runWithInterval(() -> {
    System.out.println("无限执行任务！");
}, 10, -1);
```

### 3. 指定时间执行
```java
// 今天或明天的7点执行一次
SimpleScheduler.runAtTime(() -> {
    System.out.println("7点任务执行了！");
}, 7, 0);
```

### 4. 每天定时执行
```java
// 每天7点执行，执行30天
SimpleScheduler.runDailyAtTime(() -> {
    System.out.println("每日7点任务！");
}, 7, 0, 30);

// 每天7点执行，无限执行
SimpleScheduler.runDailyAtTime(() -> {
    System.out.println("每日7点任务！");
}, 7, 0, -1);
```

## 实用示例

### 定时截图
```java
// 每30秒截图一次，共截图10次
SimpleScheduler.runWithInterval(() -> {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    String filename = "screenshots/auto_" + timestamp + ".png";
    ScreenshotUtil.captureFullScreen(filename);
}, 30, 10);
```

### 每日备份任务
```java
// 每天凌晨2点执行备份
SimpleScheduler.runDailyAtTime(() -> {
    System.out.println("开始每日备份...");
    // 执行备份逻辑
    System.out.println("备份完成！");
}, 2, 0, -1);
```

### 系统监控
```java
// 每5分钟检查一次系统状态，检查24小时
SimpleScheduler.runWithInterval(() -> {
    System.out.println("检查系统状态...");
    // 检查CPU、内存等
}, 300, 288); // 5分钟 * 288次 = 24小时
```

## 特点

### 优点
- **简单易用**: 无需复杂的配置和管理
- **单线程**: 避免多线程复杂性和资源竞争
- **轻量级**: 不依赖外部库，使用Java标准API
- **直观**: 方法名和参数含义清晰
- **灵活**: 支持Lambda表达式和方法引用

### 适用场景
- 简单的定时任务需求
- 单机应用的定时处理
- 测试和演示场景
- 不需要复杂调度的场景
- 学习和教学用途

### 注意事项
1. **阻塞执行**: 任务是顺序执行的，一个任务完成后才执行下一个
2. **异常处理**: 任务异常不会中断调度器，会记录错误信息
3. **时间精度**: 基于`Thread.sleep()`，精度受系统影响
4. **中断支持**: 支持`Thread.interrupt()`中断执行
5. **内存占用**: 长时间运行需要注意内存使用

## 与复杂调度器的对比

| 特性 | SimpleScheduler | TaskScheduler |
|------|----------------|---------------|
| 复杂度 | 简单 | 复杂 |
| 多线程 | 单线程 | 多线程 |
| 并发执行 | 不支持 | 支持 |
| 任务管理 | 无 | 完整管理 |
| Cron表达式 | 不支持 | 支持 |
| 适用场景 | 简单需求 | 复杂调度 |

## 运行演示

运行 `SimpleSchedulerDemo` 类可以看到所有功能的演示效果。

## 最佳实践

1. **任务设计**: 保持任务简单，避免长时间运行
2. **异常处理**: 在任务内部处理可能的异常
3. **资源管理**: 及时释放任务中使用的资源
4. **日志记录**: 添加适当的日志记录便于调试
5. **测试验证**: 充分测试定时逻辑的正确性