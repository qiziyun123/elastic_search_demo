package com.qizy.service.impl;

import com.alibaba.fastjson.JSON;
import com.qizy.es.model.Customer;
import com.qizy.es.model.Employee;
import com.qizy.es.model.JoinName;
import com.qizy.service.PrepareDataService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


@Service
public class PrepareDataServiceImpl implements PrepareDataService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public void prepareEmployeeCustomerData() {
        String indexName = "corp_employee_customer";
        String[] userTags = {"体育", "游戏", "歌星", "演员", "历史", "小说", "科技", "政治", "军事", "土豪", "摄影", "时尚", "旅游", "汽车","打工人"};
        String[] userNames = {"许澄邈", "刘德泽", "程海超", "邓海阳", "邓海荣", "陈海逸", "宋海昌", "徐瀚钰", "徐瀚文", "杨楠", "安嘉和",
                "陈涵亮", "程涵煦", "宋明宇", "徐涵衍", "万浩皛", "万浩波", "荣浩博", "陈浩初", "陈浩宕", "赵浩歌", "周浩广", "梅婷","韩鹏",
                "周浩邈", "周浩气", "章浩思", "徐浩言", "徐鸿宝", "许鸿波", "许鸿博", "许鸿才", "徐鸿畅", "许鸿畴", "宋鸿达", "石林","王晓鸥",
                "徐鸿德", "徐鸿飞", "徐鸿风", "徐鸿福", "许鸿光", "徐鸿晖", "章鸿朗", "周鸿文", "章鸿轩", "宋鸿煊", "和鸿骞", "薛宝钗",
                "凯鸿远", "宋鸿云", "徐鸿哲", "徐鸿祯", "徐鸿志", "徐鸿卓", "徐嘉澍", "徐光济", "徐澎湃", "徐彭泽", "宋鹏池", "姚佳",
                "宋鹏海", "宋浦和", "宋浦泽", "方瑞渊", "方越泽", "方博耘", "方德运", "方辰宇", "方辰皓", "方辰钊", "方辰铭", "何平",
                "方辰锟", "方辰阳", "方辰韦", "方辰良", "方辰沛", "方晨轩", "方晨涛", "方晨濡", "方晨潍", "方鸿振", "方吉星", "蓝晓斌",
                "方铭晨", "方起运", "方运凡", "方运凯", "方运鹏", "方运浩", "方运诚", "方运良", "方运鸿", "方运锋", "方运盛", "王思涵",
                "方运升", "方运杰", "方运珧", "方运骏", "方运凯", "方运乾", "方维运", "方运晟", "方运莱", "方运华", "方耘豪", "周文王",
                "方星爵", "方星腾", "方星睿", "李武", "李成", "李兵", "李良", "李超", "李劲", "李锋", "李杉", "李铭", "李尧", "李国强",
                "李恺", "李清", "李毅", "李安", "李福", "李淼", "李益", "李易", "李一", "李宇", "李浩", "李焕", "李明", "李忠", "韩卓智",
                "李宁", "李虎", "李儒", "李哲", "李贵", "李焘", "李晖", "李森", "李泉", "李平", "李黎", "李飞", "李琛", "李晓", "雷瑞山",
                "李功", "李韬", "李彬", "李龙", "李穆", "李江", "李宏", "李杰", "李康", "李钦", "李阳", "李剑", "李蒙", "李恒", "徐迪",
                "李旭", "李柏", "李楠", "李奎", "李进", "方若强", "陆枝妃", "陆齐榕", "田婕遥", "方秀蓓", "黄飞", "杨汝辰", "张斌", "谢峰",
                "徐静玉", "周莎", "赖利雨", "吕枝莉", "钟香蓉", "赖英", "万怀宜", "沈凤丹", "李宁", "蔡子强", "侯求武", "冉镇龙", "蒋日军",
                "徐孝姣", "黄毕海", "熊从发", "袁怀斐", "张天志", "叶问", "李小龙", "金山找", "刘玉叶", "刘洋", "马洋", "马永贞", "郭伟", "姜辉",
                "王磊", "李雪梅", "张庆忠", "吴建忠", "徐强", "亢钊", "刘海涛", "邹雨宸", "翟涛", "焦阳", "包龙星", "李天一", "冯辉", "王凯", "陈甜甜", "冉琳艳", "颜红颜",
                "张海涛", "王育", "车范根", "杜文慧", "李有才", "邵震", "李佳琪", "于根", "董文晓", "董伟", "吴希伟", "黄河", "潘绍井"};
        Map<Integer, String> corpMap = new HashMap<>();
        corpMap.put(0, "A");
        corpMap.put(1, "B");
        corpMap.put(2, "C");
        corpMap.put(3, "D");
        corpMap.put(4, "E");
        corpMap.put(5, "F");
        corpMap.put(6, "G");
        corpMap.put(7, "H");
        corpMap.put(8, "I");
        corpMap.put(9, "J");
        corpMap.put(10, "A-1");
        corpMap.put(11, "A-2");
        corpMap.put(12, "A-3");
        corpMap.put(13, "B-1");
        corpMap.put(14, "B-2");
        corpMap.put(15, "B-3");
        corpMap.put(16, "满座");
        corpMap.put(17, "全聚德");
        corpMap.put(18, "超能洗衣粉");
        corpMap.put(19, "孵化场");
        corpMap.put(20, "链家地产");

