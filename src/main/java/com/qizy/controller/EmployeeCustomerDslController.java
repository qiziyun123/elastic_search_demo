package com.qizy.controller;


import com.qizy.common.PageInfo;
import com.qizy.common.Scroll;
import com.qizy.es.vo.CustomerVO;
import com.qizy.es.vo.EmployeeVO;
import com.qizy.service.EmployeeCustomerService;
import com.qizy.service.EsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employCustomer")
public class EmployeeCustomerDslController {

    @Autowired
    EmployeeCustomerService employeeCustomerService;

    @Autowired
    EsCommonService esCommonService;

    /**
     * 查询某个企业某个部门企业的员工
     * ES 默认查询10条
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
    public PageInfo<CustomerVO> pageCustomerByEmployeeName(@RequestParam(value = "employeeName") String employeeName,
                                                           @RequestParam(value = "page") Integer page,
                                                           @RequestParam(value = "size") Integer size){
        return employeeCustomerService.pageCustomerByEmployeeName(employeeName,page,size);
    }

    /**
     * 查询某个企业某个部门企业的员工
     * ES 默认查询10条
     * @param corpId 企业ID
     * @return
     */
    @GetMapping("getScrollEmployByCorpDept")
    public Scroll<EmployeeVO> getScrollEmployByCorpDept(@RequestParam(value = "corpId") Integer corpId,
                                                        @RequestParam(value = "deptId" ,required = false) Integer deptId,
                                                        @RequestParam(value = "size") Integer size){
        return employeeCustomerService.getScrollEmployByCorpDept(corpId,deptId,size);
    }

    /**
     * 翻页查询 某个企业某个部门企业的员工
     * @param nextId
     * @return
     * @throws IOException
     */
    @GetMapping("nextScrollEmployByCorpDept")
    public List<EmployeeVO> getScrollEmployByCorpDept(@RequestParam(value = "nextId") String nextId) throws IOException {
        return esCommonService.nextScrollList(nextId,EmployeeVO.class);
    }

    /**
     * 模糊搜索客户名称 所有 客户，并支持分页（可跳页）
     */
    @GetMapping("pageCustomerByName")
    public PageInfo<CustomerVO> pageCustomerByName(@RequestParam(value = "name") String name,
                                                           @RequestParam(value = "page") Integer page,
                                                           @RequestParam(value = "size") Integer size){
        return employeeCustomerService.pageCustomerByName(name,page,size);
    }

}
