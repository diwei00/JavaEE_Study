package api;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取当前会话
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录！");
            return;
        }
        //注销，删除会话中的user对象
        httpSession.removeAttribute("user");
        //重定向到登录界面
        resp.sendRedirect("login.html");
    }
}
