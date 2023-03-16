import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
//使用callable创建线程任务（具有返回值）
//需要使用FutureTask进行包装
public class ThreadDemo30 {
    public static void main(String[] args) throws Exception {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for(int i = 0; i <= 100; i++) {
                    sum += i;
                }
                return sum;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread t = new Thread(futureTask);
        t.start();
        //get方法获取到结果，这里会进行阻塞，直到callable执行完毕，才能获取到结果
        Integer tmp = futureTask.get();
        System.out.println(tmp);
    }
}
