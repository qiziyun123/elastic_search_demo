package com.qizy.es.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Set;

@Data
public class Customer {
    @JSONField(name ="customer_id")
    private String customerId;

    @JSONField(name ="customer_name")
    private String customerName;

    @JSONField(name ="customer_tag_name")
    private Set<String> customerTagName;
    @JSONField(name ="join_name")
    private JoinName joinName;

}
