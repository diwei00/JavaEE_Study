import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
     servlet生命周期：
     1）开始执行的时候，创建HttpServlet实例（执行init()）
     2）执行过程中，收到请求，通过service调用doGet（）
     3）销毁之前，执行destroy
 */

@WebServlet("/hello2")
public class HelloServlet2 extends HttpServlet {
    @Override
    public void init() throws ServletException {
        //Tomcat首次收到和该类HttpServlet相关的时候（浏览器输入/hello2），就会创建这个HttpServlet对象，同时调用init方法。（只调用一次）
        //只需要实例一次，后续只需要使用这个实例即可
        System.out.println("init");
    }

    @Override
    public void destroy() {
        //当服务器不使用的时候，就需要销毁HttpServlet对象，就会执行这个方法（只执行一次）
        //destroy执行的时机不稳定
        //点击smart Tomcat停止按钮，通过8005端口，主动停止（会执行）
        //直接傻进行，就来不及执行
        System.out.println("destroy");
    }


    //Tomcat收到请求后，就会通过service调用doGet方法
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("aaa");
    }
}
