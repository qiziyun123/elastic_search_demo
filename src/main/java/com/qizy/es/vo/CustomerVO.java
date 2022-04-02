package com.qizy.es.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CustomerVO {

    @JSONField(name ="customer_id")
    private String customerId;

    @JSONField(name ="customer_name")
    private String customerName;

    @JSONField(name ="customer_tag_name")
    private List<String> customerTagName;
}
