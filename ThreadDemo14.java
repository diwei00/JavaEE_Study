//多线程对同一个变量累加
class Count {
    public int count = 0;
    synchronized public void add() {
        count++;
    }
}
public class ThreadDemo14 {
    public static void main(String[] args) {
        Count count = new Count();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10000; i++) {
                    count.add();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10000; i++) {
                    count.add();
                }
            }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(count.count);
    }
}
