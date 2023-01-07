//模拟实现阻塞队列
//基于数组实现，循环数组
class MyBlockingQueue {
    private int[] elem;
    int head = 0;
    int tail = 0;
    int size = 0;
    public MyBlockingQueue() {
        this.elem = new int[1000];
    }
    public void put(int value) throws InterruptedException {
        //队列满了，如果再如数据就阻塞，阻塞到队列不满为止
        //虽然notify通知之后，队列就不满了，为了防止通知了队列任然是满的，这里用while循环
        //这个方法及右读又有写，多线程环境下是不安全的，那么就需要保证这些代码的原子性
        synchronized (this) {
            while (elem.length == size) {
                this.wait();
            }
            elem[tail] = value;
            tail++;
            //tail = tail % elem.length;
            if(tail >= elem.length) {
                tail = 0;
            }
            this.size++;

            //唤醒take中的wait
            this.notify();

        }
    }
    public Integer tack() throws InterruptedException {
        //如果队列空了，再出数据就阻塞，阻塞到队列不空为止
        //虽然notify通知之后，队列就不为空了，为了防止通知了队列任然是空的，这里用while循环
        int tmp = 0;
        synchronized (this) {
            while (this.size == 0) {
                this.wait();
            }
            tmp = elem[head];
            head++;
            if(head >= elem.length) {
                head = 0;
            }
            size--;
            //唤醒put中的wait
            this.notify();
        }
        return tmp;

    }
}
public class ThreadDemo22 {
    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue myBlockingQueue = new MyBlockingQueue();
        myBlockingQueue.put(1);
        myBlockingQueue.put(2);
        myBlockingQueue.put(3);
        System.out.println(myBlockingQueue.tack());
        System.out.println(myBlockingQueue.tack());
        System.out.println(myBlockingQueue.tack());
        myBlockingQueue.tack();
    }
}