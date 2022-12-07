//lambda表达式实现函数式接口Runable（实例其函数式接口对象）
public class ThreadDemo5 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("aaa");
        });
        thread.start();
    }
}
