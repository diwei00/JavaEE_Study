package cn.itcast.hotel;


import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class HotelSearchTest {

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
    /**
     * 解析响应
     * @param response 响应json数据
     */
    void handleResponse(SearchResponse response) {
        // 解析响应
        // 拿到hits
        SearchHits searchHits = response.getHits();
        // 拿到total中的value
        long value = searchHits.getTotalHits().value;
        System.out.println("共搜索到了" + value + "条数据");

        // 拿到里面的hits，包含了具体的数据
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 拿到具体数据
            String json = hit.getSourceAsString();

            // 反序列化为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testMatchAll() throws IOException {
        // 准备request对象
        SearchRequest request = new SearchRequest("hotel");
        // 设置查询条件
        // request.source()就是最大的DSL中的json
        // QueryBuilders设置查询类型
        request.source().query(QueryBuilders.matchAllQuery());

        // 发送请求，进行查询
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析响应
        handleResponse(response);

    }


    @Test
    void testMatch() throws IOException {

        // 准备request对象
        SearchRequest request = new SearchRequest("hotel");
        // 构造查询条件
        request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));

        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析响应
        handleResponse(response);
    }

    /**
     * bool符合查询
     * @throws IOException
     */
    @Test
    void testBool() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        // 构造BoolQueryBuilder对象
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // must绑定term查询
        boolQuery.must(QueryBuilders.termQuery("city", "深圳"));
        // filter绑定range查询
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(100).lt(300));

        // 构造DSL语句
        request.source().query(boolQuery);

        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析响应
        handleResponse(response);

    }

    // 分页查询，结果进行排序
    @Test
    void testPageAndSort() throws IOException {

        int page = 2;
        int size = 5;
        // 准备request对象
        SearchRequest request = new SearchRequest("hotel");
        // 绑定查询条件
        request.source().query(QueryBuilders.matchAllQuery());

        // 设置分页参数
        request.source().from((page - 1) * size).size(5);
        // 设置结果排序规则
        request.source().sort("score", SortOrder.ASC);


        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析响应
        handleResponse(response);
    }

    // 查询结果高亮显示（需要替换原来非高亮字段）
    // 争对搜索字段进行高亮显示，因此搜索时需要指定字段
    // 根据DSL语法进行解析
    @Test
    void testHighlight() throws IOException {
        // 构造request对象
        SearchRequest request = new SearchRequest("hotel");
        // 构造查询类型
        request.source().query(QueryBuilders.matchQuery("all", "如家"));
        // 构造高亮字段，添加的标签，是否需要和搜索字段一致
        request.source().highlighter(new HighlightBuilder().field("name")
                .requireFieldMatch(false).preTags("<em>").postTags("</em>"));

        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析响应（需要将高亮字段与原来字段进行替换）
        handleHighlightResponse(response);
    }

    void handleHighlightResponse(SearchResponse response) {
        // 拿到外层hits
        SearchHits searchHits =  response.getHits();
        // 拿到搜索的总条数
        long value = searchHits.getTotalHits().value;
        System.out.println("共搜索到了条"+ value +"数据");

        // 拿到里层hits，包含具体数据（数组）
        SearchHit[] hits = searchHits.getHits();

        for (SearchHit hit : hits) {
            // 拿到source（具体数据）
            String json = hit.getSourceAsString();
            // 反序列化为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);

            // 拿到高亮结果（map类型）
            Map<String, HighlightField> highlightFields =  hit.getHighlightFields();
            if(!CollectionUtils.isEmpty(highlightFields)) {

                // 拿到value值，具体高亮字段内容（数组）
                HighlightField highlightField = highlightFields.get("name");
                if(highlightField != null) {
                    // 拿到数组中具体高亮字段内容
                    String name = highlightField.getFragments()[0].string();
                    // 替换掉原来非高亮字段
                    hotelDoc.setName(name);
                }
            }
            System.out.println(hotelDoc);


        }


    }




}
