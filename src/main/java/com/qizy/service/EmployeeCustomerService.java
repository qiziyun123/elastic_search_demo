package com.qizy.service;


import com.qizy.common.PageInfo;
import com.qizy.common.Scroll;
import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;

import java.util.List;

public interface EmployeeCustomerService {

    List<EmployeeVO> listEmployByCorpDept(Integer corpId, Integer deptId);

    List<CustomerVO> listCustomerByEmployee(String employeeId);

    List<CustomerVO> listCustomerByEmployeeIdAndCustomerName(String employeeId, String customerName);

    PageInfo<CustomerVO> pageCustomerByEmployeeName(String employeeName, Integer page, Integer size);

    Scroll<EmployeeVO> getScrollEmployByCorpDept(Integer corpId, Integer deptId,Integer size);

    PageInfo<CustomerVO> pageCustomerByName(String name, Integer page, Integer size);

    void updateUserScore(String customerId, String employeeId, Integer score);

    void deleteCustom(String customerId, String employeeId);
}
