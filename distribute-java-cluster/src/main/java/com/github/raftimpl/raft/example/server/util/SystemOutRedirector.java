package com.github.raftimpl.raft.example.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * System.out 重定向工具类
 * 将 System.out.println 的输出重定向到日志文件
 */
public class SystemOutRedirector {
    private static final Logger SYSTEM_OUT_LOGGER = LoggerFactory.getLogger("SystemOut");
    private static final PrintStream originalOut = System.out;
    private static boolean isRedirected = false;

    /**
     * 重定向 System.out 到日志文件
     */
    public static void redirectSystemOut() {
        if (isRedirected) {
            return;
        }

        PrintStream customOut = new PrintStream(new ByteArrayOutputStream() {
            @Override
            public void flush() {
                String output = toString(StandardCharsets.UTF_8);
                if (!output.isEmpty()) {
                    SYSTEM_OUT_LOGGER.info(output.trim());
                }
                reset();
            }
        }, true, StandardCharsets.UTF_8.name());

        System.setOut(customOut);
        isRedirected = true;
        SYSTEM_OUT_LOGGER.info("System.out has been redirected to log file");
    }

    /**
     * 恢复原始的 System.out
     */
    public static void restoreSystemOut() {
        if (!isRedirected) {
            return;
        }

        System.setOut(originalOut);
        isRedirected = false;
        SYSTEM_OUT_LOGGER.info("System.out has been restored to original");
    }

    /**
     * 检查是否已经重定向
     */
    public static boolean isRedirected() {
        return isRedirected;
    }
} 