package com.qizy.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 * @param <T>
 */
@Getter
@Setter
public class PageInfo<T> implements Serializable {

    /**
     * 合计
     */
    private long total;

    /**
     * 数据
     */
    private List<T> list;

    /**
     * 当前页
     */
    private int pages;

    /**
     * 页大小
     */
    private int size;
}
