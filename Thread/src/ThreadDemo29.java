import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//
public class ThreadDemo29 {
    public static void main(String[] args) {
        //具体指定线程个数
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //只有一个线程
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        //线程数量根据任务多少动态变化
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        //延迟执行任务
        ExecutorService executorService5 = Executors.newScheduledThreadPool(10);
        //创建一个抢占式执行的线程池（任务执行顺序不确定）
        ExecutorService executorService6 = Executors.newWorkStealingPool();
        for(int i = 0; i < 1000; i++) {
            int n = i;
            executorService6.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(n);
                }
            });
        }

    }
}