        Map<Integer, String> deptMap = new HashMap<>();
        deptMap.put(0, "部门一");
        deptMap.put(1, "部门二");
        deptMap.put(2, "部门三");
        deptMap.put(3, "部门四");
        deptMap.put(4, "部门五");
        deptMap.put(5, "技术部");
        deptMap.put(6, "财务部");
        deptMap.put(7, "行政部");

        String[] employeeNames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "鲁", "韦", "张", "陈", "郭", "范", "高", "林", "黄", "朱", "许", "连", "韩", "辛", "蒋", "楚",
                "刘", "宋", "金", "曹", "沈", "谢", "杜", "丁", "庞", "吕", "蔡", "段", "魏", "王", "张", "李", "欧阳", "司马", "方", "董", "吴", "唐", "杨", "慕容", "诸葛"};

        int employeeSize = 10;
        int customSize = 300;

        for (int i = 0; i < employeeSize; i++) {
            String employeeId = UUID.randomUUID().toString();
            int nameIndex = new Random().nextInt(employeeNames.length);

            String employeeName = employeeNames[nameIndex];
            int corpId = new Random().nextInt(corpMap.size());
            String corpName = corpMap.get(corpId);
            int deptId = new Random().nextInt(deptMap.size());
            String deptName = deptMap.get(deptId);

            Employee employee = new Employee();
            employee.setCorpId(corpId);
            employee.setCorpName(corpName);
            employee.setEmployeeId(employeeId);
            employee.setEmployeeName(employeeName);
            employee.setDeptId(deptId);
            employee.setDeptName(deptName);
            JoinName parentJoinName = new JoinName();
            parentJoinName.setName("employee");
            employee.setJoinName(parentJoinName);

            try {
                // 插入员工父
                IndexRequest request = new IndexRequest(indexName);
                String employJson = JSON.toJSONString(employee);
                request.id(employeeId);
                request.source(employJson, XContentType.JSON);
                IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
                System.out.println(indexResponse.status().getStatus());
                List<Customer> customersList = new ArrayList<>(customSize);
                for (int j = 0; j < customSize; j++) {
                    Customer customer = new Customer();
                    Set<String> customerTagName = randomTagNames(userTags);
                    customer.setCustomerTagName(customerTagName);
                    String customerId = UUID.randomUUID().toString();
                    customer.setCustomerId(customerId);
                    String customerName = randomCustomerName(userNames);
                    customer.setCustomerName(customerName);
                    JoinName joinName = new JoinName();
                    joinName.setName("customer");
                    joinName.setParent(employeeId);
                    customer.setJoinName(joinName);
                    customersList.add(customer);
                }
                BulkRequest bulkRequest = new BulkRequest();

                bulkRequest.timeout("15s");
                for (Customer customer : customersList) {
                    bulkRequest.add(
                            new IndexRequest(indexName)
                                    .id(customer.getCustomerId()).routing(employeeId)
                                    .source(JSON.toJSONString(customer), XContentType.JSON)
                    );
                }
                long s = System.currentTimeMillis();
                BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                long e = System.currentTimeMillis();
                System.out.println("each size is " + customSize + " cost time " + (e - s));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String randomCustomerName(String[] userNames) {
        int index = new Random().nextInt(userNames.length);
        return userNames[index];
    }

    private Set<String> randomTagNames(String[] userTags) {
        Set<String> customerTagNameSet = new HashSet<>();
        // 假设 每个用户的标签可以是 0个  1个 2个
        int tagNum = new Random().nextInt(3);
        for (int i = 0; i < tagNum; i++) {
            int tagIndex = new Random().nextInt(userTags.length);
            String customerTagName = userTags[tagIndex];
            customerTagNameSet.add(customerTagName);
        }
        return customerTagNameSet;
    }
}
