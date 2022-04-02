package com.qizy.controller;

import com.qizy.service.PrepareDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prepareData")
public class PrepareDataController {

    @Autowired
    PrepareDataService prepareDataService;

    @GetMapping("prepareEmployeeCustomerData")
    public void prepareEmployeeCustomerData(){
        for(int i = 0;i<10000;i++){
            prepareDataService.prepareEmployeeCustomerData();
        }

    }

}
