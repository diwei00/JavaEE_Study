import java.util.Timer;
import java.util.TimerTask;

//定时器
//使用标准库中的定时器
public class ThreadDemo23 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("11111");
            }
        }, 3000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("22222");
            }
        }, 2000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("33333");
            }
        }, 1000);
    }
}
