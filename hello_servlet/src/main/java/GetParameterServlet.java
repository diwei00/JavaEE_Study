import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

// 通过getParameter即可以获取QueryString中键值对，也可以获取到form表单构造的请求body中键值对

@WebServlet("/getParameter")
public class GetParameterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        StringBuilder stringBuilder = new StringBuilder();
        //获取到QueryString中所有key
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            //通过key获取val
            String val = req.getParameter(parameterName);
            stringBuilder.append(parameterName);
            stringBuilder.append(": ");
            stringBuilder.append(val);
            stringBuilder.append("    ");

        }

        //通过key获取val(QueryString)
        String studentId = req.getParameter("studentId");
        String classId = req.getParameter("classId");
        //当queryString里没有key的时候，返回null
        String tmp = req.getParameter("f");
        System.out.println(tmp);
        resp.getWriter().write("studentId" + ": " + studentId  +  "   " + "classId" + ": " + classId);
        resp.getWriter().write("<br>");
        resp.getWriter().write(stringBuilder.toString());
    }
}
