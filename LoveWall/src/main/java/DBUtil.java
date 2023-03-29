import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    //采取向上转型
    public static DataSource dataSource = new MysqlDataSource();

    //设置数据源
    static {
        ((MysqlDataSource) dataSource).setURL("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource) dataSource).setUser("root");
        ((MysqlDataSource) dataSource).setPassword("wuhao1126");

    }


    //封装数据库连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    //封装释放资源
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        //三个方法分开写好，防止一个抛异常导致其他资源无法释放
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }





}
