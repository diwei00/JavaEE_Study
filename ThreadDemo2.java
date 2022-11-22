//实现Runable接口
//好处：解耦合，让线程和要干的活分离开
class MyRunable implements Runnable {
    @Override
    public void run() {
        System.out.println("aaaa");

    }
}
public class ThreadDemo2 {
    public static void main(String[] args) {
        //runable描述了这个线程要干什么
       Runnable runnable = new MyRunable();
       //创建线程
       Thread thread = new Thread(runnable);
       thread.start();




    }
}
