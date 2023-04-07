import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    //通过重定向，浏览器发送get请求

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //先判断用户是否登录，如果没登陆重定向到登录页面
        //已经登录，根据会话中的用户名，显示到页面中
        //这个操作不会触发会话的创建，根据sessionId查找HttpSession对象（根据key查找value）
        HttpSession session = req.getSession(false);
        if(session == null) {
            System.out.println("用户未登录");
            resp.sendRedirect("login.html");
            return;
        }
        //登录成功
        String userName = (String) session.getAttribute("username");
        //构造页面
        resp.setContentType("text/html; charset=utf8");
        resp.getWriter().write("欢迎" + userName + "回来");
        //只要登录成功，后续请求都会带上刚才这个cookie（包含sessionId）
    }
}
