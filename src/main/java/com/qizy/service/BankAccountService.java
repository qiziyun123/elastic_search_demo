package com.qizy.service;

public interface BankAccountService {
    /**
     * 没有并发保证的添加 账号余额
     */
    void noConcurrentAdd(long accountNo, long addBalance);
}
