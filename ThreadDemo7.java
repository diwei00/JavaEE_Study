//线程终止，只是通知线程需要终止，但具体还要看线程中代码如何写
public class ThreadDemo7 {

    private static boolean flag = true;
    public static void main1(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置标志位，其他线程只要改变标志位，就可以结束run方法，结束本线程
                while (flag) {
                    System.out.println("aaaa");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();

        Thread.sleep(3000);
        flag = false;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //可以获得当前线程的引用
                //isInterrupted()线程是否中断，默认为false
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //也可等待一会再中断线程
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        //跑完异常可以主动中断线程
                        break;
                    }
                    System.out.println("aaaaa");
                }
            }
        });
        thread.start();

        Thread.sleep(3000);
        //当设置另一个线程标志位时（会设置为true），如果这个线程正处于休眠状态，sleep就会抛异常，并且清空标志位（改回标志位为false）
        //sleep清空标志位的原因：当触发sleep唤醒线程后，这个线程的状态就交给我们自己了吗，类似于代码“暂停”的做法，都会清空标志位（wait，join）
        thread.interrupt();//设置标志位，告诉线程该中断了


    }
}
