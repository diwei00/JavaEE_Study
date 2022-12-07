//线程状态
public class ThreadDemo9 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("aaa");
                }
            }
        });
        //NEW
        //有线程对象，但是没有启动线程
        System.out.println(thread.getState());
        thread.start();//一个线程只能start一次
        //RUNNABLE
        //可执行的，1.就绪队列 2.正在cpu上执行的
        for(int i = 0; i < 10; i++) {
            System.out.println(thread.getState());
        }
        //TIMED_WAITING
        //当线程处于sleep时，这个时间获取线程状态
        //BLOCKED
        //等待锁产生的最阻塞
        //WAIT
        //等待其他人来通知
        //(这三种状态都是在描述不同的阻塞状态)
        Thread.sleep(8000);
        //TERMINATED
        //线程执行结束，pcb已经释放，但对象还在
        System.out.println(thread.getState());
    }
}
