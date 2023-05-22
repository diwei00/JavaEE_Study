import com.spring.demo.component.BeanComponent;
import com.spring.demo.controller.UserController;
import com.spring.demo.controller.UserController2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        UserController userController = context.getBean("userController", UserController.class);
//        userController.printUser();
//
//        UserController2 userController2 = context.getBean("userController2", UserController2.class);
//        userController2.printUser2();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        BeanComponent beanComponent = context.getBean("beanComponent", BeanComponent.class);
        beanComponent.hello();
        context.destroy();


    }
}
