package com.qizy.service.impl;


import com.alibaba.fastjson.JSON;
import com.qizy.common.PageInfo;
import com.qizy.common.Scroll;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EsCommonService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsCommonServiceImpl implements EsCommonService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public <T> List<T> boolQuery(String indexName, BoolQueryBuilder boolQueryBuilder, Class<T> clazz) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        System.out.println(sourceBuilder.toString());

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            T doc = JSON.parseObject(searchHit.getSourceAsString(), clazz);
            list.add(doc);
        }
        return list;

    }

    @Override
    public <T> PageInfo<T> pageBoolQuery(String indexName, BoolQueryBuilder boolQueryBuilder, Class<T> clazz,
                                         Integer page, Integer size) throws IOException,RuntimeException {
        if (page < 1) {
            throw new RuntimeException("分页参数错误");
        }
        int sum = page * size;
        if (sum > 10000) {
            throw new RuntimeException("es 默认不能超过10000");
        }
        // 计算 from
        int from = (page - 1) * size;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);

        sourceBuilder.from(from);
        sourceBuilder.size(size);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        int total = (int) searchResponse.getHits().getTotalHits().value;
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<T> list = new ArrayList<T>();
        for (SearchHit searchHit : searchHits) {
            T doc = JSON.parseObject(searchHit.getSourceAsString(), clazz);
            list.add(doc);
        }
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setTotal(total);
        pageInfo.setPages(page);
        pageInfo.setSize(size);
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public <T> Scroll<T> getScroll(String indexName, QueryBuilder queryBuilder, Class<T> clazz,Integer size) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        sourceBuilder.size(size);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        // 限制2分钟
        searchRequest.scroll(TimeValue.timeValueMinutes(2L));
        System.out.println(sourceBuilder.toString());
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        int total = (int) searchResponse.getHits().getTotalHits().value;
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<T> list = new ArrayList<T>();
        for (SearchHit searchHit : searchHits) {
            T doc = JSON.parseObject(searchHit.getSourceAsString(), clazz);
            list.add(doc);
        }
        Scroll<T> scroll = new Scroll<>();
        scroll.setList(list);
        scroll.setNextId(scrollId);
        scroll.setSize(size);
        scroll.setTotal(total);

        return scroll;
    }

    @Override
    public <T>List<T> nextScrollList(String nextId,Class<T> clazz) throws IOException {
        // 创建SearchScrollRequest
        SearchScrollRequest scrollRequest = new SearchScrollRequest(nextId);
        // 指定scroll的生存时间，若不指定它会归零
        scrollRequest.scroll(TimeValue.timeValueMinutes(2L));
        // 执行查询获取结果
        SearchResponse scrollResp = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
        //8、判断是否查询到了数据输出
        SearchHit[] hits = scrollResp.getHits().getHits();
        if(hits != null && hits.length>0){
            List<T> list = new ArrayList<T>();
            for (SearchHit searchHit : hits) {
                T doc = JSON.parseObject(searchHit.getSourceAsString(), clazz);
                list.add(doc);
            }
            return list;
        }else {
            return null;
        }

    }
}
