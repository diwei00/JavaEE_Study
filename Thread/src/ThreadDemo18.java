//三个线程按顺序打印abc
//t2,t3先阻塞，t1执行完，通知t2，t2执行完通知t3（需保证t2，t3先阻塞）
public class ThreadDemo18 {
    public static void main(String[] args) throws InterruptedException {
        Object o1 = new Object();
        Object o2 = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a");
                synchronized (o1) {
                    o1.notify();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o1) {
                    try {
                        o1.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("b");
                synchronized (o2) {
                    o2.notify();
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o2) {
                    try {
                        o2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("c");
            }
        });
        t2.start();
        t3.start();
        Thread.sleep(100);
        t1.start();

    }
}
