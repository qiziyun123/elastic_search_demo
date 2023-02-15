package com.qizy.geotest;


import ch.hsr.geohash.GeoHash;
import com.qizy.MyEsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyEsApplication.class)
public class GeoTest {

    @Test
    public void testGeoHash(){
        double lat = 40.97681279; // 纬度坐标
        double lon = 116.61821440; // 经度坐标

        int precision = 11; // Geohash编码字符的长度（最大为12）
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lon, precision);
        String binaryCode = geoHash.toBinaryString(); // 使用给定的经纬度坐标生成的二进制编码
        System.out.println("二进制编码：" + binaryCode);
        String hashCode = geoHash.toBase32(); // 使用给定的经纬度坐标生成的Geohash字符编码
        System.out.println("Geohash编码：" + hashCode);
    }
}
