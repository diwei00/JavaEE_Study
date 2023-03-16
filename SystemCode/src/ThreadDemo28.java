import java.util.concurrent.atomic.AtomicInteger;
//标准库中原子类使用
public class ThreadDemo28 {
    public static void main(String[] args) throws InterruptedException {
        //原子类，基于CAS实现的原子类，提供了自增自减等操作。多线程下就是线程安全的
        AtomicInteger count = new AtomicInteger(0);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                    count.getAndIncrement(); //count++
                    //count.incrementAndGet(); //++count
                    //count.getAndDecrement(); //count--
                    //count.decrementAndGet(); //--count
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                    count.getAndIncrement();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count.get());
    }
}