package com.qizy.controller;


import com.qizy.service.EmployeeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 更新用户信息
 */
@RequestMapping("/employCustomer")
@RestController
public class CustomerUpdateController {

    @Autowired
    EmployeeCustomerService employeeCustomerService;

    /**
     * 更新用户的积分
     * @param customerId
     * @param employeeId
     * @param score
     */
    @GetMapping("updateUserScore")
    public void listEmployByCorpDept(@RequestParam(value = "customerId") String customerId,
                                     @RequestParam(value = "employeeId") String employeeId,
                                     @RequestParam(value = "score") Integer score){
        employeeCustomerService.updateUserScore(customerId,employeeId,score);
    }

    /**
     * 删除用户
     * ES删除子文档，必须指定父文档路径，并且删除父文档，子文档依然存在
     * @param customerId
     * @param employeeId
     */
    @GetMapping("deleteCustom")
    public void deleteCustom(@RequestParam(value = "customerId") String customerId,
                             @RequestParam(value = "employeeId") String employeeId){
        employeeCustomerService.deleteCustom(customerId,employeeId);
    }
}
