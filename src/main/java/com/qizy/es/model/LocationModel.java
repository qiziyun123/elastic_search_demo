package com.qizy.es.model;


import lombok.Data;


@Data
public class LocationModel {

    //Jackson对应的注解:@JsonProperty
    //  FastJson对应的注解: @JSONField  pom group com.fasterxml.jackson.core
    /**
     * 经度值
     */
    private Double lon;

    /**
     * 纬度值
     */
    private Double lat;


}
