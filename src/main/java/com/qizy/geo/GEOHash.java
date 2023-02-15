package com.qizy.geo;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.HashMap;

/**
 * @author lx
 */
public class GEOHash {
    static final String ONE = "1";
    static final String ZERO = "0";
    static final String TWO = "2";
    static final String LONGITUDE_MAX = "180";
    static final String LONGITUDE_MIN = "-180";
    static final String LATITUDE_MAX = "90";
    static final String LATITUDE_MIN = "-90";
    static final int BIN_NUM = 26;
    static final int MERGE_BIN_NUM = 26 << 1;

    /**
     * Base32编码的字符串池
     */
    private final static char[] BASE_32_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 与十进制值的对应关系
     */
    final static HashMap<Character, Integer> BASE_32_LOOKUP = new HashMap<Character, Integer>();


    static {
        int i = 0;
        for (char c : BASE_32_CHARS) {
            BASE_32_LOOKUP.put(c, i++);
        }
    }

    public static void main(String[] args) {
        String longitude = getBin("116.61821440", true);
//        System.out.println("longitudebin: " + longitude);
        String latitude = getBin("40.97681279", false);
//        System.out.println("latitudebin: " + latitude);
        String merge = merge(longitude, latitude);
//        System.out.println("mergebin: " + merge);
        String geoHash = base32Encode(merge);
        System.out.println("geohash: " + geoHash);
        double[] doubles = base32Decode(geoHash);
        System.out.println("longitude: " + doubles[0] + ",  latitude: " + doubles[1]);
    }

    /**
     * 经纬度的二进制编码
     */
    private static String getBin(String longitudeOrLatitude, boolean isLongitudeOrLatitude) {
        String min, max;
        if (isLongitudeOrLatitude) {
            max = LONGITUDE_MAX;
            min = LONGITUDE_MIN;
        } else {
            max = LATITUDE_MAX;
            min = LATITUDE_MIN;
        }
        BigDecimal num = new BigDecimal(longitudeOrLatitude);
        BigDecimal two = new BigDecimal(TWO), x, y, z;
        StringBuilder stringBuilder = new StringBuilder();
        if (num.compareTo(new BigDecimal(ZERO)) >= 0) {
            stringBuilder.append(ONE);
            x = new BigDecimal(ZERO);
            y = new BigDecimal(max);
        } else {
            stringBuilder.append(ZERO);
            x = new BigDecimal(min);
            y = new BigDecimal(ZERO);
        }
        z = x.add(y).divide(two);
        for (int i = 1; i < 26; i++) {
            if (num.compareTo(z) >= 0) {
                stringBuilder.append(ONE);
                x = z;
                z = x.add(y).divide(two);
            } else {
                stringBuilder.append(ZERO);
                y = z;
                z = x.add(y).divide(two);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 合并经纬度的二进制编码
     */
    private static String merge(String longitude, String latitude) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < BIN_NUM; i++) {
            stringBuilder.append(longitude.charAt(i));
            stringBuilder.append(latitude.charAt(i));
        }
        return stringBuilder.toString();
    }


    /**
     * geohash编码
     */
    public static String base32Encode(String longitudeAndLatitudeBinCode) {
        StringBuilder geohash = new StringBuilder();
        for (int i = 0; i < MERGE_BIN_NUM; ) {
            int j = i;
            String substring;
            if ((i += 5) > MERGE_BIN_NUM) {
                substring = longitudeAndLatitudeBinCode.substring(j, MERGE_BIN_NUM);
            } else {
                substring = longitudeAndLatitudeBinCode.substring(j, i);
            }
            int i1 = Integer.parseInt(substring, 2);
            geohash.append(BASE_32_CHARS[i1]);
        }
        return geohash.toString();
    }

    /**
     * geohash解码
     */
    public static double[] base32Decode(String geohash) {
        StringBuilder buffer = getBin(geohash);
        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        //偶数位，经度
        int j = 0;
        for (int i = 0; i < MERGE_BIN_NUM; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            lonset.set(j++, isSet);
        }

        //奇数位，纬度
        j = 0;
        for (int i = 1; i < MERGE_BIN_NUM; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            latset.set(j++, isSet);
        }

        double lon = base32Decode(lonset, new BigDecimal(LONGITUDE_MIN), new BigDecimal(LONGITUDE_MAX));
        double lat = base32Decode(latset, new BigDecimal(LATITUDE_MIN), new BigDecimal(LATITUDE_MAX));

        return new double[]{lon, lat};
    }

    private static StringBuilder getBin(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {
            int i = BASE_32_LOOKUP.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }
        return buffer;
    }


    private static double base32Decode(BitSet bs, BigDecimal floor, BigDecimal ceiling) {
        BigDecimal two = new BigDecimal(TWO);
        BigDecimal mid = new BigDecimal(ZERO);
        for (int i = 0; i < bs.length(); i++) {
            mid = (floor.add(ceiling).divide(two, 17, BigDecimal.ROUND_HALF_EVEN));
            if (bs.get(i)) {
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return mid.doubleValue();
    }
}
