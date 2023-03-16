//三个线程按顺序打印abc，打印10次
//先程2，3先阻塞，执行线程1，打印完后，通知线程2启动，线程1阻塞。线程2打印完后，通知线程3启动。线程2阻塞。线程3打印完后，通知线程1启动，线程3阻塞。依次各循环10次
public class ThreadDemo19 {
    public static void main(String[] args) throws InterruptedException {
        Object tmp = new Object();
        Object tmp2 = new Object();
        Object tmp3 = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    System.out.print("A ");
                    synchronized (tmp) {
                        tmp.notify();
                    }
                    synchronized (tmp3) {
                        try {
                            tmp3.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    synchronized (tmp) {
                        try {
                            tmp.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.print("B ");
                        synchronized (tmp2) {
                            tmp2.notify();
                        }
                    }
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    synchronized (tmp2) {
                        try {
                            tmp2.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("C");
                        synchronized (tmp3) {
                            tmp3.notify();
                        }

                    }
                }

            }
        });

        t2.start();
        t3.start();
        Thread.sleep(100);
        t1.start();


    }
}
