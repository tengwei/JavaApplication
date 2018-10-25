package org.javacore.thread.interrurt;

import java.io.IOException;
import java.net.ServerSocket;

class Example6 extends Thread {
    volatile ServerSocket socket;

    public static void main(String args[]) throws Exception {
        Example6 thread = new Example6();
        System.out.println("Starting thread...");
        thread.start();
        Thread.sleep(3000);
        System.out.println("Asking thread to stop...");
        Thread.currentThread().interrupt();// 再调用interrupt方法
        thread.socket.close();// 再调用close方法
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        System.out.println("Stopping application...");
    }

    public void run() {
        try {
            socket = new ServerSocket(8888);
        } catch (IOException e) {
            System.out.println("Could not create the socket...");
            return;
        }
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Waiting for connection...");
            try {
                socket.accept();
            } catch (IOException e) {
                System.out.println("accept() failed or interrupted...");
                Thread.currentThread().interrupt();//重新设置中断标示位
            }
        }
        System.out.println("Thread exiting under request...");
    }
}

/*
一、没有任何语言方面的需求一个被中断的线程应该终止。中断一个线程只是为了引起该线程的注意，被中断线程可以决定如何应对中断。

        二、对于处于sleep，join等操作的线程，如果被调用interrupt()后，会抛出InterruptedException，然后线程的中断标志位会由true重置为false，因为线程为了处理异常已经重新处于就绪状态。

        三、不可中断的操作，包括进入synchronized段以及Lock.lock()，inputSteam.read()等，调用interrupt()对于这几个问题无效，因为它们都不抛出中断异常。如果拿不到资源，它们会无限期阻塞下去。

        对于Lock.lock()，可以改用Lock.lockInterruptibly()，可被中断的加锁操作，它可以抛出中断异常。等同于等待时间无限长的Lock.tryLock(long time, TimeUnit unit)。

        对于inputStream等资源，有些(实现了interruptibleChannel接口)可以通过close()方法将资源关闭，对应的阻塞也会被放开。*/
