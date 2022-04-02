package com.qizy.service;


import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;

import java.util.List;

public interface EmployeeCustomerService {

    List<EmployeeVO> listEmployByCorpDept(Integer corpId, Integer deptId);

    List<CustomerVO> listCustomerByEmployee(String employeeId);

    List<CustomerVO> listCustomerByEmployeeParam(String employeeId, String customerName);
}
