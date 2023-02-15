package com.qizy.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tiger
 *
geohash长度	宽度width	高度height	Lat位数	Lng位数	Lat误差	        Lng误差	        km误差
1	        5,009.4km	4,992.6km	2	    3	    ±23	            ±23	            ±2500
2	        1,252.3km	624.1km	    5	    5	    ± 2.8	        ±5.6	        ±630
3	        156.5km	    156km	    7	    8	    ± 0.70	        ± 0.7	        ±78
4	        39.1km	    19.5km	    10	    10	    ± 0.087	        ± 0.18	        ±20
5	        4.9km	    4.9km	    12	    13	    ± 0.022	        ± 0.022	        ±2.4
6	        1.2km	    609.4m	    15	    15	    ± 0.0027	    ± 0.0055	    ±0.61
7	        152.9m	    152.4m	    17	    18	    ±0.00068	    ±0.00068	    ±0.076
8	        38.2m	    19m	        20	    20	    ±0.000086	    ±0.000172	    ±0.01911
9	        4.8m	    4.8m	    22	    23	    ±0.000021	    ±0.000021	    ±0.00478
10	        1.2m	    59.5cm	    25	    25	    ±0.00000268	    ±0.00000536	    ±0.0005971
11	        14.9cm	    14.9cm	    27	    28	    ±0.00000067	    ±0.00000067	    ±0.0001492
12	        3.7cm	    1.9cm	    30	    30	    ±0.00000008	    ±0.00000017	    ±0.0000186

对比4位数的时候，查找20km左右 5位数2.4KM
 */
public class GEOHash2 {
    private LocationBean location;
    /**
     * 1 2500km;2 630km;3 78km;4 30km
     * 5 2.4km; 6 610m; 7 76m; 8 19m
     */
    private int hashLength = 8; //默认算8位,经纬度转化为geohash长度
    private int latLength = 20; //纬度转化为二进制长度
    private int lngLength = 20; //经度转化为二进制长度

    private double minLat;//每格纬度的单位大小
    private double minLng;//每个经度的倒下
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public GEOHash2(double lat, double lng) {
        location = new LocationBean(lat, lng);
        setMinLatLng();
    }

    public int gethashLength() {
        return hashLength;
    }

