package com.qizy.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 翻页查询
 * @param <T>
 */
@Getter
@Setter
public class Scroll<T> implements Serializable {

    /**
     * 合计
     */
    private long total;

    /**
     * 数据
     */
    private List<T> list;

//    /**
//     * 是否还有下一页
//     */
//    private boolean hasNext;

    /**
     * 页大小
     */
    private int size;

    /**
     * 请求下一页ID
     */
    private String nextId;
}
