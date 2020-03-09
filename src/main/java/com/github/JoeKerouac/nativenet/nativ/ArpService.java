package com.github.JoeKerouac.nativenet.nativ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import com.github.JoeKerouac.nativenet.nativ.impl.NativeArpNetInterfaceImpl;
import com.joe.utils.concurrent.ThreadUtil;

/**
 * arp服务
 *
 * @author JoeKerouac
 * @version 2020年03月09日 19:32
 */
public class ArpService {

    /**
     * 查询时的空mac
     */
    private static byte[]                         EMPTY_MAC;

    /**
     * 本地arp接口
     */
    private static NativeArpNetInterface          NATIVE_INTERFACE;

    /**
     * arp接受sock
     */
    private static int                            RCV_SOCK;

    /**
     * ARP缓存
     */
    private static ConcurrentLinkedDeque<ArpData> ARP_CACHE;

    /**
     * ARP缓存最大长度
     */
    private static final int                      CACHE_SIZE = 1000;

    /**
     * 是否关闭
     */
    private static volatile boolean               SHUTDOWN   = false;

    static {
        EMPTY_MAC = new byte[] { 0, 0, 0, 0, 0, 0 };
        NATIVE_INTERFACE = new NativeArpNetInterfaceImpl();
        RCV_SOCK = NATIVE_INTERFACE.createSock();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SHUTDOWN = true;
            NATIVE_INTERFACE.close(RCV_SOCK);
        }));
        ARP_CACHE = new ConcurrentLinkedDeque<>();

        // 监听ARP消息线程
        new Thread(() -> {
            System.out.println("开始接受arp数据");
            while (!SHUTDOWN) {
                ArpData arpData = NATIVE_INTERFACE.receive_arp(RCV_SOCK);

                // 如果没有接收方，说明是想要查询mac的，忽略
                if (Arrays.equals(arpData.getDestMac(), EMPTY_MAC)) {
                    continue;
                }

                ARP_CACHE.remove(arpData);
                ARP_CACHE.add(arpData);

                System.out.println("线程接收到arp数据了：" + arpData);

                // 删除第一个
                if (ARP_CACHE.size() >= CACHE_SIZE) {
                    ARP_CACHE.removeFirst();
                }
            }
            System.out.println("arp数据接受完毕");
        }, "arp recv").start();

    }

    /**
     * 获取局域网所有ip对应的mac
     * @param localIpStr 本地ip，例如192.168.1.1
     * @param localMac 本地mac
     * @return 局域网所有ip对应的mac
     */
    public static List<ArpData> getAllMac(String localIpStr, byte[] localMac) {
        String destIpPre = localIpStr.substring(0, localIpStr.lastIndexOf(".") + 1);
        byte[] localIp = localIpStr.getBytes();
        ArpData arpData = new ArpData();

        int sock = -1;
        try {
            sock = NATIVE_INTERFACE.createSock();

            for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
                arpData.setSrcMac(localMac);
                arpData.setSrcIp(localIp);
                arpData.setDestIp((destIpPre + Byte.toUnsignedInt(i)).getBytes());
                arpData.setDestMac(EMPTY_MAC);
                int result = NATIVE_INTERFACE.sendArp(arpData, sock);
                System.out.println("发送请求：" + arpData + "；\n发送结果：" + result);
                // 隔200毫秒发
                ThreadUtil.sleep(200, TimeUnit.MILLISECONDS);
            }

            ThreadUtil.sleep(1);
            List<ArpData> list = new ArrayList<>();
            ARP_CACHE.forEach(src -> list.add(copyArpData(src)));
            return list;
        } finally {
            if (sock > 0) {
                NATIVE_INTERFACE.close(sock);
            }
        }
    }

    /**
     * copy ArpData
     * @param src 源ArpData
     * @return copy的ArpData
     */
    private static ArpData copyArpData(ArpData src) {
        ArpData copy = new ArpData();
        copy.setDestMac(Arrays.copyOf(src.getDestMac(), src.getDestMac().length));
        copy.setDestIp(Arrays.copyOf(src.getDestIp(), src.getDestIp().length));
        copy.setSrcMac(Arrays.copyOf(src.getSrcMac(), src.getSrcMac().length));
        copy.setSrcIp(Arrays.copyOf(src.getSrcIp(), src.getSrcIp().length));
        return copy;
    }

    /**
     * 从cache中查找指定ip的mac地址
     * @param ip ip
     * @return ArpData
     */
    private static byte[] getMac(byte[] ip) {
        for (ArpData data : ARP_CACHE) {
            if (Arrays.equals(data.getSrcIp(), ip)) {
                return data.getSrcMac();
            }
        }
        return null;
    }
}
