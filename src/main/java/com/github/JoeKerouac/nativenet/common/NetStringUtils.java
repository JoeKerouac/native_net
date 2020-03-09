package com.github.JoeKerouac.nativenet.common;

/**
 * 网络字符串工具包
 *
 * @author JoeKerouac
 * @version 2020年03月09日 18:34
 */
public class NetStringUtils {

    /**
     * 将ip数据转换为人类可读的字符串，例如192.168.1.1
     * @param datas ip数据
     * @return 人类可读的ip字符串，例如192.168.1.1
     */
    public static String toIpString(byte[] datas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            sb.append(Byte.toUnsignedInt(datas[i]));
            if (i < (datas.length - 1)) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * 将ip字符串转换为ip数据
     * @param ip ip字符串，例如192.168.1.1
     * @return 数据
     */
    public static byte[] toIpData(String ip) {
        String[] ipStrs = ip.trim().split("\\.");
        int len = ipStrs.length + ipStrs.length - 1;
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i % 2 == 0) {
                data[i] = (byte) Integer.parseInt(ipStrs[i]);
            } else {
                data[i] = '.';
            }
        }
        return data;
    }

    /**
     * 将mac原始数据转换为人类可读的mac字符串
     * @param datas mac原始数据
     * @return 人类可读的mac字符串，例如00:00:00:00:00:00
     */
    public static String toMacString(byte[] datas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            String hexStr = Integer.toHexString(Byte.toUnsignedInt(datas[i]));
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
            if (i < (datas.length - 1)) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    /**
     * 将mac字符串转换为mac数据
     * @param mac mac字符串，例如 f1:01:a0:11:00:00
     * @return mac数据
     */
    public static byte[] toMacData(String mac) {
        String[] ipStrs = mac.trim().split(":");
        byte[] data = new byte[ipStrs.length];
        for (int i = 0; i < ipStrs.length; i++) {
            data[i] = (byte) Integer.parseInt(ipStrs[i], 16);
        }
        return data;
    }
}
