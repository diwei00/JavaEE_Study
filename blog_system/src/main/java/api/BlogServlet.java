package api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询数据库得到所有博客的摘要
        //转换为json字符串，发送到客户端
        BlogDao blogDao = new BlogDao();
        //尝试获取页面blogId
        String blogId = req.getParameter("blogId");
        if(blogId == null) {
            //博客列表页
            List<Blog> blogList = blogDao.selectAll();
            String respJson = objectMapper.writeValueAsString(blogList);
            resp.setContentType("application/json; charset=utf8");
            resp.getWriter().write(respJson);
        }else {
            //博客详情页
            //根据blogId查找指定博客
            Blog blog = blogDao.selectById(Integer.parseInt(blogId));
            if(blog == null) {
                System.out.println("当前blogId = " + blogId + "对应的博客不存在");
            }
            String respJson = objectMapper.writeValueAsString(blog);
            resp.setContentType("application/json; charset=utf8");
            resp.getWriter().write(respJson);

        }
    }

    //提交博客，这里保存到数据库
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取到登录用户会话
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null) {
            //用户未登录
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录，无法发布博客！");
            return;
        }
        //获取到会话中当前登录的用户
        User user = (User) httpSession.getAttribute("user");
        if(user == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录，无法发布博客！");
            return;
        }

        //确保用户已经登录
        //服务器按照这样的格式解析数据
        req.setCharacterEncoding("utf8");

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        if(title == null || "".equals(title) || content == null || "".equals(content)) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前数据提交有误，缺少标题或正文");
            return;
        }

        //构造blog对象，存入数据库
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(user.getUserId());
        //发布时间，java中生成，或者数据库中生成
        blog.setPostTime(new Timestamp(System.currentTimeMillis()));

        //插入数据库
        BlogDao blogDao = new BlogDao();
        blogDao.add(blog);

        //跳转到博客列表页
        resp.sendRedirect("blog_list.html");

    }
}
