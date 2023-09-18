package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
    @Autowired
    public RestHighLevelClient client;

    /**
     * 实现搜索加分页功能
     * 添加距离排序
     * @param requestParams
     * @return
     */
    @Override
    public PageResult search(RequestParams requestParams) {
        SearchRequest request = new SearchRequest("hotel");

        // 构造bool查询，复合查询
        buildBasicQuery(requestParams, request);

        // 分页
        int page = requestParams.getPage();
        int size = requestParams.getSize();
        request.source().from((page - 1) * size).size(size);

        // 距离排序
        String location = requestParams.getLocation();
        location = "31.034661, 21.612282";

        if(location != null && !location.equals("")) {
            request.source().sort(SortBuilders
                    .geoDistanceSort("location", new GeoPoint(location))
                    .order(SortOrder.ASC)
                    .unit(DistanceUnit.KILOMETERS)
            );
        }


        try {
            SearchResponse response =  client.search(request, RequestOptions.DEFAULT);
            // 解析响应
            return handleResponse(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    // 构造查询source
    private void buildBasicQuery(RequestParams requestParams, SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 全文搜索
        // 用户输入则按照用户输入查询
        // 用户未输入则搜索所有
        // 用户输入搜索数据
        // 全文搜索需要算分
        String key =  requestParams.getKey();
        if(key != null && !key.equals("")) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", key));
        }else {
            // 用户未输入（查询所有）
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }

        // 筛选条年，不需要算分
        // 城市条件（term查询）
        if(requestParams.getCity() != null && !requestParams.getCity().equals("")) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("city", requestParams.getCity()));
        }
        // 品牌条件
        if(requestParams.getBrand() != null && !requestParams.getBrand().equals("")) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", requestParams.getBrand()));
        }
        // 星际条件
        if(requestParams.getStarName() != null && !requestParams.getStarName().equals("")) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("startName", requestParams.getStarName()));
        }
        // 价格排序（range查询）
        if(requestParams.getMinPrice() != null && requestParams.getMaxPrice() != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price")
                    .gte(requestParams.getMinPrice())
                    .lte(requestParams.getMaxPrice()));
        }

        // 放入source
        request.source().query(boolQueryBuilder);
    }

    private PageResult handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 得到搜索到的总条数
        long total = searchHits.getTotalHits().value;

        // 得到实际数据数组
        SearchHit[] hits = searchHits.getHits();
        // 构造返回数据
        List<HotelDoc> hotelDocs = new ArrayList<>();
        for(SearchHit hit : hits) {
            // 拿到具体数据
            String json = hit.getSourceAsString();
            // 反序列化为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 获取排序值(就是按照距离排序的值)
            Object[] sortValues = hit.getSortValues();
            if(sortValues.length > 0) {
                Object sortValue = sortValues[0];
                hotelDoc.setDistance(sortValue);
            }

            // 放入集合
            hotelDocs.add(hotelDoc);
        }
        // 封装返回
        return new PageResult(total, hotelDocs);


    }
}
