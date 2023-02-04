import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
//模拟实现定时器

class MyTask implements Comparable<MyTask> {
    private long time;
    private Runnable runnable;

    public MyTask(Runnable runnable, long time) {
        this.runnable = runnable;
        this.time = time;
    }
    public long getTime() {
        return time;
    }
    public void run() {
        runnable.run();
    }
    @Override
    public int compareTo(MyTask o) {
        return (int) (this.time - o.time);
    }
}
class MyTimer {
    //检测线程
    private Thread t = null;
    //存储任务
    private PriorityBlockingQueue<MyTask> priorityBlockingQueue = new PriorityBlockingQueue<>();
    public MyTimer() {
        t = new Thread(()-> {
                while (true) {
                    try {
                        //当代码执行到currentTime这一行，如果被调度走，再执行schedule改变了堆顶元素，即等待时间就变了，但这是没有wait那么notify就
                        //空打了一炮，当线程调度回来时，已经落后于时间了，即错过了依次一次schedule
                        //所以就需要保证原子性，让这个线程只有wait了，才执行notify刷新等待时间，不能空打一炮。那么就增加锁的范围
                        synchronized (this) {
                            //获取最小时间任务
                            MyTask myTask = priorityBlockingQueue.take();
                            //当前时间
                            long currentTime = System.currentTimeMillis();
                            //如果一直取的时间都小于，那么这个线程就会一直取，放。那么就需要wait等待一下
                            //等待的最大时间：currentTime - tmp.getTime()
                            if (currentTime < myTask.getTime()) {
                                priorityBlockingQueue.put(myTask);
                                this.wait(myTask.getTime() - currentTime);
                            } else {
                                myTask.run();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        });
        t.start();
    }
    public void schedule(Runnable runnable, long time) {
        //时间转换
        MyTask myTask = new MyTask(runnable, System.currentTimeMillis() + time);
        priorityBlockingQueue.put(myTask);
        //每入一个任务，就需要跟新一下wait的等待时间
        synchronized (this) {
            this.notify();
        }

    }
}

public class ThreadDemo24 {
    public static void main(String[] args) {
        MyTimer myTimer = new MyTimer();
        myTimer.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("aaaaa");
            }
        }, 3000);
        myTimer.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("bbbbb");
            }
        }, 2000);
        myTimer.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("ccccc");
            }
        }, 1000);
    }
}
