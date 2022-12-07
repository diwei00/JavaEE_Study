//单例模式（懒汉模式）
//在实际使用时创建对象，没有饿汉模式那么急切（类加载时）
//多线程模式下：
//由于方法中有写也有读，由于线程调度的随机性，可能两个线程都会读取到null值（一个线程读取完被调度走），这样就会实例两次对象，即线程不安全
//加锁是有开销的，对象一旦new出来，下一次线程只需要读取内存即可，不同在加锁
//内存可见性问题：
// 只有第一次才真正读取了内存，后续都是读取寄存器/cache（由于在一个线程里涉及读和写）
//指令重排序问题，new SingletonLazy();涉及三部操作：1.申请内存空间 2.调用构造方法，内存空间初始化一个合理的对象 3.返回这块内存的地址
//多线程情况下，指令重排序执行1，3，2，就会出问题，先执行1，3就被调度走，这块内存就是非法的（这块内存对象还没有构造好）volatile可以解决指令重排序问题（禁止）
public class SingletonLazy {
    private volatile static SingletonLazy singletonLazy = null;
    //在实际调用方法时实例化对象
    public static SingletonLazy getInstance() {
        if(singletonLazy == null) {
            synchronized (SingletonLazy.class) {
                if(singletonLazy == null) {
                    singletonLazy = new SingletonLazy();//指令重排序
                }
            }
        }

        return singletonLazy;
    }
    private SingletonLazy() {};

    public static void main(String[] args) {
        SingletonLazy s1 = SingletonLazy.getInstance();
        SingletonLazy s2 = SingletonLazy.getInstance();
        System.out.println(s1 == s2);
        System.out.println("aaaa");
    }

}