    /**
     * @Author:lulei
     * @Description: 设置经纬度的最小单位
     */
    private void setMinLatLng() {
        minLat = LocationBean.MAXLAT - LocationBean.MINLAT;
        for (int i = 0; i < latLength; i++) {
            minLat /= 2.0;
        }
        minLng = LocationBean.MAXLNG - LocationBean.MINLNG;
        for (int i = 0; i < lngLength; i++) {
            minLng /= 2.0;
        }
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 求所在坐标点及周围点组成的九个
     */
    public List<String> getGeoHashBase32For9() {
        double leftLat = location.getLat() - minLat;
        double rightLat = location.getLat() + minLat;
        double upLng = location.getLng() - minLng;
        double downLng = location.getLng() + minLng;
        List<String> base32For9 = new ArrayList<String>();
        //左侧从上到下 3个
        String leftUp = getGeoHashBase32(leftLat, upLng);
        if (!(leftUp == null || "".equals(leftUp))) {
            base32For9.add(leftUp);
        }
        String leftMid = getGeoHashBase32(leftLat, location.getLng());
        if (!(leftMid == null || "".equals(leftMid))) {
            base32For9.add(leftMid);
        }
        String leftDown = getGeoHashBase32(leftLat, downLng);
        if (!(leftDown == null || "".equals(leftDown))) {
            base32For9.add(leftDown);
        }
        //中间从上到下 3个
        String midUp = getGeoHashBase32(location.getLat(), upLng);
        if (!(midUp == null || "".equals(midUp))) {
            base32For9.add(midUp);
        }
        String midMid = getGeoHashBase32(location.getLat(), location.getLng());
        if (!(midMid == null || "".equals(midMid))) {
            base32For9.add(midMid);
        }
        String midDown = getGeoHashBase32(location.getLat(), downLng);
        if (!(midDown == null || "".equals(midDown))) {
            base32For9.add(midDown);
        }
        //右侧从上到下 3个
        String rightUp = getGeoHashBase32(rightLat, upLng);
        if (!(rightUp == null || "".equals(rightUp))) {
            base32For9.add(rightUp);
        }
        String rightMid = getGeoHashBase32(rightLat, location.getLng());
        if (!(rightMid == null || "".equals(rightMid))) {
            base32For9.add(rightMid);
        }
        String rightDown = getGeoHashBase32(rightLat, downLng);
        if (!(rightDown == null || "".equals(rightDown))) {
            base32For9.add(rightDown);
        }
        return base32For9;
    }

    /**
     * @param length
     * @return
     * @Author:lulei
     * @Description: 设置经纬度转化为geohash长度
     */
    public boolean sethashLength(int length) {
        if (length < 1) {
            return false;
        }
        hashLength = length;
        latLength = (length * 5) / 2;
        if (length % 2 == 0) {
            lngLength = latLength;
        } else {
            lngLength = latLength + 1;
        }
        setMinLatLng();
        return true;
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 获取经纬度的base32字符串
     */
    public String getGeoHashBase32() {
        return getGeoHashBase32(location.getLat(), location.getLng());
    }

    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei
     * @Description: 获取经纬度的base32字符串
     */
    private String getGeoHashBase32(double lat, double lng) {
        boolean[] bools = getGeoBinary(lat, lng);
        if (bools == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bools.length; i = i + 5) {
            boolean[] base32 = new boolean[5];
            for (int j = 0; j < 5; j++) {
                base32[j] = bools[i + j];
            }
            char cha = getBase32Char(base32);
            if (' ' == cha) {
                return null;
            }
            sb.append(cha);
        }
        return sb.toString();
    }

    /**
     * @param base32
     * @return
     * @Author:lulei
     * @Description: 将五位二进制转化为base32
     */
    private char getBase32Char(boolean[] base32) {
        if (base32 == null || base32.length != 5) {
            return ' ';
        }
        int num = 0;
        for (boolean bool : base32) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return CHARS[num % CHARS.length];
    }

    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei
     * @Description: 获取坐标的geo二进制字符串
     */
    private boolean[] getGeoBinary(double lat, double lng) {
        boolean[] latArray = getHashArray(lat, LocationBean.MINLAT, LocationBean.MAXLAT, latLength);
        boolean[] lngArray = getHashArray(lng, LocationBean.MINLNG, LocationBean.MAXLNG, lngLength);
        return merge(latArray, lngArray);
    }

    /**
     * @param latArray
     * @param lngArray
     * @return
     * @Author:lulei
     * @Description: 合并经纬度二进制
     */
    private boolean[] merge(boolean[] latArray, boolean[] lngArray) {
        if (latArray == null || lngArray == null) {
            return null;
        }
        boolean[] result = new boolean[lngArray.length + latArray.length];
        Arrays.fill(result, false);
        for (int i = 0; i < lngArray.length; i++) {
            result[2 * i] = lngArray[i];
        }
        for (int i = 0; i < latArray.length; i++) {
            result[2 * i + 1] = latArray[i];
        }
        return result;
    }

    /**
     * @param value
     * @param min
     * @param max
     * @return
     * @Author:lulei
     * @Description: 将数字转化为geohash二进制字符串
     */
    private boolean[] getHashArray(double value, double min, double max, int length) {
        if (value < min || value > max) {
            return null;
        }
        if (length < 1) {
            return null;
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            double mid = (min + max) / 2.0;
            if (value > mid) {
                result[i] = true;
                min = mid;
            } else {
                result[i] = false;
                max = mid;
            }
        }
        return result;
    }

    class LocationBean {
        public static final double MINLAT = -90;
        public static final double MAXLAT = 90;
        public static final double MINLNG = -180;
        public static final double MAXLNG = 180;
        private double lat;//纬度[-90,90]
        private double lng;//经度[-180,180]

        public LocationBean(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
        public double getLat() {
            return lat;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public double getLng() {
            return lng;
        }
        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        GEOHash2 g = new GEOHash2(40.222012, 116.248283);
        g.sethashLength(12);
        System.out.println("当前坐标："+g.getGeoHashBase32());
        for (String str:
                g.getGeoHashBase32For9()) {
            System.out.println(str);
        }
    }
}