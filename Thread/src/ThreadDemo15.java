//线程安全测试
class Cumsum {
    public int a = 0;
    synchronized public void add() {
        a++;
    }
}
public class ThreadDemo15 {
    public static void main(String[] args) {
        Cumsum cumsum = new Cumsum();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                     cumsum.add();
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {
                    cumsum.add();
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
        System.out.println(cumsum.a);

    }
}
