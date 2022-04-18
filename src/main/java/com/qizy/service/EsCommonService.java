package com.qizy.service;

import com.qizy.common.PageInfo;
import com.qizy.common.Scroll;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

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
                                  Class<T> clazz, Integer page, Integer size) throws IOException, RuntimeException;

    /**
     * 获得翻页查询
     *
     * @param indexName
     * @param queryBuilder
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Scroll<T> getScroll(String indexName, QueryBuilder queryBuilder, Class<T> clazz, Integer size) throws IOException;

    /**
     * 已获得翻页的基础上
     *
     * @param nextId
     * @return
     */
    <T> List<T> nextScrollList(String nextId, Class<T> clazz) throws IOException;


    /**
     * 局部更新文档
     *
     * @param indexName
     * @param docId
     */
    void updateDoc(String indexName, String docId, List<String> properties, List<String> values) throws IOException;

    /**
     * 父子类型更新子文档
     *
     * @param indexName
     * @param childId
     * @param parentId
     */
    void updateChild(String indexName, String childId, String parentId, List<String> properties, List<String> values) throws IOException;

    /**
     * 删除子文档
     *
     * @param indexName
     * @param childId
     * @param parentId
     */
    void deleteChild(String indexName, String childId, String parentId) throws IOException;

    void updateSingleProAddValueNoConcurrent(String indexName, String docId, String property, long addValue) throws IOException;
}
