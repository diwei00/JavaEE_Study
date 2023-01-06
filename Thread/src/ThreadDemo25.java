import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//基于阻塞队列实现生产者消费者模型
public class ThreadDemo25 {
    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        //生产者
        Thread t1 = new Thread(() -> {
            int count = 0;
            while (true) {
                System.out.println("生产：" + count);
                try {
                    blockingQueue.put(count);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count++;
            }
        });
        //消费者
        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    Integer tmp = blockingQueue.take();
                    System.out.println("消费：" + tmp);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
    }
}
