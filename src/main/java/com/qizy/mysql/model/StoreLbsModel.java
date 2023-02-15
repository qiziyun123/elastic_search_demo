package com.qizy.mysql.model;


import lombok.Data;

@Data
public class StoreLbsModel {

    /**
     * 数据库的ID
     */
    private Integer id;

    /**
     *
     */
    private Integer corpId;

    /**
     *
     */
    private String storeName;

    /**
     *
     */
    private String address;


    /**
     * 行政区划码
     */
    private String adCode;

    /**
     * 行政区名称
     */
    private String adName;

    /**
     *
     */
    private Integer provinceCode;

    /**
     * 省名
     */
    private String provinceName;

    /**
     *
     */
    private Integer cityCode;

    /**
     * 百度对应的城市名
     */
    private String cityName;

    /**
     * 类型
     */
    private Integer poiTypeCode;

    /**
     *
     */
    private String poiTypeName;

    /**
     * 数据类型 0 以外都是假造的数据
     */
    private Integer dataType;

    /**
     * 经度值
     */
    private Double lng;

    /**
     * 纬度值
     */
    private Double lat;
}
