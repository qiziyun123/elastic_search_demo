package com.qizy.es.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("corp_id")
    private Integer corpId;

    /**
     *
     */
    @JsonProperty("store_name")
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
    @JsonProperty("adname")
    private String adName;

    /**
     *
     */
    @JsonProperty("province_code")
    private Integer provinceCode;

    /**
     * 省名
     */
    @JsonProperty("province_name")
    private String provinceName;

    /**
     *
     */
    @JsonProperty("city_code")
    private Integer cityCode;

    /**
     * 百度对应的城市名
     */
    @JsonProperty("city_name")
    private String cityName;

    /**
     * 类型
     */
    @JsonProperty("poi_type_code")
    private Integer poiTypeCode;

    /**
     *
     */
    @JsonProperty("poi_type_name")
    private String poiTypeName;

    /**
     * 数据类型 0 以外都是假造的数据
     */
    @JsonProperty("data_type")
    private Integer dataType;

    /**
     *
     */
    private LocationModel location;
}
