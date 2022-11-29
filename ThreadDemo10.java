//多线程测试
public class ThreadDemo10 {


    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                long a = 0;
                for(long i = 0; i < 100_0000_0000L; i++) {
                    a++;
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                long b = 0;
                for(long i = 0; i < 100_0000_0000L; i++) {
                    b++;
                }
            }
        });
        long start = System.currentTimeMillis();
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        long end = System.currentTimeMillis();
        System.out.println(end - start);


        long a = 0;
        long b = 0;
        long start2 = System.currentTimeMillis();
        for(long i = 0; i < 100_0000_0000L; i++) {
            a++;
        }
        for(long i = 0; i < 100_0000_0000L; i++) {
            b++;
        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - start2);
        //实际上thread1和thread2在执行的过程中，会经历很多次调度
        //这些调度有些是并发执行（在一个cpu核心），有些是并行执行（在两个cpu核心）
        //线程自身调度也需要时间开销







    }
}
