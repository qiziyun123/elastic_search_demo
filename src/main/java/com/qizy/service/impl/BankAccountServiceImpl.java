package com.qizy.service.impl;


import com.qizy.service.BankAccountService;
import com.qizy.service.EsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    EsCommonService esCommonService;

    @Override
    public void noConcurrentAdd(long accountNo, long addBalance) {
        String indexName = "bank_account";

        try{
            esCommonService.updateSingleProAddValueNoConcurrent(indexName,String.valueOf(accountNo),"balance",addBalance);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
