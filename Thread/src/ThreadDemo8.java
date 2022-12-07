//线程等待
public class ThreadDemo8 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++) {
                    System.out.println("aaa");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        Thread.sleep(5000);
        //线程等待（阻塞）
        //当主线程走到join时就会阻塞，直到thread线程执行结束，才会执行主线程（thread线程肯定比main线程先结束）
        //可设置参数为最大阻塞时间
        thread.join();
        System.out.println("bbb");
    }
}
