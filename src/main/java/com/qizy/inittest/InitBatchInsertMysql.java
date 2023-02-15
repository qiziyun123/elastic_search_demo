package com.qizy.inittest;

import com.qizy.MyEsApplication;
import com.qizy.common.Scroll;
import com.qizy.es.model.StoreLbsModel;
import com.qizy.mapper.StoreLbsMapper;
import com.qizy.service.EsCommonService;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyEsApplication.class)
public class InitBatchInsertMysql {

    @Autowired
    EsCommonService esCommonService;

    @Autowired
    StoreLbsMapper storeLbsMapper;

    @Test
    public void insert() throws IOException {
        String indexName = "store_lbs";
        List<SortBuilder> sortBuilders = new ArrayList<>();
        sortBuilders.add(new FieldSortBuilder("data_type").order(SortOrder.DESC));
        sortBuilders.add(new FieldSortBuilder("city_code").order(SortOrder.DESC));
        Scroll<StoreLbsModel> scroll = esCommonService.getScroll(indexName, null,sortBuilders,
                StoreLbsModel.class, 200);
        // 第一次查询得到 scrollId
        String scrollId = scroll.getNextId();
        List<StoreLbsModel> list = scroll.getList();
        // 先将第一次保存redis
        saveToMysql(list);
        // 永久遍历，直到取不出数据
        int loopTime = 0;
        boolean goNext = true;
        while (goNext) {
            List<StoreLbsModel> nlist = esCommonService.nextScrollList(scrollId, StoreLbsModel.class);
            saveToMysql(nlist);
            if (nlist.size() == 0) {
                goNext = false;
            }
            loopTime++;
        }
        System.out.println("循环 " + loopTime);
    }

    private void saveToMysql(List<StoreLbsModel> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        List<com.qizy.mysql.model.StoreLbsModel> saveList = new ArrayList<>();
        for (StoreLbsModel storeLbsModel : list) {
            com.qizy.mysql.model.StoreLbsModel mysqlModel = new com.qizy.mysql.model.StoreLbsModel();
            BeanUtils.copyProperties(storeLbsModel,mysqlModel);
            double lng = storeLbsModel.getLocation().getLon();
            double lat = storeLbsModel.getLocation().getLat();
            mysqlModel.setLng(lng);
            mysqlModel.setLat(lat);
            saveList.add(mysqlModel);
        }
        storeLbsMapper.batchInsertOrUpdate(saveList);

    }
}
