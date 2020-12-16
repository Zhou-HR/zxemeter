package com.gdiot.ssm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouHR
 */
public class Utilty {

    private static Utilty instance = new Utilty();

    public static Utilty getInstance() {
        return instance;
    }

    public static final int MIN_MID_VALUE = 1;

    public static final int MAX_MID_VALUE = 65535;

    public static String convertByteToString(byte[] array, int begin, int end) {
        StringBuffer sb = new StringBuffer();
        for (int i = end; i > begin - 1; i--) {
            sb.append(parseByte2HexStr(array[i - 1]));
        }
        return sb.toString();
    }

    public static byte getCheckChar(byte[] datas, int len) {
        byte cnt = (byte) 0x00;
        for (int i = 0; i < len; i++) {
            cnt = (byte) (cnt + datas[i]);
        }
        cnt = (byte) ~cnt;
        cnt = (byte) (cnt + (byte) 0x33);
        return cnt;
    }

    public int bytes2Int(byte[] b, int start, int length) {
        int sum = 0;
        int end = start + length;
        for (int k = start; k < end; k++) {
            int n = ((int) b[k]) & 0xff;
            n <<= (--length) * 8;
            sum += n;
        }
        return sum;
    }

    public byte[] int2Bytes(int value, int length) {
        byte[] b = new byte[length];
        for (int k = 0; k < length; k++) {
            b[length - k - 1] = (byte) ((value >> 8 * k) & 0xff);
        }
        return b;
    }

    public boolean isValidofMid(int mId) {
        if (mId < MIN_MID_VALUE || mId > MAX_MID_VALUE) {
            return false;
        }
        return true;
    }

    public static String parseByte2HexStr(byte buf) {
        String hex = Integer.toHexString(buf & 0xFF).toUpperCase();
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }

    public static String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String hexStringToASC(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /*
     * 分离字符串3200_0_5501，生成list后返回
     */
    public static List SplitString(String d_str) {
        String[] s = d_str.split("_");
        List list = new ArrayList();
        for (int i = 0; i < s.length; i++) {
//			System.out.println("s"+i+"=" + s[i]);
            list.add(s[i]);
        }
        return list;
    }

    public static String DecToHexString(int dec) {
        //将其转换为十六进制并输出
        String strHex = Integer.toHexString(dec);
        if (strHex.length() == 1) {//长度为1位时，高位补0
            return "0" + strHex;
        }
        return strHex;
    }


    public static String getType(Object o) { //获取变量类型方法
        return o.getClass().toString(); //使用int类型的getClass()方法
    }

    public static void main(String[] args) {
        System.out.println(hexStringToASC("41542B514C57554C4441544145583D34392C41413732303030303031343235353030303030303030303036303231303030303030303030303030393634393030303030303030444434303030303030303030303030303030303030303030303030303030303130313138303030303132303044352C3078303130300D0A"));
        ;
    }
}
