import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/redirect")
public class Redirect extends HelloServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置重定向
        // 涉及两次交互
        // 先发起一次请求，服务器返回需要重定向的url（body中location字段），第二次发起请求就是新url了
        //resp.sendRedirect("https://www.baidu.com");

        // 直接设置状态码，和location字段
        resp.setStatus(302);
        // 如果name存在，覆盖旧的值
        resp.setHeader("location", "https://www.baidu.com");


        // 如果name存在，不覆盖旧的值，并添加新的键值对（header中key可以重复）
        //resp.addHeader();
    }
}
