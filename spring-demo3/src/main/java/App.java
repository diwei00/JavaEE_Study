import com.spring.demo.controller.Controller2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Controller2 controller2 = context.getBean("controller2", Controller2.class);
        controller2.hello();


    }
}
