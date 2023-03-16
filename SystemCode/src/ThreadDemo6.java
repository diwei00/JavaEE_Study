import javax.swing.plaf.nimbus.State;
//线程之间并发执行，抢占式调度
public class ThreadDemo6 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("aaa");
                }

            }
        }, "MyThread");//构造方法设置线程名字
        thread.setDaemon(true);//设置为后台线程
        thread.start();



        System.out.println(thread.getId());//获得线程id
        System.out.println(thread.getName());//获得线程名字
        Thread.State state =  thread.getState();//获得线程状态
        System.out.println(state);
        System.out.println(thread.getPriority());//获得线程优先级
        //前台线程，会阻止进程的结束，前台线程的工作没做完，进程是完不了的。
        //后台线程，不会阻止进程的结束，后台线程的工作没做完，进程是可以结束的。
        System.out.println(thread.isDaemon());//是否为后台线程（守护线程）只与这个方法有关
        Thread.sleep(5000);

        //run执行结束，线程销毁，pcb随之释放，但是thread不一定释放
        //线程执行就为true，（run）没开始/执行结束为false
        System.out.println(thread.isAlive());//线程是否存活

        System.out.println("aaaaaaa");


    }

}
