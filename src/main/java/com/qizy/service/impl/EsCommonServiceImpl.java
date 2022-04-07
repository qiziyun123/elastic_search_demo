package com.qizy.service.impl;


import com.alibaba.fastjson.JSON;
import com.qizy.common.PageInfo;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EsCommonService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
