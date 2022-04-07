package com.qizy.service.impl;


import com.alibaba.fastjson.JSON;
import com.qizy.common.PageInfo;
import com.qizy.common.Scroll;
import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EmployeeCustomerService;
import com.qizy.service.EsCommonService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.HasParentQueryBuilder;
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
    EsCommonService esCommonService;

    @Override
    public List<EmployeeVO> listEmployByCorpDept(Integer corpId, Integer deptId) {
        String indexName = "corp_employee_customer";
        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        boolQueryBuilder2.must(QueryBuilders.matchQuery("corp_id", corpId));
        if (!ObjectUtils.isEmpty(deptId)) {
            boolQueryBuilder2.must(QueryBuilders.matchQuery("dept_id", deptId));
        }
        boolQueryBuilder1.filter(boolQueryBuilder2);

        try {
            return esCommonService.boolQuery(indexName,boolQueryBuilder1,EmployeeVO.class);
        } catch (IOException e) {
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
        ParentIdQueryBuilder parentIdQueryBuilder = new ParentIdQueryBuilder("customer", employeeId);
        boolQueryBuilder2.must(parentIdQueryBuilder);
        boolQueryBuilder1.filter(boolQueryBuilder2);

        try {
            return esCommonService.boolQuery(indexName,boolQueryBuilder1,CustomerVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CustomerVO> listCustomerByEmployeeIdAndCustomerName(String employeeId, String customerName) {
        String indexName = "corp_employee_customer";
        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        ParentIdQueryBuilder parentIdQueryBuilder = new ParentIdQueryBuilder("customer", employeeId);
        MatchQueryBuilder customerNameQuery = QueryBuilders.matchQuery("customer_name", customerName);

        boolQueryBuilder2.must(parentIdQueryBuilder).must(customerNameQuery);
        boolQueryBuilder1.filter(boolQueryBuilder2);

        try {
            return esCommonService.boolQuery(indexName,boolQueryBuilder1,CustomerVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PageInfo<CustomerVO> pageCustomerByEmployeeName(String employeeName, Integer page, Integer size) {
        String indexName = "corp_employee_customer";
        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        QueryBuilder innerHasParentQueryBulider = QueryBuilders.matchQuery("employee_name",employeeName);
        HasParentQueryBuilder hasParentQueryBuilder = new HasParentQueryBuilder("employee",innerHasParentQueryBulider,false);
        boolQueryBuilder2.must(hasParentQueryBuilder);
        boolQueryBuilder1.filter(boolQueryBuilder2);

        try {
            return esCommonService.pageBoolQuery(indexName,boolQueryBuilder1,CustomerVO.class,
                    page,size);
        } catch (IOException e) {
            e.printStackTrace();
            PageInfo<CustomerVO> pageInfo = new PageInfo<>();
            pageInfo.setList(null);
            pageInfo.setTotal(0);
            return pageInfo;
        }

    }

    @Override
    public Scroll<EmployeeVO> getScrollEmployByCorpDept(Integer corpId, Integer deptId,Integer size) {
        String indexName = "corp_employee_customer";
        // 最外层bool
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        // 里层bool
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        boolQueryBuilder2.must(QueryBuilders.matchQuery("corp_id", corpId));
        if (!ObjectUtils.isEmpty(deptId)) {
            boolQueryBuilder2.must(QueryBuilders.matchQuery("dept_id", deptId));
        }
        boolQueryBuilder1.filter(boolQueryBuilder2);

        try {
            return esCommonService.getScroll(indexName,boolQueryBuilder1,EmployeeVO.class,size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
