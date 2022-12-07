//多线程实现求和
import java.util.Random;
class Add2 {
    public int sum1 = 0;
    public int sum2 = 0;
}
public class ThreadDemo13 {
    public static void main(String[] args) {
        int[] arr = new int[1000_0000];
        Random random = new Random();
        for(int i = 0; i < 1000_0000; i++) {
            arr[i] = random.nextInt(100) + 1;
        }
        Add2 add = new Add2();
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 1000_0000; i+=2) {
                    add.sum1 += arr[i];
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 1; i < 1000_0000; i++) {
                    add.sum2 += arr[i];
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(add.sum1 + add.sum2);
        long end = System.currentTimeMillis();
        System.out.println(end - start);


    }
}
