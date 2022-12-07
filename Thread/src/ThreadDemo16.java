import java.util.Scanner;
//内存可见性问题
//内存可见性问题（两个线程读取和修改同一个变量）编译器优化可能认为这个变量是不可变的（加volatile可变的）
//一个线程读取一个变量，同时另一个线程修改同一个变量，此时读到的值，不一定是修改后的值，读这个线程没有感受到变量的变化（编译器优化）
//此时就需要我们自己告诉编译器这个变量是可变的，让它每次都去读取
//每个线程都有自己的栈空间，即使同一个方法在不同线程中调用，局部变量也会处在不同的栈空间，本质是不同的变量
class Counter {
    //不能修饰局部变量
    //局部变量在不同的线程里占用不同的栈空间，意味这就是不同的变量（每个线程都有自己的栈空间）
    volatile public int flag = 0;
}
public class ThreadDemo16 {
    public static void main(String[] args) {

        Counter counter = new Counter();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter.flag == 0) {

                }
                System.out.println("aaaa");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                counter.flag = scanner.nextInt();
            }
        });
        t1.start();
        t2.start();
    }
}
