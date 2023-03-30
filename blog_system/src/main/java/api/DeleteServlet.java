package api;

import model.Blog;
import model.BlogDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String blogId = req.getParameter("blogId");
        if(blogId == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("缺少blogId字段");
            return;
        }
        BlogDao blogDao = new BlogDao();
        Blog blog = blogDao.selectById(Integer.parseInt(blogId));
        if(blog == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("要删除的博客不存在 blogId = " + blogId);
            return;
        }
        //确认身份，登录者只能删除自己发布的博客
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录！");
            resp.sendRedirect("login.html");
            return;
        }
        User user = (User) httpSession.getAttribute("user");
        if(user == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录！");
            resp.sendRedirect("login.html");
            return;
        }
        //判断当前登录与要删除的博客是否对应
        if(user.getUserId() != blog.getUserId()) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前只能删除自己的博客！");
            return;
        }

        //身份和博客对应，执行删除逻辑
        blogDao.delete(Integer.parseInt(blogId));
        //重定向到博客详情页
        resp.sendRedirect("blog_list.html");

    }
}
