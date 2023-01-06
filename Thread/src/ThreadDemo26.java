import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//这里直接使用方法获取的实例，成为“工厂方法”，这个类称为“工厂类”，这是一种设计模式“工厂模式”
//工厂模式：使用普通方法代替构造方法，实例对象。可以弥补构造方法不能实现的功能。比如构造方法相同参数代表意义不同，无法构成构造方法重载，就可以使用普通方法来创建是实例
//
public class ThreadDemo26 {
    public static void main(String[] args) {
        //线程池里的线程都是前台线程，阻止进程的结束
        //创建线程池，指定线程数量为10个
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //这里涉及变量捕获，变量的生命周期不一样，就存在变量捕获
        //变量捕获的前提需要这个变量没有改变
        //变量捕获先把这个变量存起来，使用时再创建这个变量，把值赋过去
        for(int i = 0; i < 1000; i++) {
            int n = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(n);
                }
            });
        }
    }
}
