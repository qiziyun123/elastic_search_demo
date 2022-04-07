package com.qizy.service;

import com.qizy.common.PageInfo;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Es 通用类
 */
public interface EsCommonService {

    /**
     * bool多条件查询
     *
     * @param indexName
     * @param boolQueryBuilder
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> List<T> boolQuery(String indexName, BoolQueryBuilder boolQueryBuilder,
                          Class<T> clazz) throws IOException;

    /**
     * bool多条件浅分页查询
     *
     * @param indexName
     * @param boolQueryBuilder
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> PageInfo<T> pageBoolQuery(String indexName, BoolQueryBuilder boolQueryBuilder,
                                  Class<T> clazz,Integer page, Integer size) throws IOException,RuntimeException;
}
