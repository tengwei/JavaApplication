package org.javacore.thread;

public class JProfilerTest extends Thread {
    public static void main(String[] args) throws InterruptedException {
        JProfilerTest t = new JProfilerTest();
        for (int i = 1; i < 10000; i++) {
            new HelloWorld();
            t.sleep(100); // 休眠100毫秒
        }
    }
}

class HelloWorld {
    public HelloWorld() {
        System.out.println("Hello Jayzee!");
    }
}
