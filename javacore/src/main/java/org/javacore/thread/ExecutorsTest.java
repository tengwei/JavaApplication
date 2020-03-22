package org.javacore.thread;

import java.util.concurrent.*;

public class ExecutorsTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建固定线程为5的线程池
        ExecutorService exec = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(50), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 500; i++) {
            int finalI = i;
            while (((ThreadPoolExecutor) exec).getQueue().size()>40){
                System.out.println("等待");
                Thread.sleep(10 * 1000);
            }
            exec.execute(() -> {
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + ":"+finalI);
            });
        }

        Thread.sleep(100 * 1000);
        System.out.println("结束");

    }
}
