package com.qizy.service.impl;


import com.alibaba.fastjson.JSON;
import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EmployeeCustomerService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.ParentIdQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeCustomerServiceImpl implements EmployeeCustomerService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public List<EmployeeVO> listEmployByCorpDept(Integer corpId, Integer deptId) {
        String indexName = "corp_employee_customer";

        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        boolQueryBuilder2.must(QueryBuilders.matchQuery("corp_id",corpId));
        if(!ObjectUtils.isEmpty(deptId)){
            boolQueryBuilder2.must(QueryBuilders.matchQuery("dept_id",deptId));
        }
        boolQueryBuilder1.filter(boolQueryBuilder2);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder1);
        sourceBuilder.size(30);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        System.out.println(sourceBuilder.toString());
        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//          int total = (int)searchResponse.getHits().getTotalHits().value;
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<EmployeeVO> list = new ArrayList<>();
            for(SearchHit searchHit:searchHits) {
                EmployeeVO employee = JSON.parseObject(searchHit.getSourceAsString(), EmployeeVO.class);
                list.add(employee);
            }
            return list;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CustomerVO> listCustomerByEmployee(String employeeId) {
        String indexName = "corp_employee_customer";

        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        ParentIdQueryBuilder parentIdQueryBuilder = new ParentIdQueryBuilder("customer",employeeId);
        boolQueryBuilder2.must(parentIdQueryBuilder);
        boolQueryBuilder1.filter(boolQueryBuilder2);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder1);
        sourceBuilder.size(20);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        System.out.println(sourceBuilder.toString());
        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<CustomerVO> list = new ArrayList<>();
            for(SearchHit searchHit:searchHits) {
                CustomerVO customerVO = JSON.parseObject(searchHit.getSourceAsString(), CustomerVO.class);
                list.add(customerVO);
            }
            return list;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CustomerVO> listCustomerByEmployeeParam(String employeeId, String customerName) {
        String indexName = "corp_employee_customer";
        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        ParentIdQueryBuilder parentIdQueryBuilder = new ParentIdQueryBuilder("customer",employeeId);
        MatchQueryBuilder customerNameQuery = QueryBuilders.matchQuery("customer_name",customerName);

        boolQueryBuilder2.must(parentIdQueryBuilder).must(customerNameQuery);
        boolQueryBuilder1.filter(boolQueryBuilder2);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder1);
        sourceBuilder.size(10);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        System.out.println(sourceBuilder.toString());
        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<CustomerVO> list = new ArrayList<>();
            for(SearchHit searchHit:searchHits) {
                CustomerVO customerVO = JSON.parseObject(searchHit.getSourceAsString(), CustomerVO.class);
                list.add(customerVO);
            }
            return list;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}