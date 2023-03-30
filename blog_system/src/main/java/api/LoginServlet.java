package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //实现登录功能
        //设置请求编码，告诉servlet按照怎样的格式解析
        req.setCharacterEncoding("utf8");
        //设置响应类型，告诉客户端按照怎样格式解析
        resp.setContentType("application/json;charset=utf8");
        //获取到username 和  password
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //判断
        if(username == null || "".equals(username) || password == null || "".equals(password)) {
            //登录失败
            String respJson = "{\"message\":\"登录失败！缺少username 或 password字段\"}";
            resp.getWriter().write(respJson);
            return;
        }
        //查看数据库，判断密码和用户名是否匹配
        UserDao userDao = new UserDao();
        User user = userDao.selectByUsername(username);
        if(user == null) {
            //用户名错误
            String respJson = "{\"message\":\"登录失败! 用户名或密码错误\"}";
            resp.getWriter().write(respJson);
            return;
        }
        if(!user.getPassword().equals(password)) {
            //密码错误
            String respJson = "{\"message\":\"登录失败! 用户名或密码错误\"}";
            resp.getWriter().write(respJson);
            return;
        }
        //用户验证成功，创建会话，保存当前用户信息
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        //重定向到博客列表页
        resp.sendRedirect("blog_list.html");
    }

    //用户判断用户是否登录
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        HttpSession session = req.getSession(false);
        if(session == null) {
            //用户未登录。返回空的user对象
            User user = new User();
            String respJson = objectMapper.writeValueAsString(user);
            resp.getWriter().write(respJson);
            return;
        }
        User user = (User) session.getAttribute("user");
        if(user == null) {
            user = new User();
            String respJson = objectMapper.writeValueAsString(user);
            resp.getWriter().write(respJson);
            return;
        }
        //获取到user对象，直接返回
        String respJson = objectMapper.writeValueAsString(user);
        resp.getWriter().write(respJson);
    }
}
