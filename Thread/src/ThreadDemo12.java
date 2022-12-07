//死锁问题
//循环阻塞，你等我，我等你
//解决死锁问题，打破循环阻塞问题
//给锁编号，按照一定的顺序去获取锁（由小到大，或者由大到小）
//任意线程加多把锁的时候，只要遵守这样的顺序，此时循环等待就会破除
public class ThreadDemo12 {
    public static void main(String[] args) {
        Object o1 = new Object();
        Object o2 = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (o2) {
                        System.out.println("aaaa");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (o2) {
                        System.out.println("bbbbb");
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}
