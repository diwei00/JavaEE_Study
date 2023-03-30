package model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//对博客数据进行操作
public class BlogDao {
    //新增博客
    public void add(Blog blog) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "insert into blog values(null, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getContent());
            //获取的是格式化日期字符串
            statement.setTimestamp(3, blog.getPostTimestamp());
            statement.setInt(4, blog.getUserId());
            //执行sql
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            DBUtil.close(connection, statement, null);
        }
    }

    //根据博客ID查询指定博客（博客详情页）
    public Blog selectById(int blogId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "select * from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);

            //执行sql
            resultSet = statement.executeQuery();

            //主键，只会查找到一个元素
            if(resultSet.next()) {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                return blog;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    //直接查询数据库中所有博客数据（博客列表页）
    public List<Blog> selectAll() {
        List<Blog> blogList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "select * from blog order by postTime desc";
            statement = connection.prepareStatement(sql);
            //执行sql
            resultSet = statement.executeQuery();

            //遍历结果
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                //提取content的摘要
                String content = resultSet.getString("content");
                if(content.length() >= 100) {
                    content = content.substring(0, 100) + "...";
                }
                blog.setContent(content);
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                blogList.add(blog);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return blogList;
    }


    //根据ID删除指定博客
    public void delete(int blogId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "delete from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);

            //执行sql
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, null);
        }
    }
    public int count() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "select count(blogId) from blog";
            statement = connection.prepareStatement(sql);
            //执行sql
            resultSet = statement.executeQuery();
            int count = 0;
            //只有一条记录
            if(resultSet.next()) {
                count = resultSet.getInt("count(blogId)");
            }
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return -1;
    }
    public int countByUserId(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql
            String sql = "select count(userId) from blog where userId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            //执行sql
            resultSet = statement.executeQuery();
            //只有一条记录
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt("count(userId)");
            }
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return -1;

    }

}
