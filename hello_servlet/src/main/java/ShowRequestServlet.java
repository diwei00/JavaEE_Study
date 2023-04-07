import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet("/showRequest")
public class ShowRequestServlet extends HelloServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应的数据格式，浏览器就会解析
        resp.setContentType("text/html");

        StringBuilder stringBuilder = new StringBuilder();
        //获取协议名称及版本号（首行）
        stringBuilder.append(req.getProtocol());
        stringBuilder.append("<br>");
        //获取方法名
        stringBuilder.append(req.getMethod());
        stringBuilder.append("<br>");
        //获取url
        stringBuilder.append(req.getRequestURI());
        stringBuilder.append("<br>");
        //获取ContextPath
        stringBuilder.append(req.getContextPath());
        stringBuilder.append("<br>");
        //获取到QueryString
        stringBuilder.append(req.getQueryString());
        stringBuilder.append("<br>");

        //获取所有请求头名
        Enumeration<String> headerNames = req.getHeaderNames();
        //迭代器遍历
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            //根据key获得val
            stringBuilder.append(headName + ": " + req.getHeader(headName));
            stringBuilder.append("<br>");
        }

        //写回浏览器
        resp.getWriter().write(stringBuilder.toString());


    }
}
