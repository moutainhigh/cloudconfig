package test;

/**
 * Created by think on 2018/4/7.
 */
public class MyThread extends  Thread{

    MyDeadLockDemo myDeadLockDemo;
    Integer a;
    Integer b;

    public MyThread(  MyDeadLockDemo myDeadLockDemo, Integer a, Integer b) {
         this.myDeadLockDemo = myDeadLockDemo;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {

         myDeadLockDemo.doSomething(a,b);
    }
}
