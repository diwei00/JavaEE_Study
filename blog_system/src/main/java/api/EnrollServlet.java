package api;

import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/enroll")
public class EnrollServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //前端对数据进行判断，保证传过来数据是有效的
        req.setCharacterEncoding("utf8");
        String username = req.getParameter("username");
        String passBegin = req.getParameter("passBegin");
        String passEnd = req.getParameter("passEnd");
        if(username == null || "".equals(username) || passBegin == null || "".equals(passBegin) || passEnd == null || "".equals(passEnd)) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("输入未完成！");
            return;
        }
        if(!passBegin.equals(passEnd)) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("两次输入密码不一致！");
            return;
        }
        //构造user对象，存入数据库
        User user = new User();
        user.setUserName(username);
        user.setPassword(passBegin);
        //存入数据库
        UserDao userDao = new UserDao();
        userDao.add(user);

        //注册成功，重定向到登录页
        resp.sendRedirect("login.html");

    }
}
