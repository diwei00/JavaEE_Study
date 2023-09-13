package cn.itcast.hotel;


import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.impl.HotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HotelDocumentTest {

    @Autowired
    private HotelService hotelService;

    private RestHighLevelClient client;


    // 初始化对象，建立连接es
    @BeforeEach
    void setup() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.29.130:9200")
        ));
    }

    // 释放资源
    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }


    // 新增文档
    @Test
    void testIndexDocument() throws IOException {

        // 查询数据库得到数据
        Hotel hotel = hotelService.getById(628327L);
        // 转换对象为es中的对象（es的地理位置，经纬度是在一块）
        HotelDoc hotelDoc = new HotelDoc(hotel);


        // 准备request对象（包含索引库名和新增文档id）
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        // 添加请求数据，将数据转换为json类型
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);

        // 发送新增请求
        client.index(request, RequestOptions.DEFAULT);


    }


    // 获取文档
    @Test
    void testGetDocumentById() throws IOException {
        // 构造请求
        GetRequest request = new GetRequest("hotel", "628327");


        // 发送请求，得到响应
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        // 获取响应结果
        String json = response.getSourceAsString();

        // 转换json字符串为对象
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    // 删除文档
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel", "628327");

        client.delete(request, RequestOptions.DEFAULT);
    }


    // 修改文档
    // 全局修改，和新增文档一致
    // 如果id存在则全局修改，如果id不存在则为新增
    @Test
    void testUpdateDocument() throws IOException {
        // 构造请求
        UpdateRequest request = new UpdateRequest("hotel", "628327");

        // 局部修改
        // 设置局部需要修改的字段
        request.doc(
                "score", "88",
                "price", "220"
        );

        // 发送请求
        client.update(request, RequestOptions.DEFAULT);
    }


    // 批量操作文档
    @Test
    void testBulkRequest() throws IOException {
        // 查询数据库所有数据
        List<Hotel> hotelList = hotelService.list();

        // 构造批量新增文档请求，实质就是将所有需要操作的文档组织在一起，然后统一发送
        BulkRequest request = new BulkRequest();

        // 循环遍历，转换对象为HotelDoc
        for (Hotel hotel: hotelList) {
            // 转换数据库得到的对象
            HotelDoc hotelDoc = new HotelDoc(hotel);
            // 构造新增文档请求
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON)
            );
        }

        // 发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }



}
