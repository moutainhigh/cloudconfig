package test;

/**
 * Created by think on 2018/4/7.
 */
public class MyDeadLockDemo {

    public void doSomething(Integer a,Integer b){
        Integer num=Double.valueOf(Math.random()*2).intValue()+1;
        System.out.println("random  num "+num);

        if (num%2==0){

            synchronized (a){

                System.out.println("get lockA...1");

                synchronized (b){
                    System.out.println("get lockB ......................1");
                }
            }
        }else {
            synchronized (b) {
                System.out.println("get LockB...2");
                synchronized (a){
                    System.out.println("get lockA.............2");
                }
            }

        }
    }




    public static void main(String[] args) {
        MyDeadLockDemo demo=new MyDeadLockDemo();
        Integer a=1;
        Integer b=2;
        for (int i = 1; i <10 ; i=i+2) {
            new MyThread(demo,a,b).start();
        }
    }
}
