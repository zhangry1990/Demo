package com.zhangry.demo.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangry on 2017/3/17.
 */
public class ThreadUtil {
    private ThreadUtil() {
    }

    public static void sleep(long durationMillis) {
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
        }

    }

    public static void sleep(long duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
        }

    }

    public static ThreadFactory buildJobFactory(String nameFormat) {
        return (new ThreadFactoryBuilder()).setNameFormat(nameFormat).build();
    }

    public static void gracefulShutdown(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout, TimeUnit timeUnit) {
        pool.shutdown();

        try {
            if(!pool.awaitTermination((long)shutdownTimeout, timeUnit)) {
                pool.shutdownNow();
                if(!pool.awaitTermination((long)shutdownNowTimeout, timeUnit)) {
                    System.err.println("Pool did not terminated");
                }
            }
        } catch (InterruptedException var5) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

    public static void normalShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit) {
        try {
            pool.shutdownNow();
            if(!pool.awaitTermination((long)timeout, timeUnit)) {
                System.err.println("Pool did not terminated");
            }
        } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
        }

    }

    public static class WrapExceptionRunnable implements Runnable {
        private static Logger logger = LoggerFactory.getLogger(ThreadUtil.WrapExceptionRunnable.class);
        private Runnable runnable;

        public WrapExceptionRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public void run() {
            try {
                this.runnable.run();
            } catch (Exception var2) {
                logger.error("Unexpected error occurred in task", var2);
            }

        }
    }
}

