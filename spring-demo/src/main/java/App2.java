import com.spring.demo.Teacher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class App2 {
    public static void main(String[] args) {
        // 当获取到spring上下文对象的时候就会加载spring中所有的对象到内存中
        // 类似于饿汉模式，加载对象比较急切
        // 将xml中所有bean存储到spring对象中
        // 特点：比较费内存，一次性加载，之后的读取就会非常快
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");


        // 获取bean对象时才会加载获取的这个具体对象到内存中
        // 类似于懒汉模式，加载bean对象没有那么急切
        // 只有获取bean对象时，才会加对应bean到spring中
        // 特点：节省内存，调用时才会加载具体的bean，对spring中效率不高
        // 早期使用的方法，如今ApplicationContext为BeanFactory的子类，功能更加强大
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-config.xml"));
        Teacher teacher = beanFactory.getBean("teacher", Teacher.class);

    }
}
