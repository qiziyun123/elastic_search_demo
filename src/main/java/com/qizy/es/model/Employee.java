package com.qizy.es.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Employee {
    @JSONField(name ="corp_id")
    private Integer corpId;
    @JSONField(name ="corp_name")
    private String corpName;
    @JSONField(name ="dept_id")
    private Integer deptId;
    @JSONField(name ="dept_name")
    private String deptName;
    @JSONField(name ="employee_id")
    private String employeeId;
    @JSONField(name ="employee_name")
    private String employeeName;
    @JSONField(name ="join_name")
    private JoinName joinName;

}
