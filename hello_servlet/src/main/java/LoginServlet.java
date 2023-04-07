import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        //判断登录
        if(!userName.equals("zhansan") && !userName.equals("lisi")) {
            System.out.println("登录失败，用户名错误");
            resp.sendRedirect("login.html");
            return;
        }
        if(!password.equals("123")) {
            System.out.println("密码错误");
            resp.sendRedirect("login.html");
            return;
        }
        //登录成功
        //创建会话
        //所谓会话是一个键值对，key是sessionId，value是HttpSession对象
        //每个客户端登录都会有一个这样的会话（键值对），服务器需要管理多个会话，搞个hash表存储
        //getSession(true)判断当前请求是否已经有对应的会话了（拿着cookie中的sessionId查一下hash表）
        //如果SessionId不存在，就创建新会话，插入hash表，如果查到了就返回对应会话
        //创建过程：1）构造HttpSession对象 2）构造一个唯一sessionId 3）把这个键值对插入hsah表 4）把sessionId设置到响应报文Set-Cookie字段中
        HttpSession session = req.getSession(true);

        //HttpSession对象也是一个键值对
        //setAttribute()，getAttribute()存取键值对
        //把当前用户名保存到会话中
        session.setAttribute("username", userName);
        //重定向到主页
        resp.sendRedirect("index");
    }
}
