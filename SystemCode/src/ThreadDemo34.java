import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
//多线程环境下使用ArrayList
//1）自己加锁
//2）使用Collections.synchronizedList方法，它是带锁的，包裹起来Arraylist
//3）CopyOnWriteArrayList：写时拷贝
public class ThreadDemo34 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        List<Integer> tmp = Collections.synchronizedList(list);


        //如果针对ArrayList进行读操作，不做任何额外的工作
        //如果针对写操作，则拷贝一份新的ArrayList，在新的上面修改，修改过程中如果需要读数据就读旧的数据，当新的ArrayList修改完后，再和旧的替换
        //替换本质就是引用之间的修改，原子的
        //只适合数组比较小的情况下（拷贝需要时间）
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();


    }
}
