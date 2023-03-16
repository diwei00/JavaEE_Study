//synchronized加锁
//进入方法加锁，出方法就会解锁
//如果两个线程尝试同时加锁，只有一个线程会加锁成功，另一个线程会阻塞等待（BLOCKED），直到解锁后，才可能加锁成功（阻塞队列进入就绪队列）。
//操作系统中的锁“不可剥夺性”，一旦一个线程获取到锁，除非它主动释放，不然无法强占。
//加锁会把并发改为串行
//synchronized会一直等待阻塞下去
//锁虽然在方法上修饰，但其实是通过线程把锁加到对象上
//两个线程，一个线程不加锁，另一个线程加锁，不会产出锁重突
//代码块加锁，可以显示指定锁对象
//可重入：一个线程针对同一个对象连续加两次锁，如果允许这样的操作，就是可重入的，如果不允许就是不可重入的（死锁）synchronized为可重入锁
class Add {
    public static int a = 0;
    synchronized public  void add() {
        a++;
    }
    synchronized public void add2() {
        synchronized (this) {
           a++;
       }
    }
}
public class ThreadDemo11 {

    public static void main(String[] args) throws InterruptedException {
        Add add2 = new Add();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                    add2.add();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                    add2.add2();
                }
            }
        });
        //执行一次a++需要，load,add,save(都内存数据到寄存器，寄存器值++, 寄存器值写回内存)
        //由于两个线程是并发执行的，这些指令会随机组合（抢占式执行，随意调度），就会产生线程安全问题（不同的顺序，结果就会产生差异）
        //第二个线程读取的值是在第一个线程保存后读取的1，就会加2次（线程安全）
        //两次读取的值都为0，则最终只加1. 在一次线程切换中，另一个线程可能会执行多次三步流程（线程不安全）
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(Add.a);
    }
}
