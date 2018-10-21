package org.javacore.unsafeClass;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UnsafeTest {

    static final Integer a;

    static {
        //unsafe = Unsafe.getUnsafe();
        a = 3;
        System.out.println(a);
    }

    private UnsafeTest() {
        //a = 3;
    }


    public static UnsafeTest getInstance() {
        return UnsafeTest.User.instance;
    }

    public static void main(String[] args) throws Exception {
        //UnsafeTest unsafeTest = new UnsafeTest();
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(Boolean.TRUE);
        Unsafe unsafe = (Unsafe) field.get(null);

        User user = (User) unsafe.allocateInstance(User.class);
        user.test();

        user.setName("111111");

        user.test();

        Field userField = User.class.getDeclaredField("name");
        userField.setAccessible(Boolean.TRUE);
        System.out.println((String) userField.get(user));

        Method method = User.class.getDeclaredMethod("test");

        method.invoke(user);

        unsafe.putObject(user, unsafe.objectFieldOffset(userField), "kao");


        user.test();
    }

    private static class User {
        private String name;

        private static UnsafeTest instance = new UnsafeTest();

        User() {
            System.err.println("init");
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void test() {
            System.err.println("hello,world" + name);
        }
    }
}
