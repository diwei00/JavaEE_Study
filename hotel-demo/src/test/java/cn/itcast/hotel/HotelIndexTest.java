package cn.itcast.hotel;

import cn.itcast.hotel.global.GlobalVariable;
import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.impl.HotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
public class HotelIndexTest {


    private RestHighLevelClient client;

    // 在每个测试方法执行之前执行一次，用于初始化client对象
    // 初始化client对象，连接es
    @BeforeEach
    void setup() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.29.130:9200")
        ));
    }

    // 在每个方法执行之后执行一次，用于释放连接
    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

   @Test
   void testInit() {
       System.out.println(client + "aaaaa1");
   }


    // 创建索引库
    @Test
    void testCreateHotelIndex() throws IOException {

        // 创建request对象（指定索引库名称）
        CreateIndexRequest request = new CreateIndexRequest("hotel");

        // 设置请求参数，dsl json参数的mapping限制索引库
        request.source(GlobalVariable.dslCreateIndexArgument,XContentType.JSON);

        // 发起请求
        // RequestOptions请求参数，一般使用默认（可以设置请求的一些信息）
        // indices()方法可得到所有操作所应库方法
        client.indices().create(request, RequestOptions.DEFAULT);

    }


    // 删除索引库
    @Test
    void testDeleteHotelIndex() throws IOException {

        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }


    // 判断索引库是否存在
    @Test
    void testExistsHotelIndex() throws IOException {

        GetIndexRequest request = new GetIndexRequest("hotel");
        boolean result = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(result);
    }






}
