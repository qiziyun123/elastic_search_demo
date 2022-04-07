package com.qizy.controller;


import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EmployeeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employCustomer")
public class EmployeeCustomerDslController {

    @Autowired
    EmployeeCustomerService employeeCustomerService;

    /**
     * 查询某个企业某个部门企业的员工
     * @param corpId 企业ID
     * @return
     */
    @GetMapping("listEmployByCorpDept")
    public List<EmployeeVO> listEmployByCorpDept(@RequestParam(value = "corpId") Integer corpId,
                                                 @RequestParam(value = "deptId" ,required = false) Integer deptId){
        return employeeCustomerService.listEmployByCorpDept(corpId,deptId);
    }

    /**
     * 查询某个企业某个部门员工下的 客户
     * 使用ES的 parent_id 关键字查询
     * @param employeeId 员工ID
     * @return
     */
    @GetMapping("listCustomerByEmployee")
    public List<CustomerVO> listCustomerByEmployee(@RequestParam(value = "employeeId") String employeeId){
        return employeeCustomerService.listCustomerByEmployee(employeeId);
    }


    /**
     * 查询某个企业某个部门员工下的 客户,并支持客户部分查询条件
     * 使用ES的 parent_id 关键字查询
     * @param employeeId 员工ID
     * @param customerName 客户名称
     * @return
     */
    @GetMapping("listCustomerByEmployeeIdAndCustomerName")
    public List<CustomerVO> listCustomerByEmployeeIdAndCustomerName(@RequestParam(value = "employeeId") String employeeId,
                                                        @RequestParam(value = "customerName") String customerName){
        return employeeCustomerService.listCustomerByEmployeeIdAndCustomerName(employeeId,customerName);
    }

    /**
     * has_parent 师范
     * 比如查询 返回员工姓名是 指定姓名的 所有 客户，并支持分页（可跳页）
     */
    @GetMapping("pageCustomerByEmployeeName")
    public List<CustomerVO> pageCustomerByEmployeeName(@RequestParam(value = "employeeName") String employeeName,
                                           @RequestParam(value = "page") Integer page,
                                           @RequestParam(value = "size") Integer size){
        return employeeCustomerService.pageCustomerByEmployeeName(employeeName,page,size);
    }


}
