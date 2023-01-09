import java.util.concurrent.locks.ReentrantLock;
//使用ReentrantLock加锁
//为了保证unlock方法一定被执行，一般把他包裹在finally中
//ReentrantLock提供了公平锁的实现，构造方法里写true，不写或者写false都是不公平锁
//synchronized产生阻塞就是死等，ReentrantLock提供了更加灵活的等待方式：tryLock方法，返回值为boolean可以判断是否加锁
// 1）无参数版本，能枷锁就加不能就放弃  2）有参数版本，指定最大等待时间，超时就放弃
//synchronized加锁，使用notify随机唤醒一个线程。ReentrantLock搭配一个Condition类，可以唤醒指定线程
public class ThreadDemo31 {
    volatile private static int sum = 0;
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    public static void func() {
        boolean tmp = false;
        try {
            reentrantLock.lock();
            for(int i = 0; i <= 100; i++) {
                sum += i;
            }
        }finally {
            reentrantLock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                func();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                func();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(sum);

    }
}
