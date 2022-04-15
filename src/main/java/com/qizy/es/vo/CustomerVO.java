package com.qizy.es.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


import java.util.Set;

@Data
public class CustomerVO {

    @JSONField(name ="customer_id")
    private String customerId;

    @JSONField(name ="customer_name")
    private String customerName;

    private String nick;

    @JSONField(name ="customer_tag_name")
    private Set<String> customerTagName;

    @JSONField(name ="employee_id")
    private String employeeId;
}
