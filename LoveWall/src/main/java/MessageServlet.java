import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Message {
    public String from;
    public String to;
    public String message;
}
@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    //数据保存在List中
    private List<Message> messageList = new ArrayList<>();
    //List<Message> messageList = new ArrayList<>();
    //jackson核心类
    private ObjectMapper objectMapper = new ObjectMapper();

    //向服务器提交数据
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 读取body中json文件，并解析成message对象
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);
        //保存到list中
        //messageList.add(message);
        save(message);
        //返回响应
        resp.setStatus(200);

    }

    //从服务器获取数据
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 把对象转换为json字符串，写入到body中
        //objectMapper.writeValue(resp.getWriter(), messageList);
        resp.setContentType("application/json; charset=utf8");


        messageList = load();
        // 把对象转换为json字符串
        String jsonResp = objectMapper.writeValueAsString(messageList);
        // 打印日志
        System.out.println("jsonResp: " + jsonResp);
        // 写入body中
        resp.getWriter().write(jsonResp);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //删除操作
        delete();
        //返回响应
        resp.setStatus(200);
    }
    private void delete() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "delete from message order by messageId desc limit 1";
            statement = connection.prepareStatement(sql);
            //执行sql
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, null);
        }

    }

    private void save(Message message) {
        //建立连接
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "insert into message values(?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, null);
            statement.setString(2, message.from);
            statement.setString(3, message.to);
            statement.setString(4, message.message);

            //执行sql
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            DBUtil.close(connection, statement, null);
        }
    }
    private List<Message> load() {
        List<Message> messageList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "select * from message";
            statement = connection.prepareStatement(sql);
            //执行sql
            resultSet = statement.executeQuery();
            //遍历集合
            while (resultSet.next()) {
                Message message = new Message();
                message.from = resultSet.getString("from");
                message.to = resultSet.getString("to");
                message.message = resultSet.getString("message");
                messageList.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            DBUtil.close(connection, statement, resultSet);
        }
        return messageList;

    }
}
