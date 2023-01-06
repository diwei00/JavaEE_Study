import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//模拟实现线程池
//存在阻塞队列，保存任务
//存在一些线程，处理任务
//提交任务
class MyThreadPool {

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    //创建线程，处理任务
    public MyThreadPool(int n) {
        for(int i = 0; i < n; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Runnable take = queue.take();
                        take.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t.start();
        }
    }
    //提交任务
    public void submit(Runnable take) throws InterruptedException {
        queue.put(take);
    }


}
public class ThreadDemo27 {
    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool(10);
        for(int i = 0; i < 1000; i++) {
            int n = i;
            myThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(n);
                }
            });
        }
    }


}