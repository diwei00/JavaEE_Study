import java.util.concurrent.CountDownLatch;
//CountDownLatch的使用
public class ThreadDemo33 {
    public static void main(String[] args) throws InterruptedException {
        //具体有10个任务
        CountDownLatch latch = new CountDownLatch(10);
        for(int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //任务执行完成，调用countDown方法，表示任务执行完成
                    latch.countDown();
                }
            });
            t.start();
        }

        //阻塞到10个任务全部执行完成，第10次调用countDown方法才起作用
        latch.await();
        System.out.println("Aaaaaa");
        //使用场景跑步比赛
        //等待所有人跑完才算结束
    }
}
