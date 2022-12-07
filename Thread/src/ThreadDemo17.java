//wait notify
//wait
// 1.先释放锁（保证要有锁）
// 2.阻塞等待
// 3.收到通知后，尝试获取锁，并且在获取锁之后继续往下执行
//notify
//通知wait

//某个线程调用wait方法，就会进入阻塞（无论是通过哪个对象wait），此时就处在WAITING状态
public class ThreadDemo17 {
    public static void main(String[] args) throws InterruptedException {
        Object object = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1前");
                synchronized (object) {
                    try {
                        object.wait(); //不加任何参数就是死等
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t1后");
            }
        });

        //notify只能唤醒同一个对象上的等待线程
        //如果和wait对象不一致，则不生效
        //多个线程wait的时候，notify随机唤醒一个，notifyAll全部唤醒，一起竞争锁
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t2前");
                synchronized (object) {
                    object.notify();
                }
                System.out.println("t2后");
            }
        });
        t1.start();
        Thread.sleep(500);
        t2.start();
    }
}
