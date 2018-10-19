package org.design.pattern;


public class SingletonTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        if (singleton1 == singleton2)
            System.out.println("same");
    }
}
