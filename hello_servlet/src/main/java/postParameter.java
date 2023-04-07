import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

class Student {
     public int studentId;
     public int classId;
     public int age;
}
@WebServlet("/postParameter2")
public class postParameter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        //获取body的长度
//        int len = req.getContentLength();
//        byte[] buffer = new byte[len];
//        //读取请求body中的body
//        InputStream inputStream = req.getInputStream();
//        inputStream.read(buffer);
//
//        String body = new String(buffer, 0, len);
//        System.out.println("body" + body);
//        //写回客户端
//        resp.getWriter().write(body);


        //使用jackson解析json文件



        // 执行原理：
        // 1）从body中读取json字符串
        // 2）根据第二个参数类对象，创建实例
        // 3）解析json文件，处理成map键值对结构
        // 4）遍历所有键值对，看键的名字和Student实例的哪个属性匹配，就把对应的value设置到该属性中
        // 5）返回该Student实例

        //如果student属性少于json文件中的键值对，服务器直接抛异常
        //如果student属性多于json文件中的键值对，则有多少构造多少

        // 使用jackson的核心类
        ObjectMapper objectMapper = new ObjectMapper();

        Student student = objectMapper.readValue(req.getInputStream(), Student.class);
        System.out.println(student.studentId + " " + student.classId);
        resp.getWriter().write(student.studentId + " " + student.classId);


    }
}
