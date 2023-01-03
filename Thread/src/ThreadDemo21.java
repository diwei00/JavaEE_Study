import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//阻塞队列
//如果队列为空，出队操作就会阻塞，阻塞到另一个线程进行了入队操作（队列不为空）
//如果队列满了，入队操作就会阻塞，阻塞到另一个线程进行了出队操作（队列不为满）
//消息队列
//特殊的队列，相当于再阻塞队基础上，加上了“消息的类型”，按照指定类型进行先进先出
//消息队列已经被写作一个程序部署在一组服务器上，使用这可以通过客户端的方式发起请求
//阻塞队列特性：可以实现生产者消费者模型
//两个好处：1.实现发送方和接收方之间的解耦合 2.做到”削峰填谷“，保证系统的稳定性
//理解：两个服务器之间如果直接进行调用，一个服务器挂了，可能引起另一个服务器也挂了（耦合太高）
//为了降低耦合性，在服务器间增加阻塞队列（消息队列），一个服务器得到的请求只需要入队列即可，其他具体处理数据的服务器在队列中出队，执行即可。
//这样就算一个服务器挂了，但消息队列存在，就不会影响另一个服务器，并且服务器中的代码也只需要针对消息队列即可
//”削峰填谷“，对于大量的请求，阻塞队列可以起到一个缓冲的效果，保证系统的稳定性
public class ThreadDemo21 {
    public static void main(String[] args) throws InterruptedException {
        //阻塞队列
        //put入队列，take出队列，这两个方法带有阻塞功能
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        blockingQueue.put("aaaaaaa");
        String tmp = blockingQueue.take();
        System.out.println(tmp);
        //队列已经为空，再出队列就会阻塞等待
        //String tmp2 = blockingQueue.take();
        //System.out.println(tmp2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    blockingQueue.put("bbbbb");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String tmp = null;
                try {
                    tmp = blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(tmp);
            }
        });
        //先调度t2线程，由于为空队列，进入阻塞。t1线程启动后，进行入队操作，就打破了t2线程的阻塞状态，进行出队操作
        t2.start();
        Thread.sleep(100);
        t1.start();


    }
}
