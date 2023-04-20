import com.spring.demo.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// 只要这个对象多处都需要使用，那么这个对象就可以作为bean存储到spring中
public class App {
    public static void main(String[] args) {
        // 得到spring(上下文)对象   （从spring中获取对象，首先需要获取spring）
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        // spring中存在多个对象，可以根据（id , class , id class）确定某一个对象
        // 根据id获取
        // DI操作，程序运行期间动态将依赖对象注入到当前类中
        Student student = (Student) context.getBean("student");

        // 根据类型class获取（如果spring中根据class可以找到多个对象，就会抛异常）
        //Student student1 = context.getBean(Student.class);

        // 根据id + class获取（可以更加确定某一个对象，更加推荐的做法）
        Student student2 = context.getBean("student", Student.class);
        // 使用bean对象
        student2.hello();

        Student stu = context.getBean("stu", Student.class);

        // spring中将同一个对象存储多个，当程序运行的时候，会多个对象是独立的（内存都是各有各的）
        System.out.println(stu == student);
    }
}
