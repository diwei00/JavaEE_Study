import java.util.concurrent.Semaphore;
//信号量为可用资源的个数
//如果信号量为0，继续申请可用资源就阻塞等待（信号量不能为负）
//锁可以视为计数器为1的信号量，二元信号量
public class ThreadDemo32 {
    public static void main(String[] args) throws InterruptedException {
        //初始化信号量为3
        Semaphore semaphore = new Semaphore(3);
        //申请信号量（个数 -1），可以指定参数一次就申请多个
        semaphore.acquire();
        semaphore.acquire();
        semaphore.acquire();
        semaphore.release();
        semaphore.acquire();
        //释放信号量（个数 +1），可以指定参数一次就释放多个
        //semaphore.release();

    }
}
