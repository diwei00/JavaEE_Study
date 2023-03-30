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

@WebServlet("/count")
public class CountServlet extends HttpServlet {
    //用于博客列表页，查询所有博客数量
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询数据库有多少篇博客
        BlogDao blogDao = new BlogDao();
        int count = blogDao.count();
        resp.setContentType("application/json;charset=utf8");

        //构造json字符串，发送到前端
        String respJson = "{\"count\":" + "\"" + count + "\"}";
        resp.getWriter().write(respJson);

    }
   //用于博客详情页，统计数据库中和当前博客userId相等的博客数量
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            resp.getWriter().write("要查找的博客不存在 blogId = " + blogId);
            return;
        }
        int userId = blog.getUserId();
        //查询和当前博客userId匹配的作者数量
        int count = blogDao.countByUserId(userId);
        //构造json字符串，发送到前端
        resp.setContentType("application/json;charset=utf8");
        String respJson = "{\"count\":" + "\"" + count + "\"}";
        resp.getWriter().write(respJson);

    }
}
