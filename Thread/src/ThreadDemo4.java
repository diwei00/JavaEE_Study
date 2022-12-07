//匿名内部类实现Runable实例
public class ThreadDemo4 {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("aaaa");
            }
        });
        thread.start();
    }
}
