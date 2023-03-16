//单例模式（特定的类只能创建一个实例，不能创建多个实例）通过语法实现
//饿汉模式（在类加载的时候就创建好对象）
//多线程模式下，由于方法中只涉及读取内存，没有修改，天然就是线程安全的
public class Singleton {
    //static修饰属于类属性，在类加载（解析class文件）时就创建好了，只有唯一一份，同时也就只有这一个对象
    private static Singleton singleton = new Singleton();

    //只能通过这个方法获取实例
    public static Singleton getInstance() {
        return singleton;
    }

    //将构造方法设为私有
    private Singleton() {};

    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }

}
