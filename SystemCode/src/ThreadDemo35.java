import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
//多线程下使用HashMap
public class ThreadDemo35 {
    public static void main(String[] args) {
        //多线程下线程不安全
        HashMap<Integer, Integer> map = new HashMap<>();

        //多线程下线程安全（给关键方法加锁）
        Hashtable<Integer, Integer> hashtable = new Hashtable<>();



        //多线程下线程安全（更优化的线程安全哈希表）
        //1）最大优化之处：把一把大锁改为小锁了。大大缩小了锁冲突的概率（哈希表的每个元素上加锁，锁对象为头节点）
        //如果数据哈希到同一个链表，这个时候多线程修改存在线程安全问题，如果数据在不同链表上就不存在线程安全问题。
        //在JDK1.8之前使用的是分段锁，分段式加锁，但这种方法不够彻底，锁粒度切分也不够细
        //2）针对读不加锁，针对写加锁
        //读和读之间不存在锁竞争
        //写和写之间存在锁竞争
        //读和写之间不存在锁竞争。这样就可能造成脏读问题（读的数据不够完整），因此这里的写操作设计为了：原子的写操作 + volatile
        //3）内部充分使用了CAS，进一步减少加锁的数目。比如维护元素个数
        //4）针对扩容采取了“化整为零”的方式
        //HashMap/HashTable采取了开辟一块新的数组，将原来的数据重新哈希过来（如果数据量大，就比较耗时）
        //ConcurrentHashMap如果需要扩容，采取每次搬运一小部分元素
        //新旧数组都保留
        //put需要扩容时，会开辟一个更大的数组，往新数组上增加元素，并且保留旧数组，每次搬运一部分旧数组元素。
        //get方法如果需要查找数据，新旧数组都查找
        //当所有数据搬运完成了，就释放旧数组
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
    }
}
