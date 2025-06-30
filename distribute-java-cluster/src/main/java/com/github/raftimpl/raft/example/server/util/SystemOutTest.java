package com.github.raftimpl.raft.example.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System.out 重定向测试类
 */
public class SystemOutTest {
    private static final Logger LOG = LoggerFactory.getLogger(SystemOutTest.class);

    public static void main(String[] args) {
        LOG.info("开始测试 System.out 重定向功能");
        
        // 测试重定向前的输出
        System.out.println("这是重定向前的 System.out.println 输出");
        
        // 启用重定向
        SystemOutRedirector.redirectSystemOut();
        LOG.info("System.out 重定向已启用");
        
        // 测试重定向后的输出
        System.out.println("这是重定向后的 System.out.println 输出 - 应该写入日志文件");
        System.out.println("第二行输出");
        System.out.println("第三行输出");
        
        // 测试恢复
        SystemOutRedirector.restoreSystemOut();
        LOG.info("System.out 已恢复到原始状态");
        
        // 测试恢复后的输出
        System.out.println("这是恢复后的 System.out.println 输出 - 应该显示在控制台");
        
        LOG.info("System.out 重定向测试完成");
    }
} 