package com.qizy.inittest;


import com.qizy.MyEsApplication;
import com.qizy.common.Scroll;
import com.qizy.es.model.StoreLbsModel;
import com.qizy.service.EsCommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyEsApplication.class)
public class InitBatchInsertRedis {


    @Autowired
    EsCommonService esCommonService;

    @Autowired
    RedisTemplate redisTemplate;

    public static final String geoKey = "geo:redius:";

    @Test
    public void insert() throws IOException {
        String indexName = "store_lbs";
        Scroll<StoreLbsModel> scroll = esCommonService.getScroll(indexName, null, null,
                StoreLbsModel.class, 100);
        // 第一次查询得到 scrollId
        String scrollId = scroll.getNextId();
        List<StoreLbsModel> list = scroll.getList();
        // 先将第一次保存redis
        saveToRedis(list);
        // 永久遍历，直到取不出数据
        int loopTime = 0;
        boolean goNext = true;
        while (goNext) {
            List<StoreLbsModel> nlist = esCommonService.nextScrollList(scrollId, StoreLbsModel.class);
            if (list != null && list.size() > 0) {
                saveToRedis(nlist);
            }

            if (nlist.size() == 0) {
                goNext = false;
            }
            loopTime++;
        }
        System.out.println("循环 " + loopTime);
    }

    private void saveToRedis(List<StoreLbsModel> list) {
        Map<String, Map<Integer, Point>> cityKeys = new HashMap<>();
        for (StoreLbsModel storeLbsModel : list) {
            double lng = storeLbsModel.getLocation().getLon();
            double lat = storeLbsModel.getLocation().getLat();
            String cityCode = String.valueOf(storeLbsModel.getCityCode());
            Integer corpId = storeLbsModel.getCorpId();
            if (cityKeys.containsKey(cityCode)) {
                Map<Integer, Point> points = cityKeys.get(cityCode);
                points.put(corpId, new Point(lng, lat));
            } else {
                Map<Integer, Point> points = new HashMap<>();
                points.put(corpId, new Point(lng, lat));
                cityKeys.put(cityCode, points);
            }
        }

        for (Map.Entry<String, Map<Integer, Point>> entry : cityKeys.entrySet()) {
            String cityCode = entry.getKey();
            Map<Integer, Point> points = entry.getValue();
            redisTemplate.boundGeoOps(geoKey + cityCode).add(points);
        }
    }


}
