import com.spring.demo.controller.*;
import com.spring.demo.entity.User;
import com.spring.demo.service.Teacher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    // @Controller：控制器，验证用户请求数据的正确性（安保系统）。直接和前端打交到，验证前端发来请求是否正确
    // @Service：服务，编排和调度具体执行方法的（客服中心）。不会直接操作数据库，根据请求判断具体调用哪个方法
    // @Repository：数据持久层，直接和数据库交互（执行者）（DAO层 data access object）
    // @Component：组件（工具类）。为整个项目存放一些需要使用的组件，放在其他位置不是很合适
    // @Configuration：配置项（项目中的一些配置）。当项目启动时，如果遇到哪个类被这个注解修饰，那么就会加载其中的一些配置
    // 上述五大类注解，xml配置的路径中只要能查找到（递归查询），其所修饰的类都会作为bean存储到spring中（类名需不同）
    // 五大类注解存在不同含义，当我们看到某一个注解就可以明确这个了类是做什么的。
    // 除了Component以外的其他四大类注解，都有Component注解的功能，也就是它的扩展。
    public static void main1(String[] args) {
        // 获取到spring上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        StudentController studentController = context.getBean("studentController", StudentController.class);
        studentController.hello();

        StudentService studentService = context.getBean("studentService", StudentService.class);
        studentService.hello();

        StudentRepository studentRepository = context.getBean("studentRepository", StudentRepository.class);
        studentRepository.hello();

        StudentComponent studentComponent = context.getBean("studentComponent", StudentComponent.class);
        studentComponent.hello();

        StudentConfiguration studentConfiguration = context.getBean("studentConfiguration", StudentConfiguration.class);
        studentConfiguration.hello();

        SController sController = context.getBean("SController", SController.class);
        sController.hello();

        // Student没有在指定路径下的子目录中
//        Student student = context.getBean("student", Student.class);
//        student.hello();



        // 如果指定路径下有同名类，写起来很麻烦，不建议这样写
//        com.spring.demo.controller.StudentController studentController1 = context.getBean("StudentController2", com.spring.demo.controller.StudentController.class);
//        studentController1.hello();

        Teacher teacher = context.getBean("teacher", Teacher.class);
        teacher.hello();

        // 使用方法注解
        User user = context.getBean("user6", User.class);
        System.out.println(user.getUsername());

        // UserController在指定目录中的子目录中
        UserController2 userController2 = context.getBean("userController2", UserController2.class);
        userController2.hello();
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        UserController3 userController3 = context.getBean("userController3", UserController3.class);
        userController3.hello();


    }
}
