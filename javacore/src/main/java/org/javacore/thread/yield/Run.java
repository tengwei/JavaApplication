package org.javacore.thread.yield;

public class Run {

    public static void main(String[] args) {
        YieldThread ta=new YieldThread("hello1");
        YieldThread tb=new YieldThread("hello2");
        ta.start();
        tb.start();
    }
}
