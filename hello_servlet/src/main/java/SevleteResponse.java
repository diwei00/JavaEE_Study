import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/response")
public class SevleteResponse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("studentId");
        String classId = req.getParameter("classId");
        //设置body类型
//        resp.setContentType("text/html");
//        //设置字符集
//        resp.setCharacterEncoding("utf8");

        //同时设置body类型和字符集
        resp.setContentType("text/html; charset=utf8");
        resp.getWriter().write("studentId 学生: " + studentId + "classId: " + classId);
    }
}
