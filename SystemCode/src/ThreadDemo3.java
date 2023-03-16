public class ThreadDemo3 {
    public static void main(String[] args) {
        //使用匿名内部类
        //匿名内部类为Thread的子类
        //创建了子类的实例，让thread引用
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("aaaa");
            }
        };
        thread.start();
    }
}
