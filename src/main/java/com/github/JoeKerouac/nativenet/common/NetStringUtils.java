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
     * @param datas ip数据，长度必须为4
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
     * 将mac原始数据转换为人类可读的mac字符串
     * @param datas
     * @return
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
}
