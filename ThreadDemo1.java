class MyThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("aaaa");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
public class ThreadDemo1 {
    public static void main(String[] args) throws InterruptedException {//主线程
        //创建线程
        Thread thread = new MyThread();
        //启动线程
        thread.start();

        while (true) {
            System.out.println("bbbb");
            Thread.sleep(1000);
        }

    }
}
