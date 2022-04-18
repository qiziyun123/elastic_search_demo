package com.qizy.controller;


import com.qizy.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/bankAccount")
@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    /**
     * 没有并发保证的添加 账号余额
     */
    @GetMapping("noConcurrentAdd")
    public void noConcurrentAdd(@RequestParam(value = "accountNo") String accountNoStr,
                                @RequestParam(value = "addBalance") String addBalanceStr){
        long accountNo = Long.parseLong(accountNoStr);
        long addBalance = Long.parseLong(addBalanceStr);
        bankAccountService.noConcurrentAdd(accountNo,addBalance);
    }

}
