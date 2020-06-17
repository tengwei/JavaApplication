package org.javacore.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadDomain401 {
    public static void main(String[] args) throws Exception {
        ThreadDomain40 td = new ThreadDomain40();
        MyThread40 mt = new MyThread40(td);
        mt.start();
        Thread.sleep(3000);
        td.signal();
    }
}


class ThreadDomain40 {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void await() {
        try {
            lock.lock();
            System.out.println("await时间为：" + System.currentTimeMillis());
            condition.await();
            System.out.println("await等待结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("signal时间为：" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
        System.out.println("signal时间为1：" + System.currentTimeMillis());
    }
}

class MyThread40 extends Thread {
    private ThreadDomain40 td;

    public MyThread40(ThreadDomain40 td) {
        this.td = td;
    }

    public void run() {
        td.await();
    }
}
