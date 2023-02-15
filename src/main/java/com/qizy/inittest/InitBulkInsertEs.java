package com.qizy.inittest;

import com.qizy.MyEsApplication;
import com.qizy.mysql.model.StoreLbsModel;
import com.qizy.mapper.StoreLbsMapper;
import com.qizy.service.EsCommonService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyEsApplication.class)
public class InitBulkInsertEs {

    @Autowired
    EsCommonService esCommonService;

    @Autowired
    StoreLbsMapper storeLbsMapper;

    @Autowired
    RestHighLevelClient client;

    // 从mysql查询 导入ES
    @Test
    public void insert() {
        int start = 8223697;
        int end = 8223697+1000;
        int add = 1000;
        int sum = 8311906;
        while (start < sum) {
            List<StoreLbsModel> list = storeLbsMapper.queryBetween(start, end);
            if (list != null && list.size() > 0) {
                BulkRequest bulkRequest = new BulkRequest();
                for (StoreLbsModel storeLbsModel : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", storeLbsModel.getId());
                    map.put("corp_id", storeLbsModel.getCorpId());
                    map.put("store_name", storeLbsModel.getStoreName());
                    map.put("adcode", storeLbsModel.getAdCode());
                    map.put("province_code", storeLbsModel.getProvinceCode());
                    map.put("city_code", storeLbsModel.getCityCode());
                    map.put("province_name", storeLbsModel.getProvinceName());
                    map.put("city_name", storeLbsModel.getCityName());
                    map.put("adname", storeLbsModel.getAdName());
                    map.put("address", storeLbsModel.getAddress());
                    map.put("poi_type_name", storeLbsModel.getPoiTypeName());
                    map.put("poi_type_code", storeLbsModel.getPoiTypeCode());

                    Map<String, Object> location = new HashMap<>();
                    location.put("lat", storeLbsModel.getLat());
                    location.put("lon", storeLbsModel.getLng());
                    map.put("location", location);
                    bulkRequest.add(new IndexRequest("store_lbs").source(map));
                }

                try {
                    client.bulk(bulkRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    if (!(e.getMessage().contains("200 OK"))) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            start+=add;
            end+=add;
        }

    }


//    @Test
//    public void queryNotExist() throws IOException {
//        String indexName = "store_lbs";
//        Scroll<StoreLbsModel> scroll = esCommonService.getScroll(indexName, null, null,
//                com.qizy.es.model.StoreLbsModel.class, 500);
//        // 第一次查询得到 scrollId
//        String scrollId = scroll.getNextId();
//        List<com.qizy.es.model.StoreLbsModel> list = scroll.getList();
//        // 先将第一次保存redis
//        queryFromMysql(list);
//        // 永久遍历，直到取不出数据
//        int loopTime = 0;
//        boolean goNext = true;
//        while (goNext) {
//            List<com.qizy.es.model.StoreLbsModel> nlist = esCommonService.nextScrollList(scrollId, StoreLbsModel.class);
//            queryFromMysql(nlist);
//            if (nlist.size() == 0) {
//                goNext = false;
//            }
//            loopTime++;
//        }
//
//    }

    private void queryFromMysql(List<StoreLbsModel> list) {
        List<Integer> esIds = list.stream().map(StoreLbsModel::getId).collect(Collectors.toList());
        List<Integer> mysqlIds = storeLbsMapper.getByIds(esIds);
        esIds.removeAll(mysqlIds);
        if (esIds.size() > 0) {
            for(Integer id:esIds){
                System.out.println(id);
            }

        }

    }

    @Test
    public void testRemove(){
        List<Integer> esIds = new ArrayList<>();
        esIds.add(1462);esIds.add(1463);esIds.add(1464);

        List<Integer> mysqlIds = new ArrayList<>();
        mysqlIds.add(1462);
        mysqlIds.add(1464);
        esIds.removeAll(mysqlIds);
        if (esIds.size() > 0) {
            for(Integer id:esIds){
                System.out.println(id);
            }

        }

    }
}
