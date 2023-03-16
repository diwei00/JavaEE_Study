//三个线程按自己名称打印cba
//使用join方法，让ba线程先阻塞，当c线程执行完，执行b线程，b线程执行完，执行a线程
//当遇到join方法线程就会阻塞，直到该线程执行完，才会进入就绪队列
public class ThreadDemo20 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print(Thread.currentThread().getName() + " ");

            }
        }, "c");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(Thread.currentThread().getName() + " ");

            }
        }, "b");
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
            }
        }, "a");
        t1.start();
        t2.start();
        t3.start();

    }
}
