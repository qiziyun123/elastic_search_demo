package com.qizy.es.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 银行账号，主要用户 并发测试
 */
@Data
public class BankAccount {
    @JSONField(name ="account_no")
    private long accountNo;

    private long balance;

    @JSONField(name ="owner_name")
    private String ownerName;
}
